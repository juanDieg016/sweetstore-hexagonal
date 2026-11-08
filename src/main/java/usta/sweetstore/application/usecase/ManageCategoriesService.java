package usta.sweetstore.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import usta.sweetstore.application.port.in.ManageCategoriesUseCase;
import usta.sweetstore.application.port.out.CategoryRepository;
import usta.sweetstore.domain.exception.BusinessRuleException;
import usta.sweetstore.domain.model.Category;

import java.util.List;

@ApplicationScoped
public class ManageCategoriesService implements ManageCategoriesUseCase {

    private final CategoryRepository categoryRepository;

    @Inject
    public ManageCategoriesService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(String name, String description) {

        if (name == null || name.isBlank()) {
            throw new BusinessRuleException(
                    "El nombre de la categoría es obligatorio."
            );
        }

        Category category = new Category(
                null,
                name.trim(),
                description
        );

        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {

        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessRuleException(
                                "Categoría no encontrada."
                        )
                );
    }
}