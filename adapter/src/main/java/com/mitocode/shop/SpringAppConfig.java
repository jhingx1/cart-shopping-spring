package com.mitocode.shop;

import com.mitocode.shop.application.port.out.persistence.CartRepository;
import com.mitocode.shop.application.port.out.persistence.ProductRepository;
import com.mitocode.shop.application.port.in.cart.GetCartUseCase;
import com.mitocode.shop.application.port.in.cart.EmptyCartUseCase;
import com.mitocode.shop.application.port.in.cart.AddToCartUseCase;
import com.mitocode.shop.application.port.in.product.FindProductsUseCase;
import com.mitocode.shop.application.service.cart.AddToCartService;
import com.mitocode.shop.application.service.cart.EmptyCartService;
import com.mitocode.shop.application.service.cart.GetCartService;
import com.mitocode.shop.application.service.product.FindProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAppConfig {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Bean
    GetCartUseCase GetCartUseCase(){
        return new GetCartService(cartRepository);
    }

    @Bean
    EmptyCartUseCase EmptyCartUseCase(){
        return new EmptyCartService(cartRepository);
    }

    @Bean
    AddToCartUseCase AddToCartUseCase(){
        return new AddToCartService(cartRepository,productRepository);
    }

    @Bean
    FindProductsUseCase FindProductsUseCase(){
        return new FindProductsService(productRepository);
    }

}