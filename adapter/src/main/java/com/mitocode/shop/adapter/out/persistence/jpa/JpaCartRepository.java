package com.mitocode.shop.adapter.out.persistence.jpa;

import com.mitocode.shop.application.port.out.persistence.CartRepository;
import com.mitocode.shop.model.cart.Cart;
import com.mitocode.shop.model.customer.CustomerId;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ConditionalOnProperty(name = "persistence", havingValue = "mysql")
@Repository
@RequiredArgsConstructor
public class JpaCartRepository implements CartRepository{

    private final JpaCartClassSpringDataRepository springDataRepository;

    @Override
    @Transactional
    public void save(Cart cart){
        springDataRepository.save(CartMapper.toJpaEntity(cart));
    }

    @Override
    @Transactional
    public Optional<Cart> findByCustomerId(CustomerId customerId){
        return springDataRepository.findById(customerId.value())
                .flatMap(cartJpaEntity -> CartMapper.toModelEntityOptional(cartJpaEntity));
    }

    @Override
    @Transactional
    public void deleteByCustomerId(CustomerId customerId){
        springDataRepository.findById(customerId.value())
                .ifPresent(customer -> springDataRepository.deleteById(customerId.value()));
    }

}
