package com.mitocode.shop.model.cart;

import com.mitocode.shop.model.money.Money;
import com.mitocode.shop.model.product.Product;
import com.mitocode.shop.model.product.ProductId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class CartLineItem {

    private final Product product;
    private int quantity;

    //incrementar items
    public void increaseQuantityBy(int augend,int itemInStock) throws NotEnoughItemsInStockException{
            if(augend < 1){
                throw new IllegalArgumentException("You must add at least one item");
            }
            int newQuantity = quantity + augend;
            if(itemInStock < newQuantity){
                throw new NotEnoughItemsInStockException("Product %s has less items ins stock (%d) than the request total quantity (%d)"
                        .formatted(product.id(),product.itemInStock(), newQuantity),product.itemInStock());
            }
            this.quantity = newQuantity;
    }

    public Money subTotal(){
        return product.price().multiply(quantity);
    }

}
class test{
    public static void main(String[] args) {
        Currency usd1 = Currency.getInstance("USD");
        BigDecimal amount1 = new BigDecimal("10.00");
        Money money1 = new Money(usd1, amount1);
        Product product = new Product(
                ProductId.randomProductId(),
                "any name",
                "any description",
                money1,
                10
                );
        System.out.println(product);
        CartLineItem cartLineItem = new CartLineItem(product);
        System.out.println(cartLineItem);
    }
}