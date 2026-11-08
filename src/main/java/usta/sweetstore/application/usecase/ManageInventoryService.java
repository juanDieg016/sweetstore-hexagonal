package usta.sweetstore.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import usta.sweetstore.application.port.in.ManageInventoryUseCase;
import usta.sweetstore.application.port.out.InventoryMovementRepository;
import usta.sweetstore.application.port.out.ProductRepository;
import usta.sweetstore.domain.exception.BusinessRuleException;
import usta.sweetstore.domain.model.InventoryMovement;
import usta.sweetstore.domain.model.MovementType;
import usta.sweetstore.domain.model.Product;
import usta.sweetstore.domain.service.InventoryService;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ManageInventoryService implements ManageInventoryUseCase {

    private final ProductRepository productRepository;
    private final InventoryMovementRepository movementRepository;
    private final InventoryService inventoryService;

    @Inject
    public ManageInventoryService(
            ProductRepository productRepository,
            InventoryMovementRepository movementRepository
    ) {
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
        this.inventoryService = new InventoryService();
    }

    @Override
    public InventoryMovement registerEntry(
            Long productId,
            int quantity,
            String description
    ) {

        Product product = findProduct(productId);

        inventoryService.addStock(product, quantity);

        productRepository.save(product);

        InventoryMovement movement =
                new InventoryMovement(
                        null,
                        product,
                        MovementType.ENTRY,
                        quantity,
                        LocalDateTime.now(),
                        description
                );

        return movementRepository.save(movement);
    }

    @Override
    public InventoryMovement registerExit(
            Long productId,
            int quantity,
            String description
    ) {

        Product product = findProduct(productId);

        inventoryService.removeStock(product, quantity);

        productRepository.save(product);

        InventoryMovement movement =
                new InventoryMovement(
                        null,
                        product,
                        MovementType.EXIT,
                        quantity,
                        LocalDateTime.now(),
                        description
                );

        return movementRepository.save(movement);
    }

    @Override
    public List<InventoryMovement> findAllMovements() {
        return movementRepository.findAll();
    }

    @Override
    public List<Product> findLowStockProducts() {

        return productRepository.findAll()
                .stream()
                .filter(Product::hasLowStock)
                .toList();
    }

    private Product findProduct(Long productId) {

        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new BusinessRuleException(
                                "Producto no encontrado."
                        )
                );
    }
}