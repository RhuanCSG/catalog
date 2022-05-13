package rhuancarlos.catalog.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import rhuancarlos.catalog.dto.ProductDTO;
import rhuancarlos.catalog.entities.Category;
import rhuancarlos.catalog.entities.Product;
import rhuancarlos.catalog.repositories.CategoryRepository;
import rhuancarlos.catalog.repositories.ProductRepository;
import rhuancarlos.catalog.services.exception.DataBaseException;
import rhuancarlos.catalog.services.exception.ResourceNotFoundException;
import rhuancarlos.catalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository productRepository;
	@Mock
	private CategoryRepository categoryRepository;

	private long existingId;
	private long nonExistingId;
	private long dependetId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	private ProductDTO productDto;


	@BeforeEach
	void SetUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		dependetId = 3L;
		productDto = Factory.createProductDto();
		product = Factory.createProduct();
		category = Factory.createCategory();
		page = new PageImpl<>(List.of(product));
		
		when(productRepository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
		
		when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
		when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		when(productRepository.getOne(existingId)).thenReturn(product);
		when(productRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		when(categoryRepository.getOne(existingId)).thenReturn(category);
		when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		doNothing().when(productRepository).deleteById(existingId);
		doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
		doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependetId);
		
	}
	
	@Test
	public void insertShouldReturnProductDTO() {
		ProductDTO result = service.insert(productDto);
		Assertions.assertNotNull(result);
	}

	@Test 
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllPaged(pageable);
		Assertions.assertNotNull(result);
		verify(productRepository, times(1)).findAll(pageable);
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO result = service.findById(existingId);
		Assertions.assertNotNull(result);
		verify(productRepository, times(1)).findById(existingId);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
		verify(productRepository, times(1)).findById(nonExistingId);
	}
	
	@Test
	public void updateShouldReturnProductDTOWheIdExists() {
		ProductDTO result = service.update(existingId, productDto);
		Assertions.assertNotNull(result);
		verify(productRepository, times(1)).getOne(existingId);
		verify(productRepository, times(1)).save(product);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenNonExistingId() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, productDto);
		});
		verify(productRepository, times(1)).getOne(nonExistingId);
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {

		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});

		verify(productRepository, times(1)).deleteById(existingId);
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});

		verify(productRepository, times(1)).deleteById(nonExistingId);
	}
	@Test
	public void deleteShouldThrowDataBaseExceptionWhenDependentId() {

		Assertions.assertThrows(DataBaseException.class, () -> {
			service.delete(dependetId);
		});

		verify(productRepository, times(1)).deleteById(dependetId);
	}

}
