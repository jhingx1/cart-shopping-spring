package com.mitocode.shop.model.customer;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
public class CustumerIdTest {

    @ParameterizedTest
    @ValueSource(ints = {-100,-1, 0})
    void givenAValueLessThan1_newCustomerId_throwsException(int value){
        ThrowableAssert.ThrowingCallable invocation = () ->new CustomerId(value);
        assertThatIllegalArgumentException().isThrownBy(invocation);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,8_765,2_000_000_000})
    void givenAValueGreaterThanOrEqualTo1_newCustomerId_succeds(int value){

        CustomerId customerId = new CustomerId(value);
        assertThat(customerId.value()).isEqualTo(value);

    }

}
