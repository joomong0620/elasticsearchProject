package edu.og.es.document;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.Setting;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@ToString
@Document(indexName="products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Setting(settingPath="/elasticsearch/product-settings.json")
public class ProductDocument {
	
	@Id
	// elasticsearch가 자동으로 id를 문자열로 생성
	private String id;
	
	
	@MultiField(mainField=@Field(type=FieldType.Text,
			        analyzer = "products_name_analyzer"),
			
			otherFields= {@InnerField(suffix="auto_complete",
					type=FieldType.Search_As_You_Type,
					analyzer="nori")})
	private String name;	
	
	
	@Field(type=FieldType.Text,
			analyzer="products_description_analyzer")
	private String description;
	
	
	@Field(type=FieldType.Integer)
	private Integer price;
	
	
	
	@Field(type=FieldType.Double)
	private Double rating;
	
	
	
	@MultiField(mainField=@Field(type=FieldType.Text,
			analyzer="products_description_analyzer"),
			
			otherFields= {@InnerField(suffix="row",
				type=FieldType.Keyword)})
	private String category;
	
	
	
}
