package rhuancarlos.catalog.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rhuancarlos.catalog.entities.Category;
import rhuancarlos.catalog.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

	@Autowired
	private CategoryService service;

	@GetMapping
	ResponseEntity<List<Category>> findAll() {

		List<Category> list = service.findAll();
		return ResponseEntity.ok().body(list);

	}
}
