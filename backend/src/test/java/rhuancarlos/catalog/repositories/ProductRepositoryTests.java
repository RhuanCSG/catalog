package rhuancarlos.catalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import rhuancarlos.catalog.entities.Product;
import rhuancarlos.catalog.services.exception.ResourceNotFoundException;
import rhuancarlos.catalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExtistingId;
	private Long countProduct;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExtistingId = 2L;
		countProduct = 3L;
	}
	
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		repository.save(product);
		
		Assertions.assertNotNull(product);
		Assertions.assertEquals(countProduct+1, product.getId());
	}
	
	@Test
	public void findByIdShouldReturnOptionalNotEmptyWhenIdExists() {
		Optional<Product> result = repository.findById(existingId);
		
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Optional<Product> result = repository.findById(nonExtistingId);

		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void deletShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(existingId);
		
		Optional<Product> result = repository.findById(existingId);
		Assertions.assertTrue(result.isEmpty());
		
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExtistingId);
		});
		
	}

}
