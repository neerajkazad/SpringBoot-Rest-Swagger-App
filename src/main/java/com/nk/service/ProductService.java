package com.nk.service;

import com.nk.model.Product;
import java.util.List;

public interface ProductService {
    
    /**
     * Save a new product
     * @param product the product to save
     * @return the saved product
     */
    Product saveProduct(Product product);
    
    /**
     * Get all products
     * @return list of all products
     */
    List<Product> getAllProducts();
    
    /**
     * Get a product by its ID
     * @param id the product ID
     * @return the product if found, otherwise null
     */
    Product getProductById(Long id);
    
    /**
     * Update an existing product
     * @param id the product ID
     * @param productDetails the updated product details
     * @return the updated product
     */
    Product updateProduct(Long id, Product productDetails);
    
    /**
     * Delete a product by its ID
     * @param id the product ID
     * @return true if deleted successfully, false otherwise
     */
    boolean deleteProduct(Long id);
}