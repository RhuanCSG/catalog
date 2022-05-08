package rhuancarlos.catalog.tests;

import java.time.Instant;

import rhuancarlos.catalog.dto.ProductDTO;
import rhuancarlos.catalog.entities.Category;
import rhuancarlos.catalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product product = new Product(1L, "Iphone", "good phone", 5000.00, "https://store.storeimages.cdn-apple.com/",
				Instant.parse("2022-04-29T16:24:00Z"));
		product.getCategories().add(createCategory());
		return product;
	}

	public static ProductDTO createProductDto() {
		Product product = createProduct();		
		return new ProductDTO(product, product.getCategories());
		
	}

	public static Category createCategory() {
		return new Category(1L,"Electronics");
		
	}

}
