package usta.sweetstore.application.port.in;

import usta.sweetstore.domain.model.Category;

import java.util.List;

public interface ManageCategoriesUseCase {

    Category create(
            String name,
            String description
    );

    List<Category> findAll();

    Category findById(Long id);
}