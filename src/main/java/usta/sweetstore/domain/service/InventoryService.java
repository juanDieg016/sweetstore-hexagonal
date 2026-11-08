package usta.sweetstore.domain.service;

import usta.sweetstore.domain.exception.BusinessRuleException;
import usta.sweetstore.domain.model.Product;

import java.math.BigDecimal;

public class InventoryService {

    public void validateProduct(Product product) {

        if (product == null) {
            throw new BusinessRuleException(
                    "El producto es obligatorio."
            );
        }

        if (product.getName() == null
                || product.getName().isBlank()) {

            throw new BusinessRuleException(
                    "El nombre del producto es obligatorio."
            );
        }

        if (product.getPrice() == null
                || product.getPrice()
                .compareTo(BigDecimal.ZERO) <= 0) {

            throw new BusinessRuleException(
                    "El precio debe ser mayor a cero."
            );
        }

        if (product.getStock() < 0) {

            throw new BusinessRuleException(
                    "El stock no puede ser negativo."
            );
        }

        if (product.getMinimumStock() < 0) {

            throw new BusinessRuleException(
                    "El stock mínimo no puede ser negativo."
            );
        }

        if (product.getCategory() == null) {

            throw new BusinessRuleException(
                    "El producto debe tener una categoría."
            );
        }
    }


    public void addStock(
            Product product,
            int quantity
    ) {

        validateQuantity(quantity);

        product.setStock(
                product.getStock() + quantity
        );
    }


    public void removeStock(
            Product product,
            int quantity
    ) {

        validateQuantity(quantity);

        if (product.getStock() < quantity) {

            throw new BusinessRuleException(
                    "No hay suficiente inventario disponible."
            );
        }

        product.setStock(
                product.getStock() - quantity
        );
    }


    private void validateQuantity(int quantity) {

        if (quantity <= 0) {

            throw new BusinessRuleException(
                    "La cantidad debe ser mayor a cero."
            );
        }
    }
}