package com.mitocode.shop.adapter.in.rest.cart;

import com.mitocode.shop.application.port.in.cart.EmptyCartUseCase;
import com.mitocode.shop.model.customer.CustomerId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.mitocode.shop.adapter.in.rest.common.CustomerIdParser.parseCustomerId;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class EmptyCartController {

    private final EmptyCartUseCase emptyCartUseCase;

    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCart(@PathVariable("customerId") String customerIdString){
        CustomerId customerId = parseCustomerId(customerIdString);
        emptyCartUseCase.emptyCart(customerId);
    }

}
