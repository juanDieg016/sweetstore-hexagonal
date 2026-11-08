package usta.sweetstore.infrastructure.adapter.out.persistence.adapter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import usta.sweetstore.application.port.out.InventoryMovementRepository;
import usta.sweetstore.domain.model.InventoryMovement;
import usta.sweetstore.infrastructure.adapter.out.persistence.entity.InventoryMovementEntity;
import usta.sweetstore.infrastructure.adapter.out.persistence.mapper.InventoryMovementEntityMapper;
import usta.sweetstore.infrastructure.adapter.out.persistence.repository.InventoryMovementJpaRepository;

import java.util.List;

@ApplicationScoped
public class JpaInventoryMovementRepository implements InventoryMovementRepository {

    private final InventoryMovementJpaRepository repository;

    @Inject
    public JpaInventoryMovementRepository(
            InventoryMovementJpaRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public InventoryMovement save(
            InventoryMovement movement
    ) {

        InventoryMovementEntity entity =
                InventoryMovementEntityMapper.toEntity(movement);

        if (entity.getId() == null) {
            repository.persist(entity);
        } else {
            entity = repository.getEntityManager().merge(entity);
        }

        return InventoryMovementEntityMapper.toDomain(entity);
    }

    @Override
    public List<InventoryMovement> findAll() {

        return repository.listAll()
                .stream()
                .map(InventoryMovementEntityMapper::toDomain)
                .toList();
    }
}