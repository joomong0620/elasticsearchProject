package edu.og.es.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="products")
@NoArgsConstructor(access=AccessLevel.PROTECTED) //JPA 스펙상 필수 + 외부 생성 방지
@AllArgsConstructor
@Builder
@Getter
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// DB가 PK 값을 자동 생성
	private Long id;
	
	@Column(nullable=false)
	private String name;
	
	@Lob // 대용량 텍스트 저장용 
	@Column(columnDefinition = "CLOB", nullable = false)
	private String description; // HTML로 상품 설명 저장
	private int price;
	private double rating;
	private String category;
}
