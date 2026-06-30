package org.lotzapp.component;

import org.lotzapp.regiologapi.model.Category;
import org.lotzapp.regiologapi.model.CategoryStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryComponent {
    private static final List<Category> CATEGORIES = new ArrayList<>();

    static  {
        CATEGORIES.add(new Category(UUID.randomUUID(), "Category #1 (AT)", CategoryStatus.ACTIVE, 1, OffsetDateTime.now(), OffsetDateTime.now()));
        CATEGORIES.add(new Category(UUID.randomUUID(), "Category #1 (DE)", CategoryStatus.ACTIVE, 2, OffsetDateTime.now(), OffsetDateTime.now()));
    }

    public static List<Category> getCategories() {
        return new ArrayList<>(CATEGORIES);
    }

    public static ResponseEntity<List<Category>> getCategoriesResponse() {
        return ResponseEntity.ok(CATEGORIES);
    }
}
