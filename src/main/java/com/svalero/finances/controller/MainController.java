package com.svalero.finances.controller;

import javax.imageio.ImageIO;

import com.itextpdf.text.*;
import com.svalero.finances.service.CategoryService;
import javafx.scene.Parent;
import javafx.stage.FileChooser;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.svalero.finances.domain.Transaction;
import com.svalero.finances.service.TransactionService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.springframework.stereotype.Component;


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;


@Component
public class MainController {
    @FXML
    private Label label;
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, String> colType;
    @FXML private TableColumn<Transaction, String> colCategory;
    @FXML private TableColumn<Transaction, Double> colAmount;
    @FXML private TableColumn<Transaction, String> colDate;
    @FXML private TableColumn<Transaction, String> colDescription;
    @FXML private Label lblBalance;
    @FXML private ComboBox<String> comboType;
    @FXML private ComboBox<String> comboCategory;
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;

    private List<Transaction> allTransactions;

    private final TransactionService service = new TransactionService();
    private final CategoryService categoryService = new CategoryService();

    @FXML
    public void initialize() {

        comboType.getSelectionModel().select("All");


        comboCategory.getItems().add("All"); // opción por defecto!!!!
        comboCategory.getItems().addAll(categoryService.getAllCategories());
        comboCategory.getSelectionModel().select("All");


        // Configure table columns
        colType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        colCategory.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
        colAmount.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getAmount()).asObject());
        colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate().toString()));
        colDescription.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));

        loadTransactions();
        // Resaltar filas según tipo
        transactionTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Transaction t, boolean empty) {
                super.updateItem(t, empty);

                if (t == null || empty) {
                    setStyle("");
                    setTooltip(null);
                } else {
                    if ("Income".equalsIgnoreCase(t.getType())) {
                        setStyle("-fx-background-color: #e0f8e0;");
                    } else if ("Expense".equalsIgnoreCase(t.getType())) {
                        setStyle("-fx-background-color: #fde0e0;");
                    } else {
                        setStyle("");
                    }

                    Tooltip tooltip = new Tooltip(t.getDescription());
                    setTooltip(tooltip);
                }
            }
        });

    }



    private void loadTransactions() {
        allTransactions = service.getAllTransactions();
       // List<Transaction> transactions = service.getAllTransactions();
        transactionTable.getItems().setAll(allTransactions);
        lblBalance.setText(String.format("%.2f €", service.calculateBalance()));

        checkExpenseWarning();
    }
    @FXML
    public void onApplyFilter() {
        String selectedType = comboType.getSelectionModel().getSelectedItem();
        String selectedCategory = comboCategory.getSelectionModel().getSelectedItem();
        LocalDate fromDate = dateFrom.getValue();
        LocalDate toDate = dateTo.getValue();

        List<Transaction> filtered = allTransactions.stream()
                .filter(t -> selectedType.equals("All") || t.getType().equals(selectedType))
                .filter(t -> selectedCategory.equals("All") || t.getCategory().equals(selectedCategory))
                .filter(t -> (fromDate == null || !t.getDate().isBefore(fromDate)))
                .filter(t -> (toDate == null || !t.getDate().isAfter(toDate)))
                .toList();

        transactionTable.getItems().setAll(filtered);
    }
    @FXML
    public void onResetFilter() {
        comboType.getSelectionModel().select("All");
        comboCategory.getSelectionModel().select("All");
        dateFrom.setValue(null);
        dateTo.setValue(null);

        transactionTable.getItems().setAll(allTransactions);
    }

    @FXML
    public void onAddTransaction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/add-transaction.fxml"));
            Scene scene = new Scene(loader.load());

            AddTransactionController controller = loader.getController();
            controller.loadCategories();

            Stage stage = new Stage();
            stage.setTitle("Add Transaction");
            stage.setScene(scene);
            stage.showAndWait();

            loadTransactions(); // refrescar
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void onEditTransaction() {
        Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a transaction to edit.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/edit-transaction.fxml"));

            Scene scene = new Scene(loader.load());
            EditTransactionController controller = loader.getController();
            controller.setTransaction(selected);

            Stage stage = new Stage();
            stage.setTitle("Edit Transaction");
            stage.setScene(scene);
            stage.showAndWait();

            loadTransactions(); // recargar tabla después de editar
            showAlert("Transaction updated successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void onDeleteTransaction() {
        Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a transaction to delete.");
            return;
        }

        service.deleteTransaction(selected.getId());
        loadTransactions();
        showAlert("Transaction deleted successfully.");
    }

    @FXML
    public void onShowChart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/chart.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Income vs Expense");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void checkExpenseWarning() {
        double totalIncome = 0;
        double totalExpense = 0;

        for (Transaction t : service.getAllTransactions()) {
            if (t.getType().equalsIgnoreCase("Income")) {
                totalIncome += t.getAmount();
            } else if (t.getType().equalsIgnoreCase("Expense")) {
                totalExpense += t.getAmount();
            }
        }

        if (totalIncome == 0) {
            return; // evitar la división por cero
        }

        double percent = (totalExpense / totalIncome) * 100;

        if (percent >= 60) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Spending Alert");
            alert.setHeaderText("¡Warning!");
            alert.setContentText(
                    "You have spent " + String.format("%.1f", percent) + "% of your total income."
            );
            alert.showAndWait();
        }
    }

    @FXML
    public void onExportCSV() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export transactions to CSV");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));

        File file = chooser.showSaveDialog(null);

        if (file != null) {
            try {
                exportToCSV(file);
                new Alert(Alert.AlertType.INFORMATION, "Data exported successfully!").showAndWait();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error exporting: " + e.getMessage()).showAndWait();
            }
        }
    }


    private void exportToCSV(File file) throws Exception {
        List<Transaction> transactions = service.getAllTransactions();

        FileWriter writer = new FileWriter(file);

        // Header
        writer.write("ID,Type,Category,Description,Amount,Date\n");

        for (Transaction t : transactions) {
            writer.write(
                    t.getId() + "," +
                            t.getType() + "," +
                            t.getCategory() + "," +
                            "\"" + t.getDescription() + "\"," +
                            t.getAmount() + "," +
                            t.getDate() + "\n"
            );
        }

        writer.close();
    }

    @FXML
    public void onExportPDF() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
            File file = fileChooser.showSaveDialog(transactionTable.getScene().getWindow());

            if (file == null) return;

            // Crear documento PDF
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Título
            Paragraph title = new Paragraph("Transaction Report",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // espacio

            // Tabla
            PdfPTable pdfTable = new PdfPTable(5); // 5 columnas
            pdfTable.setWidthPercentage(100);

            // Encabezados
            pdfTable.addCell("Type");
            pdfTable.addCell("Category");
            pdfTable.addCell("Amount (€)");
            pdfTable.addCell("Date");
            pdfTable.addCell("Description");

            // Filas de la tabla
            for (Transaction t : service.getAllTransactions()) {
                pdfTable.addCell(t.getType());
                pdfTable.addCell(t.getCategory());
                pdfTable.addCell(String.format("%.2f", t.getAmount()));
                pdfTable.addCell(t.getDate().toString());
                pdfTable.addCell(t.getDescription());
            }

            document.add(pdfTable);
            document.add(new Paragraph(" "));

            // Totales
            double totalIncome = service.getAllTransactions().stream()
                    .filter(t -> t.getType().equalsIgnoreCase("Income"))
                    .mapToDouble(Transaction::getAmount).sum();

            double totalExpense = service.getAllTransactions().stream()
                    .filter(t -> t.getType().equalsIgnoreCase("Expense"))
                    .mapToDouble(Transaction::getAmount).sum();

            Paragraph totals = new Paragraph(
                    String.format("Total Income: %.2f €\nTotal Expense: %.2f €\nBalance: %.2f €",
                            totalIncome, totalExpense, totalIncome - totalExpense),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)
            );
            totals.setAlignment(Element.ALIGN_RIGHT);
            document.add(totals);

            document.close();

            new Alert(Alert.AlertType.INFORMATION, "PDF exported successfully!").showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error exporting PDF: " + e.getMessage()).showAndWait();
        }
    }
    @FXML
    public void onManageCategories() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/category-manager.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Manage Categories");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onClose() {
        Stage stage = (Stage) transactionTable.getScene().getWindow();
        stage.close();
    }


    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("FinanTrack");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
