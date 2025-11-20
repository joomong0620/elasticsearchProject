package edu.og.es.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(indexName="users")
@Builder
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor

@Getter
@Setter
public class UserDocument {
	// mapping : 인덱스 구조 전체
	// mapping 생성 : 데이터가 저장될 구조를 만드는 것
	
	// @Id
	// @Field : 데이터의 속성을 정의하는 어노테이션
	
	@Id
	private String id; // Elasticsearch에서는 Document ID를 문자열(String)으로 다룬다.
	
	@Field(type= FieldType.Keyword) // @Field : 컬럼 이름, 타입, 옵션 설정(==column 정의)
	private String name; // 매핑 항목 : 실제 데이터의 각 속성(필드) (==column)
	
	@Field(type=FieldType.Integer)
	private Integer age;
	
	@Field(type=FieldType.Boolean)
	private Boolean isActive;
	
}
