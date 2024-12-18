package com.mitocode.shop.adapter.out.persistence.jpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCartClassSpringDataRepository extends JpaRepository<CartJpaEntity,Integer>{
}
