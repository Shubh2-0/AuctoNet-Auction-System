package com.masai.Dao;

import com.masai.Dto.Product;

public interface ProductDao {

	boolean updateQuantity(int productId, int quantity);

	int getGStPercentage(int productId);

	Product getProductById(int productId);

	boolean updateProductBySeller(Product product, int id);

	boolean addProductBySeller(Product product);

	int getProductIdByCategoryName(String catgoryName);

}
