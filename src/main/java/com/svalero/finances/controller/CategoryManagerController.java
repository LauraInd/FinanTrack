package com.svalero.finances.controller;

import com.svalero.finances.service.CategoryService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class CategoryManagerController {

    @FXML
    private ListView<String> listCategories;  // Usamos String, no Category

    @FXML
    private TextField txtNewCategory;

    private final CategoryService categoryService = new CategoryService();

    @FXML
    public void initialize() {
        refreshCategories();
    }

    private void refreshCategories() {
        // Obtener nombres de categorías y establecerlos en el ListView
        listCategories.getItems().setAll(categoryService.getAllCategories());
    }

    @FXML
    public void onAddCategory() {
        String name = txtNewCategory.getText().trim();

        if (name.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Category name cannot be empty.").show();
            return;
        }

        try {
            categoryService.addCategory(name);
            txtNewCategory.clear();
            refreshCategories();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    public void onDeleteCategory() {
        String selected = listCategories.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a category to delete.").show();
            return;
        }

        try {
            // Borrar por nombre de categoría
            categoryService.deleteCategory(selected);
            refreshCategories();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }
}
