package com.mitocode.shop.adapter.out.persistence.inmemory;

import com.mitocode.shop.adapter.out.persistence.DemoProducts;
import com.mitocode.shop.application.port.out.persistence.ProductRepository;
import com.mitocode.shop.model.product.Product;
import com.mitocode.shop.model.product.ProductId;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import java.util.concurrent.ConcurrentHashMap;

@ConditionalOnProperty(name = "persistence", havingValue = "inmemory", matchIfMissing = true)
@Repository
public class InMemoryProductRepository implements ProductRepository{

    private final Map<ProductId, Product> products = new ConcurrentHashMap<>();

    public InMemoryProductRepository() {
        createDemoProducts();
    }

    private void createDemoProducts(){
        DemoProducts.DEMO_PRODUCTS.forEach(this::save);
    }

    @Override
    public void save(Product product) {
        products.put(product.id(),product);
    }

    @Override
    public Optional<Product> findById(ProductId productId) {
        return Optional.ofNullable(products.get(productId));
    }

    @Override
    public List<Product> findByNameOrDescripcion(String query) {
        String queryLowerCase = query.toLowerCase(Locale.ROOT);
        return products.values().stream()
                .filter(p -> matchesQuery(p,queryLowerCase))
                .toList();
    }

    private boolean matchesQuery(Product product, String query){
        return product.name().toLowerCase(Locale.ROOT).contains(query)
                || product.descripcion().toLowerCase(Locale.ROOT).contains(query);
    }


}
