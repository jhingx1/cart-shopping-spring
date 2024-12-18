package com.mitocode.shop.model.money;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import static com.mitocode.shop.model.money.TestMoneyFactory.euros;
import static com.mitocode.shop.model.money.TestMoneyFactory.usDollars;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;

class MoneyTest {
    private static final Currency EUR = Currency.getInstance("EUR");

    @Test
    void givenAmountWithAndInvalidScale_newMoney_throwsIllegalArgumentException(){
        BigDecimal amountWithScale3 = new BigDecimal(BigInteger.valueOf(12999),3);

        ThrowingCallable invocation = () -> new Money(EUR,amountWithScale3);
        assertThatIllegalArgumentException().isThrownBy(invocation);

        //assertThrows(IllegalArgumentException.class,()->new Money(EUR,amountWithScale3));
    }

    @Test
    void givenEuroAmount_addADollarAmount_throwsIllegalArgumentException(){
        Money euros = euros(11,99);
        Money dollars = usDollars(11,99);

        ThrowingCallable invocation = () -> euros.add(dollars);
        assertThatIllegalArgumentException().isThrownBy(invocation);
    }

}
