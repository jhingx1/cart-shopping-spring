package com.mitocode.shop.model.cart;

public class NotEnoughItemsInStockException extends Exception{

    private final int itemInStock;

    public NotEnoughItemsInStockException(String mensaje,int itemInStock){
        super(mensaje);
        this.itemInStock = itemInStock;
    }

    public int itemInStock(){
        return itemInStock;
    }

}
