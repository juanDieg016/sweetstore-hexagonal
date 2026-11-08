package usta.sweetstore.infrastructure.adapter.out.persistence.adapter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import usta.sweetstore.application.port.out.CategoryRepository;
import usta.sweetstore.domain.model.Category;
import usta.sweetstore.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import usta.sweetstore.infrastructure.adapter.out.persistence.mapper.CategoryEntityMapper;
import usta.sweetstore.infrastructure.adapter.out.persistence.repository.CategoryJpaRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaCategoryRepository implements CategoryRepository {

    private final CategoryJpaRepository repository;

    @Inject
    public JpaCategoryRepository(CategoryJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Category save(Category category) {

        CategoryEntity entity =
                CategoryEntityMapper.toEntity(category);

        if (entity.getId() == null) {
            repository.persist(entity);
        } else {
            entity = repository.getEntityManager().merge(entity);
        }

        return CategoryEntityMapper.toDomain(entity);
    }

    @Override
    public Optional<Category> findById(Long id) {

        return repository.findByIdOptional(id)
                .map(CategoryEntityMapper::toDomain);
    }

    @Override
    public List<Category> findAll() {

        return repository.listAll()
                .stream()
                .map(CategoryEntityMapper::toDomain)
                .toList();
    }
}