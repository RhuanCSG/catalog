package rhuancarlos.catalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import rhuancarlos.catalog.dto.ProductDTO;
import rhuancarlos.catalog.repositories.ProductRepository;
import rhuancarlos.catalog.services.exception.ResourceNotFoundException;
import rhuancarlos.catalog.tests.Factory;

@SpringBootTest
@Transactional
public class ProductServiceIntegrationTests {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
	private ProductDTO productDto;

	@BeforeEach
	void setUp() throws Exception {

		productDto = Factory.createProductDto();

		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
	}

	@Test
	public void insertShouldReturnProductDTO() {

		ProductDTO product = productService.insert(productDto);
		Assertions.assertNotNull(product);
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {

		ProductDTO product = productService.update(existingId, productDto);
		Assertions.assertNotNull(product);
	}

	@Test
	public void updateShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productService.update(nonExistingId, productDto);
		});
	}

	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO product = productService.findById(existingId);

		Assertions.assertNotNull(product);
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productService.findById(nonExistingId);
		});
	}

	@Test
	public void deleteShouldDeleteResourceWhenIdExits() {
		productService.delete(existingId);

		Assertions.assertEquals(countTotalProducts - 1, productRepository.count());
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExit() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productService.delete(nonExistingId);
		});

	}

	@Test
	public void findAllPagedShouldReturnPage0Size10() {

		PageRequest pageRequest = PageRequest.of(0, 10);

		Page<ProductDTO> result = productService.findAllPaged(pageRequest);

		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
	}

	@Test
	public void findAllPagedShouldReturnEmptyWhenPageDoesNotExists() {
		PageRequest pageRequest = PageRequest.of(50, 10);

		Page<ProductDTO> result = productService.findAllPaged(pageRequest);

		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortByName() {
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));

		Page<ProductDTO> result = productService.findAllPaged(pageRequest);

		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
	}

}
