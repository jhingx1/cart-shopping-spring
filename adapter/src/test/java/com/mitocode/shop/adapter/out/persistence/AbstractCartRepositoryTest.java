package com.mitocode.shop.adapter.out.persistence;

import static com.mitocode.shop.model.customer.TestCustomerIdFactory.randomCustomerId;
import static com.mitocode.shop.model.money.TestMoneyFactory.euros;
import static com.mitocode.shop.model.product.TestProductFactory.createTestProduct;
import static org.assertj.core.api.Assertions.assertThat;

import com.mitocode.shop.application.port.out.persistence.CartRepository;
import com.mitocode.shop.application.port.out.persistence.ProductRepository;
import com.mitocode.shop.model.cart.Cart;
import com.mitocode.shop.model.cart.CartLineItem;
import com.mitocode.shop.model.cart.NotEnoughItemsInStockException;
import com.mitocode.shop.model.customer.CustomerId;

import java.util.Optional;

import com.mitocode.shop.model.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractCartRepositoryTest {

    private static final Product TEST_PRODUCT_1 = createTestProduct(euros(19, 99));
    private static final Product TEST_PRODUCT_2 = createTestProduct(euros(1, 49));

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void initRepositories() {
        persistTestProducts();
    }

    private void persistTestProducts() {
        productRepository.save(TEST_PRODUCT_1);
        productRepository.save(TEST_PRODUCT_2);
    }

    @Test
    void givenACustomerIdForWhichNoCartIsPersisted_findByCustomerId_returnsAnEmptyOptional() {
        CustomerId customerId =randomCustomerId();
        Optional<Cart> cart = cartRepository.findByCustomerId(customerId);

        assertThat(cart).isEmpty();
    }

    @Test
    void givenPersistedCartWithProduct_findByCustomerId_returnsTheAppropriateCart()
            throws NotEnoughItemsInStockException {
        CustomerId customerId =randomCustomerId();

        Cart persistedCart = new Cart(customerId);
        persistedCart.addProduct(TEST_PRODUCT_1, 1);
        cartRepository.save(persistedCart);

        Optional<Cart> cart = cartRepository.findByCustomerId(customerId);

        assertThat(cart).isNotEmpty();
        assertThat(cart.get().id()).isEqualTo(customerId);
        assertThat(cart.get().lineItems()).hasSize(1);
        assertThat(cart.get().lineItems().get(0).product()).isEqualTo(TEST_PRODUCT_1);
        assertThat(cart.get().lineItems().get(0).quantity()).isEqualTo(1);
    }

    @Test
    void
    givenExistingCartWithProduct_andGivenANewCartForTheSameCustomer_saveCart_overwritesTheExistingCart()
            throws NotEnoughItemsInStockException {
        CustomerId customerId =randomCustomerId();

        Cart existingCart = new Cart(customerId);
        existingCart.addProduct(TEST_PRODUCT_1, 1);
        cartRepository.save(existingCart);

        Cart newCart = new Cart(customerId);
        newCart.addProduct(TEST_PRODUCT_2, 2);
        cartRepository.save(newCart);

        Optional<Cart> cart = cartRepository.findByCustomerId(customerId);
        assertThat(cart).isNotEmpty();
        assertThat(cart.get().id()).isEqualTo(customerId);
        assertThat(cart.get().lineItems()).hasSize(1);
        assertThat(cart.get().lineItems().get(0).product()).isEqualTo(TEST_PRODUCT_2);
        assertThat(cart.get().lineItems().get(0).quantity()).isEqualTo(2);
    }

    @Test
    void givenExistingCartWithProduct_addProductAndSaveCart_updatesTheExistingCart()
            throws NotEnoughItemsInStockException {
        CustomerId customerId =randomCustomerId();

        Cart existingCart = new Cart(customerId);
        existingCart.addProduct(TEST_PRODUCT_1, 1);
        cartRepository.save(existingCart);

        existingCart = cartRepository.findByCustomerId(customerId).orElseThrow();
        existingCart.addProduct(TEST_PRODUCT_2, 2);
        cartRepository.save(existingCart);

        Optional<Cart> cart = cartRepository.findByCustomerId(customerId);
        assertThat(cart).isNotEmpty();
        assertThat(cart.get().id()).isEqualTo(customerId);
        assertThat(cart.get().lineItems())
                .map(CartLineItem::product)
                .containsExactlyInAnyOrder(TEST_PRODUCT_1, TEST_PRODUCT_2);
    }

    @Test
    void givenExistingCart_deleteByCustomerId_deletesTheCart() {
        CustomerId customerId =randomCustomerId();

        Cart existingCart = new Cart(customerId);
        cartRepository.save(existingCart);

        assertThat(cartRepository.findByCustomerId(customerId)).isNotEmpty();

        cartRepository.deleteByCustomerId(customerId);

        assertThat(cartRepository.findByCustomerId(customerId)).isEmpty();
    }

    @Test
    void givenNotExistingCart_deleteByCustomerId_doesNothing() {
        CustomerId customerId =randomCustomerId();
        assertThat(cartRepository.findByCustomerId(customerId)).isEmpty();

        cartRepository.deleteByCustomerId(customerId);

        assertThat(cartRepository.findByCustomerId(customerId)).isEmpty();
    }

}
