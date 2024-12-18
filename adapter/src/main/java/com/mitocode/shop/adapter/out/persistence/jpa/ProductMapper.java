package com.mitocode.shop.adapter.out.persistence.jpa;

import com.mitocode.shop.model.money.Money;
import com.mitocode.shop.model.product.Product;
import com.mitocode.shop.model.product.ProductId;

import java.util.Currency;
import java.util.List;
import java.util.Optional;


//transformando un modelo negocio a db
public class ProductMapper {

    private ProductMapper(){}

    //transformando estidad de negocio
    static ProductJpaEntity toJpaEntity (Product product){
        ProductJpaEntity jpaEntity = new ProductJpaEntity();

        jpaEntity.setId(product.id().value());
        jpaEntity.setName(product.name());
        jpaEntity.setDescription(product.descripcion());
        jpaEntity.setPriceCurrency(product.price().currency().getCurrencyCode());
        jpaEntity.setPriceAmount(product.price().amount());
        jpaEntity.setItemsInStock(product.itemInStock());

        return jpaEntity;
    }

    //transformando estidad de db
    static Optional<Product> toModelEntityOptional (ProductJpaEntity jpaEntity){
        return Optional.ofNullable(jpaEntity).map(ProductMapper::toModelEntity);
    }

    //de una entidad de db a una de negocio.
    static Product toModelEntity (ProductJpaEntity jpaEntity){
        return new Product(
                new ProductId(jpaEntity.getId()),
                jpaEntity.getName(),
                jpaEntity.getDescription(),
                new Money(Currency.getInstance(jpaEntity.getPriceCurrency()), jpaEntity.getPriceAmount()),
                jpaEntity.getItemsInStock()
        );
    }

    static List<Product> toModelEntities(List<ProductJpaEntity> jpaEntities){
        return jpaEntities.stream().map(ProductMapper::toModelEntity).toList();
    }

}
