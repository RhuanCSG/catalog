package rhuancarlos.catalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rhuancarlos.catalog.dto.CategoryDTO;
import rhuancarlos.catalog.dto.ProductDTO;
import rhuancarlos.catalog.entities.Category;
import rhuancarlos.catalog.entities.Product;
import rhuancarlos.catalog.repositories.CategoryRepository;
import rhuancarlos.catalog.repositories.ProductRepository;
import rhuancarlos.catalog.services.exception.DataBaseException;
import rhuancarlos.catalog.services.exception.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> list = repository.findAll(pageRequest);
			
		return list.map(x -> new ProductDTO(x));
	}
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyToDto(dto, entity);
		entity = repository.save(entity);
		
		return new ProductDTO(entity);
	}


	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getById(id);
			copyToDto(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO (entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("id not found " + id);
		}
		
	}

	

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("id not found " + id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity Violation!"); 
		}
		
	}
	
	private void copyToDto(ProductDTO dto, Product entity) {
		
		entity.setName(dto.getName());
		entity.setDescripton(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
		
		for (CategoryDTO catDto : dto.getCategories()) {
			Category category = categoryRepository.getById(catDto.getId());
			entity.getCategories().add(category);
		}
		
	}
	
	
}
