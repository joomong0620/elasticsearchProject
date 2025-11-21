package edu.og.es.repository;

import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import edu.og.es.document.UserDocument;


public interface UserDocumentRepository 
			extends ElasticsearchRepository<UserDocument, String>{

	// interface 간의 상속은 extends를 사용한다.
	Optional<UserDocument> findByName(String name);

	
}
