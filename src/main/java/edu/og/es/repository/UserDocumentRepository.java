package edu.og.es.repository;

import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import edu.og.es.document.UserDocument;


public interface UserDocumentRepository 
			extends ElasticsearchRepository<UserDocument, String>{

	Optional<UserDocument> findByName(String name);
	
}
