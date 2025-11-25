package edu.og.es.Controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.og.es.dto.PageResponse;
import edu.og.es.dto.ProductDto;
import edu.og.es.service.ProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;
	
	// 상품 생성
	@PostMapping
	public ResponseEntity<ProductDto.Response> createProduct(@RequestBody ProductDto.Create product){
		//상픔 등록 후 상품 정보 반환
		return ResponseEntity.ok(productService.createProduct(product));
	}
	
	
	// 상품 목록 조회
	@GetMapping()
	public ResponseEntity<PageResponse<ProductDto.Response>> findProductList(
			@PageableDefault(size=10, page=0,
	        sort="id", direction = Sort.Direction.DESC) Pageable pageable){
		return ResponseEntity.ok(new PageResponse<>(productService.findProductList(pageable)));
	}
	
	
	// 상품 삭제
	@DeleteMapping("/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long productId){
		productService.deleteProduct(productId);
		return ResponseEntity.ok().build(); // ok이니까 200번 반환됨.
	}
	
	
	// 자동완성/ 추천
	@GetMapping("/suggestions")// List인데 타입이 스트링으로 제한됨.
	public ResponseEntity<List<String>> getSuggestions(@RequestParam String query){
		return ResponseEntity.ok(productService.getSuggestions(query));
	}
						
	
	// Elasticsearch 기반 상품 검색
	@GetMapping("/search")
	public ResponseEntity<List<ProductDto.Response>> searchProducts(
			@RequestParam String query,
			@RequestParam(required=false) String category,
			@RequestParam(defaultValue = "0") double minPrice,
			@RequestParam(defaultValue = "100000000") double maxPrice,
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "5") int size){
		
		return ResponseEntity.ok(productService.searchProducts(query,
				category, minPrice, maxPrice, page, size));
	}
	
	
}
