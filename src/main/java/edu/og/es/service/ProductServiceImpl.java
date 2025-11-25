package edu.og.es.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NumberRangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import edu.og.es.document.ProductDocument;
import edu.og.es.dto.ProductDto;
import edu.og.es.dto.ProductDto.Create;
import edu.og.es.dto.ProductDto.Response;
import edu.og.es.entity.Product;
import edu.og.es.repository.ProductDocumentRepository;
import edu.og.es.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
	private final ProductRepository productRepository;
	private final ProductDocumentRepository productDocumentRepository;
	private final ElasticsearchOperations elasticsearchOperations;
	//ElasticsearchOperations : JPA의 EntityManager에 해당하는 역할을 수행
	// 							JSON 쿼리를 만들어서 ES 서버에 전송
	
	// 상품 등록
	@Override
	public Response createProduct(Create product) { // 변수명 중요 X
												//product나  productEntity나 안겹치는 게 중요
		// DTO -> Entity
		Product productEntity = product.toEntity();
		
		// Entity를 DB에 저장
		Product savedProduct = productRepository.save(productEntity);
		
		// Document 객체 생성
		ProductDocument productDocument 
						= new ProductDocument(
						savedProduct.getId().toString(),
						savedProduct.getName(),
						savedProduct.getDescription(),
						savedProduct.getPrice(),
						savedProduct.getRating(),
						savedProduct.getCategory());
		
		// 엘라스틱서치에 저장
		productDocumentRepository.save(productDocument);
		
		// Entity -> DTO
		return ProductDto.Response.toDto(savedProduct);
	}

	
	// 상품 목록 조회
	@Override
	public Page<Response> findProductList(Pageable pageable) {
		Page<Product> page = productRepository.findAll(pageable);
		return page.map(ProductDto.Response::toDto);
	}


	// 상품 삭제
	@Override
	public void deleteProduct(Long productId) {
		Product product = productRepository.findById(productId)
				
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
		
		productRepository.delete(product);
		
		
		//
		
	}


	// 자동완성/추천
	@Override
	public List<String> getSuggestions(String query) {
		
											// of : 내부적으로 MultiMatchQuery에 
											// builder 생성해줌
											// 매개변수 m에다가 쿼리 담아주고, type도 작성해줌
											// 이제 new builder이런거 안해도 됨
		
		// of() : 내부에서 new Builder()를 대신 생성해주는 역할
		Query multiMatchQuery = MultiMatchQuery.of(m -> 
				m
				.query(query)
				.type(TextQueryType.BoolPrefix)
				.fields("name.auto_complete",
				        "name.auto_complete._2gram",
				        "name.auto_complete._3gram")
				)._toQuery();
		
								//NativeQuery : 포장지 느낌
		NativeQuery nativeQuery = NativeQuery.builder()
				.withQuery(multiMatchQuery)
				.withPageable(PageRequest.of(0, 5)) // from :0 , size : 5
				.build();
		
		// 작성한 쿼리문을 JSON 형식으로 만들어서 Elasticsearch에서 조회
		SearchHits<ProductDocument> searchHits
				= elasticsearchOperations.search(nativeQuery, ProductDocument.class);
		return searchHits.getSearchHits()
				.stream() 
				.map(hit -> {
					ProductDocument productDocument = hit.getContent(); 
					return productDocument.getName();
				})
				.toList();
	}


	// elasticsearch 기반 상품 검색
	@Override
	public List<Response> searchProducts(String query, String category, double minPrice, double maxPrice, int page,
			int size) {
		
		// multi_match 쿼리
		Query multiMatchQuery = MultiMatchQuery.of(m -> 
					m.query(query)
					.fields("name^3", "description^1", "category^2")
					.fuzziness("AUTO")
					)._toQuery();
		
		// 2) filter 
		List<Query> filters = new ArrayList<>();
		// 2-1) term 쿼리 : 카테고리가 정확히 일치하는 것만 필터링
		if(category != null && !category.isEmpty()) {
			Query categoryFilter = TermQuery.of(t -> 
					t.field("category.raw")
					.value(category)
					)._toQuery();
		}
		
		// 2-2) range 쿼리 : 가격 볌위 필터
		Query priceRangeFilter = NumberRangeQuery.of(r ->
				r.field("price")
				.gte(minPrice)
				.lte(maxPrice)
				)._toRangeQuery()._toQuery();
		filters.add(priceRangeFilter);
		
		
		
		// 3) should - 평점이 4.0 넘는 상품 상위 노츨
		Query ratingShould = NumberRangeQuery.of(r ->
				r.field("rating")
				.gt(4.0)
				)._toRangeQuery()._toQuery();
		
		filters.add(ratingShould);
		
		// 4) bool 쿼리 조립
		Query boolQuery = BoolQuery.of(b -> b
				.must(multiMatchQuery)
				.filter(filters)
				.should(ratingShould)
				)._toQuery();
		
		// 5) highlight 쿼리
		
		// 5-1) 검색어 앞뒤에 <b></b> 추가 설정
		HighlightParameters highlightParameters 
			= HighlightParameters.builder()
				.withPreTags("<b>") // 태그 앞뒤로 어떤거 붙힐지
				.withPostTags("</b>")
				.build();
		
		// 5-2) 하이라이팅할 필드 설정 
		Highlight highlight
			= new Highlight(highlightParameters, List.of(new HighlightField("name")));
		// new HighlightField("name") : 하이라이팅할 필드
		// 뒤에다가 내가 추가하고자하는 거 인자로 넘겨주면 됨
		
		
		
		// 5-3) 하이라이트 쿼리 생성
		HighlightQuery highlightQuery 
				= new HighlightQuery(highlight, ProductDocument.class);
		
		// 6) NativeQuery 생성
		NativeQuery nativeQuery = NativeQuery.builder()
				.withQuery(boolQuery)
				.withHighlightQuery(highlightQuery)
				.withPageable(PageRequest.of(page, size))
				.build();
		
		// 7) 쿼리 실행
		SearchHits<ProductDocument> searchHits
		 	= elasticsearchOperations.search(nativeQuery, ProductDocument.class);
		return searchHits.getSearchHits()
				.stream()
				.map(hit -> {
					ProductDocument productDocument = hit.getContent();
					// hit.getContent() : id, name, description 등 정보가 담겨 있음
					String highlightedName = hit.getHighlightField("name").get(0);
					productDocument.setName(highlightedName);
					return ProductDto.Response.toDto(productDocument);
				}).toList();
	}
	



}
