package edu.og.es.dto;

import edu.og.es.document.ProductDocument;
import edu.og.es.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class ProductDto {
 
	// 상품 생성 DTO
	@Getter
	@Setter
	@AllArgsConstructor
	public static class Create{
		private String productName;
		private String description;
		private int price;
		private double rating;
		private String category;
		
		// Product DTO를 Entity로 변환
		public Product toEntity() {
			return Product.builder()
				.name(this.productName)
				.description(this.description)
				.price(this.price)
				.rating(this.rating)
				.category(this.category)
				.build();
		}
		
	}
	// 상품 응답 DTO
	@Getter
	@Builder
	@AllArgsConstructor
	public static class Response{
		private String productId;
		private String productName;
		private String description;
		private int price;
		private double rating;
		private String category;
		
		// Product Entity를 DTO로 변환
		public static Response toDto(Product p){
			return Response.builder()
					.productId(p.getId() + "") // 문자열로 만들어 쓸거면
					.productName(p.getName())
					.description(p.getDescription())
					.price(p.getPrice())
					.rating(p.getRating())
					.category(p.getCategory())
					.build();
		}
		// Product Document를 DTO로 변환
		public static Response toDto(ProductDocument p){
			return Response.builder()
					.productId(p.getId() + "") // 문자열로 만들어 쓸거면
					.productName(p.getName())
					.description(p.getDescription())
					.price(p.getPrice())
					.rating(p.getRating())
					.category(p.getCategory())
					.build();
			
				
			
		}
			
	}
	
		


}
