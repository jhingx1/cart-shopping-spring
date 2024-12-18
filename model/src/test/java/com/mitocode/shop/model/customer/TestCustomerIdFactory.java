package com.mitocode.shop.model.customer;

import java.util.UUID;

public class TestCustomerIdFactory {
    public static CustomerId randomCustomerId() {
        //Integer randomCustomerId = UUID.randomUUID();
        int randomCustomerId = (int)(Math.random()*1000+1);
        return new CustomerId(randomCustomerId);
    }

    public static void main(String[] args) {
        System.out.println(randomCustomerId());
    }
}
