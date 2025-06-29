package com.nk.service.impl;

import com.nk.model.Product;
import com.nk.repository.ProductRepository;
import com.nk.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product saveProduct(Product product) {
        // Initialize version to 0 if it's null to prevent DataIntegrityViolationException
        if (product.getVersion() == null) {
            product.setVersion(0L);
        }
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();

            // Copy the version from the existing product to the product details
            // This ensures that the version check will work correctly
            productDetails.setVersion(existingProduct.getVersion());

            // Set the ID to ensure we're updating the existing entity
            productDetails.setId(id);

            // Save the product details directly instead of copying fields
            // This ensures that the version field is properly handled
            return productRepository.save(productDetails);
        }

        return null;
    }

    @Override
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
