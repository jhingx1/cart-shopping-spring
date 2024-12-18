package com.mitocode.shop.application.port.in.product;

import java.util.List;
import com.mitocode.shop.model.product.Product;

public interface FindProductsUseCase {

    List<Product> findByNameOrDescription(String query);

}
