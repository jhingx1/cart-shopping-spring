package com.mitocode.shop.application.port.in.cart;

import com.mitocode.shop.model.cart.Cart;
import com.mitocode.shop.model.customer.CustomerId;

public interface GetCartUseCase {
    Cart getCart(CustomerId customerId);
}
