package usta.sweetstore.infrastructure.adapter.out.persistence.mapper;

import usta.sweetstore.domain.model.Category;
import usta.sweetstore.infrastructure.adapter.out.persistence.entity.CategoryEntity;

public class CategoryEntityMapper {

    public static Category toDomain(CategoryEntity entity) {

        if (entity == null) {
            return null;
        }

        return new Category(
                entity.getId(),
                entity.getName(),
                entity.getDescription()
        );
    }

    public static CategoryEntity toEntity(Category domain) {

        if (domain == null) {
            return null;
        }

        CategoryEntity entity = new CategoryEntity();

        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());

        return entity;
    }
}