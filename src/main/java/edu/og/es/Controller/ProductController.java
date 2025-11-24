package edu.og.es.Controller;

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
	        sort="name", direction = Sort.Direction.DESC) Pageable pageable){
		return ResponseEntity.ok(new PageResponse<>(productService.findProductList(pageable)));
	}
	
	
	// 상품 삭제
	@DeleteMapping("/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long productId){
		productService.deleteProduct(productId);
		return ResponseEntity.ok().build();
	}
	
}
