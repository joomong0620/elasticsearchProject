package edu.og.es.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import edu.og.es.document.ProductDocument;

// 엘라스틱 DB로 넣기 위해서 사용
public interface ProductDocumentRepository
		extends ElasticsearchRepository<ProductDocument, String>{
	
}
