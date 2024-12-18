package com.mitocode.shop.application.service.cart;

import com.mitocode.shop.application.port.in.cart.ProductNotFoundException;
import com.mitocode.shop.application.port.out.persistence.CartRepository;
import com.mitocode.shop.application.port.out.persistence.ProductRepository;
import com.mitocode.shop.model.cart.Cart;
import com.mitocode.shop.model.cart.NotEnoughItemsInStockException;
import com.mitocode.shop.model.customer.CustomerId;
import com.mitocode.shop.model.product.Product;
import com.mitocode.shop.model.product.ProductId;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.mitocode.shop.model.money.TestMoneyFactory.euros;
import static com.mitocode.shop.model.product.TestProductFactory.createTestProduct;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class AddCartServiceTest {

    private static final CustomerId TEST_CUSTOMER_ID = new CustomerId(61157);
    private static final Product TEST_PRODUCT_1 = createTestProduct(euros(19,99));
    private static final Product TEST_PRODUCT_2 = createTestProduct(euros(25,99));

    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ProductRepository productRepository = mock(ProductRepository.class);

    private final AddToCartService addToCartService = new AddToCartService(cartRepository,productRepository);

    //private static final Logger logger = LoggerFactory.getLogger(AddCartServiceTest.class);

    @BeforeEach
    void initTestDouble(){
        when(productRepository.findById(TEST_PRODUCT_1.id())).thenReturn(Optional.of(TEST_PRODUCT_1));
        when(productRepository.findById(TEST_PRODUCT_2.id())).thenReturn(Optional.of(TEST_PRODUCT_2));
    }

    @Test
    @DisplayName("given existing cart")
    void test1() throws NotEnoughItemsInStockException, ProductNotFoundException {
        Cart persistedCart = new Cart(TEST_CUSTOMER_ID);
        persistedCart.addProduct(TEST_PRODUCT_1,1);
        //logger.info(String.valueOf(persistedCart));

        when(cartRepository.findByCustomerId(TEST_CUSTOMER_ID)).thenReturn(Optional.of(persistedCart));

        Cart cart = addToCartService.addToCart(TEST_CUSTOMER_ID,TEST_PRODUCT_2.id(),3);
        //logger.info(String.valueOf(cart));

        verify(cartRepository).save(cart);
        //logger.info(String.valueOf(cart.lineItems().size()));

        assertThat(cart.lineItems()).hasSize(2);
        assertThat(cart.lineItems().get(0).product()).isEqualTo(TEST_PRODUCT_1);
        assertThat(cart.lineItems().get(0).quantity()).isEqualTo(1);

        assertThat(cart.lineItems().get(1).product()).isEqualTo(TEST_PRODUCT_2);
        assertThat(cart.lineItems().get(1).quantity()).isEqualTo(3);
    }

    @Test
    @DisplayName("given no existing cart")
    void test2() throws NotEnoughItemsInStockException, ProductNotFoundException {
        Cart cart = addToCartService.addToCart(TEST_CUSTOMER_ID,TEST_PRODUCT_1.id(),2);
        //logger.info(String.valueOf(cart));

        verify(cartRepository).save(cart);
        assertThat(cart.lineItems()).hasSize(1);
        assertThat(cart.lineItems().get(0).product()).isEqualTo(TEST_PRODUCT_1);
        assertThat(cart.lineItems().get(0).quantity()).isEqualTo(2);

    }

    @Test
    @DisplayName("given an unknow productid")
    void test3(){
        ProductId productId = ProductId.randomProductId();
        ThrowableAssert.ThrowingCallable invocation = () -> addToCartService.addToCart(TEST_CUSTOMER_ID,productId,2);
        assertThatExceptionOfType(ProductNotFoundException.class).isThrownBy(invocation);

        verify(cartRepository,never()).save(any());

    }

    @Test
    @DisplayName("given quantity less than 1")
    void test4(){
        int quantity = 0;
        ThrowableAssert.ThrowingCallable invocation = () -> addToCartService.addToCart(TEST_CUSTOMER_ID,TEST_PRODUCT_1.id(),quantity);

        assertThatIllegalArgumentException().isThrownBy(invocation);
        verify(cartRepository,never()).save(any());
    }

}
