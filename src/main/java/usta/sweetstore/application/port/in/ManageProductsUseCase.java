package usta.sweetstore.application.port.in;

import usta.sweetstore.domain.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ManageProductsUseCase {

    Product create(
            String name,
            String description,
            BigDecimal price,
            int stock,
            int minimumStock,
            Long categoryId
    );

    Product update(
            Long id,
            String name,
            String description,
            BigDecimal price,
            int minimumStock,
            Long categoryId
    );

    List<Product> findAll();

    Product findById(Long id);
}