package usta.sweetstore.infrastructure.adapter.out.persistence.adapter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import usta.sweetstore.application.port.out.ProductRepository;
import usta.sweetstore.domain.model.Product;
import usta.sweetstore.infrastructure.adapter.out.persistence.entity.ProductEntity;
import usta.sweetstore.infrastructure.adapter.out.persistence.mapper.ProductEntityMapper;
import usta.sweetstore.infrastructure.adapter.out.persistence.repository.ProductJpaRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaProductRepository implements ProductRepository {

    private final ProductJpaRepository repository;

    @Inject
    public JpaProductRepository(ProductJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Product save(Product product) {

        ProductEntity entity =
                ProductEntityMapper.toEntity(product);

        if (entity.getId() == null) {
            repository.persist(entity);
        } else {
            entity = repository.getEntityManager().merge(entity);
        }

        return ProductEntityMapper.toDomain(entity);
    }

    @Override
    public Optional<Product> findById(Long id) {

        return repository.findByIdOptional(id)
                .map(ProductEntityMapper::toDomain);
    }

    @Override
    public List<Product> findAll() {

        return repository.listAll()
                .stream()
                .map(ProductEntityMapper::toDomain)
                .toList();
    }
}