package com.mitocode.shop.model.money;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public record Money(Currency currency, BigDecimal amount) {
    public Money{
        Objects.requireNonNull(currency, "'currency' must not be null");
        Objects.requireNonNull(currency, "'amount' must not be null");
        if(amount.scale()>currency.getDefaultFractionDigits()){
            throw new IllegalArgumentException("Scale of amount %s is greater than the number of " +
                    "fraction digits used with the currency $s".formatted(amount,currency));
        }
    }

    //sumar un int mas un decimal
    public static Money of(Currency currency,int mayor,int minor){
        int scale = currency.getDefaultFractionDigits();
        return new Money(currency,BigDecimal.valueOf(mayor).add(BigDecimal.valueOf(minor,scale)));
    }

    //multiplicando el monto por la cantidad
    public Money multiply(int multiplicand){
        return new Money(currency,amount.multiply(BigDecimal.valueOf(multiplicand)));
    }
    //sumar dos montos, validando que sean de la misma moneda
    public Money add(Money augend){
        if(!this.currency.equals(augend.currency)){
            throw new IllegalArgumentException("Currency %s of augend does not match this money currency $s".formatted(augend.currency,this.currency));
        }
        return new Money(currency,amount.add(augend.amount()));
    }

//    public static void main(String[] args) {
//        Currency usd1 = Currency.getInstance("USD");
//        BigDecimal amount1 = new BigDecimal("10.00");
//        Money money1 = new Money(usd1, amount1);
//        System.out.println("Moneda1"+money1);
//        System.out.println("Moneda1*10"+money1.multiply(10));
//        System.out.println("Moneda1+OF"+of(usd1,10,150));
//
//        Currency usd2 = Currency.getInstance("USD");
//        BigDecimal amount2 = new BigDecimal("20.00");
//        Money money2 = new Money(usd2, amount2);
//        System.out.println("Moneda2"+money2);
//        System.out.println("Moneda2+Moneda1"+money1.add(money2));
//    }
}

