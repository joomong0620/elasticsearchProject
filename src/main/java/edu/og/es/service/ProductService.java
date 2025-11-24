package edu.og.es.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.og.es.dto.ProductDto;
import edu.og.es.dto.ProductDto.Create;

public interface ProductService {

	// 상품 등록
	ProductDto.Response createProduct(Create product);

	// 상품 조회
	Page<ProductDto.Response> findProductList(Pageable pageable);

	// 상품 삭제
	void deleteProduct(Long productId);


	

}
