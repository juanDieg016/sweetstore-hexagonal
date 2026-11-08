package usta.sweetstore.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import usta.sweetstore.application.port.in.ManageProductsUseCase;
import usta.sweetstore.application.port.out.CategoryRepository;
import usta.sweetstore.application.port.out.ProductRepository;
import usta.sweetstore.domain.exception.BusinessRuleException;
import usta.sweetstore.domain.model.Category;
import usta.sweetstore.domain.model.Product;
import usta.sweetstore.domain.service.InventoryService;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class ManageProductsService implements ManageProductsUseCase {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryService inventoryService;

    @Inject
    public ManageProductsService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.inventoryService = new InventoryService();
    }

    @Override
    public Product create(
            String name,
            String description,
            BigDecimal price,
            int stock,
            int minimumStock,
            Long categoryId
    ) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new BusinessRuleException(
                                "Categoría no encontrada."
                        )
                );

        Product product = new Product(
                null,
                name,
                description,
                price,
                stock,
                minimumStock,
                category
        );

        inventoryService.validateProduct(product);

        return productRepository.save(product);
    }

    @Override
    public Product update(
            Long id,
            String name,
            String description,
            BigDecimal price,
            int minimumStock,
            Long categoryId
    ) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessRuleException(
                                "Producto no encontrado."
                        )
                );

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new BusinessRuleException(
                                "Categoría no encontrada."
                        )
                );

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setMinimumStock(minimumStock);
        product.setCategory(category);

        inventoryService.validateProduct(product);

        return productRepository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessRuleException(
                                "Producto no encontrado."
                        )
                );
    }
}