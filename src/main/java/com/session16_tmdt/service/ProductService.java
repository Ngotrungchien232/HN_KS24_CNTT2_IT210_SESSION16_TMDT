package com.session16_tmdt.service;


import com.session16_tmdt.model.Product;
import com.session16_tmdt.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public Page<Product> search(String keyword, Long categoryId, Double minPrice, Double maxPrice, Pageable pageable) {
        return productRepository.searchProducts(keyword, categoryId, minPrice, maxPrice, pageable);
    }

    public void updateStock(Long productId, int quantity) {
        Product p = findById(productId);
        if (p.getStock() < quantity) throw new RuntimeException("Sản phẩm " + p.getName() + " không đủ hàng");
        p.setStock(p.getStock() - quantity);
        productRepository.save(p);
    }
}
