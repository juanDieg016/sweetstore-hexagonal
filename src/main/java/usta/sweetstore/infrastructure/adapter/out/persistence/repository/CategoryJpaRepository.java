package usta.sweetstore.infrastructure.adapter.out.persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import usta.sweetstore.infrastructure.adapter.out.persistence.entity.CategoryEntity;

@ApplicationScoped
public class CategoryJpaRepository implements PanacheRepository<CategoryEntity> {
}