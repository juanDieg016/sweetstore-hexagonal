package usta.sweetstore.application.port.out;

import usta.sweetstore.domain.model.InventoryMovement;

import java.util.List;

public interface InventoryMovementRepository {

    InventoryMovement save(
            InventoryMovement movement
    );

    List<InventoryMovement> findAll();
}