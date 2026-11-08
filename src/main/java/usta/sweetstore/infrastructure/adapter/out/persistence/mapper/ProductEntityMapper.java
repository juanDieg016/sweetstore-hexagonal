package usta.sweetstore.infrastructure.adapter.out.persistence.mapper;

import usta.sweetstore.domain.model.Product;
import usta.sweetstore.infrastructure.adapter.out.persistence.entity.ProductEntity;

public class ProductEntityMapper {

    public static Product toDomain(ProductEntity entity) {

        if (entity == null) {
            return null;
        }

        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStock(),
                entity.getMinimumStock(),
                CategoryEntityMapper.toDomain(entity.getCategory())
        );
    }

    public static ProductEntity toEntity(Product domain) {

        if (domain == null) {
            return null;
        }

        ProductEntity entity = new ProductEntity();

        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setPrice(domain.getPrice());
        entity.setStock(domain.getStock());
        entity.setMinimumStock(domain.getMinimumStock());
        entity.setCategory(
                CategoryEntityMapper.toEntity(domain.getCategory())
        );

        return entity;
    }
}