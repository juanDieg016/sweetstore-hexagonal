package usta.sweetstore.application.port.in;

import usta.sweetstore.domain.model.InventoryMovement;
import usta.sweetstore.domain.model.Product;

import java.util.List;

public interface ManageInventoryUseCase {

    InventoryMovement registerEntry(
            Long productId,
            int quantity,
            String description
    );

    InventoryMovement registerExit(
            Long productId,
            int quantity,
            String description
    );

    List<InventoryMovement> findAllMovements();

    List<Product> findLowStockProducts();
}