package usta.sweetstore.infrastructure.adapter.out.persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import usta.sweetstore.infrastructure.adapter.out.persistence.entity.ProductEntity;

@ApplicationScoped
public class ProductJpaRepository implements PanacheRepository<ProductEntity> {
}