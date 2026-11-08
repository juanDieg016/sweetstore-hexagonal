package usta.sweetstore.infrastructure.adapter.out.persistence.mapper;

import usta.sweetstore.domain.model.InventoryMovement;
import usta.sweetstore.infrastructure.adapter.out.persistence.entity.InventoryMovementEntity;

public class InventoryMovementEntityMapper {

    public static InventoryMovement toDomain(
            InventoryMovementEntity entity
    ) {

        if (entity == null) {
            return null;
        }

        return new InventoryMovement(
                entity.getId(),
                ProductEntityMapper.toDomain(entity.getProduct()),
                entity.getType(),
                entity.getQuantity(),
                entity.getDate(),
                entity.getDescription()
        );
    }

    public static InventoryMovementEntity toEntity(
            InventoryMovement domain
    ) {

        if (domain == null) {
            return null;
        }

        InventoryMovementEntity entity =
                new InventoryMovementEntity();

        entity.setId(domain.getId());
        entity.setProduct(
                ProductEntityMapper.toEntity(domain.getProduct())
        );
        entity.setType(domain.getType());
        entity.setQuantity(domain.getQuantity());
        entity.setDate(domain.getDate());
        entity.setDescription(domain.getDescription());

        return entity;
    }
}