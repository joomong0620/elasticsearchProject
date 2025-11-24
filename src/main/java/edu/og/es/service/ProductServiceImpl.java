package edu.og.es.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.og.es.dto.ProductDto;
import edu.og.es.dto.ProductDto.Create;
import edu.og.es.dto.ProductDto.Response;
import edu.og.es.entity.Product;
import edu.og.es.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
	private final ProductRepository productRepository;
	
	// 상품 등록
	@Override
	public Response createProduct(Create product) { // 변수명 중요 X
												//product나  productEntity나 안겹치는 게 중요
		// DTO -> Entity
		Product productEntity = product.toEntity();
		
		// Entity를 DB에 저장
		Product savedProduct = productRepository.save(productEntity);
		
		
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
		
	}



}
