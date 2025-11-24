package edu.og.es.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.og.es.entity.Product;

//
public interface ProductRepository extends JpaRepository<Product, Long> {

}
