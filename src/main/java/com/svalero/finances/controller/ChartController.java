package com.svalero.finances.controller;

import com.svalero.finances.service.TransactionService;
import com.svalero.finances.domain.Transaction;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

public class ChartController {

    @FXML
    private PieChart pieChart;

    private final TransactionService service = new TransactionService();

    @FXML
    public void initialize() {
        double totalIncome = 0;
        double totalExpense = 0;

        for (Transaction t : service.getAllTransactions()) {
            if ("Income".equalsIgnoreCase(t.getType())) {
                totalIncome += t.getAmount();
            } else if ("Expense".equalsIgnoreCase(t.getType())) {
                totalExpense += t.getAmount();
            }
        }

        PieChart.Data incomeData =
                new PieChart.Data("Income (" + totalIncome + " €)", totalIncome);

        PieChart.Data expenseData =
                new PieChart.Data("Expense (" + totalExpense + " €)", totalExpense);

        pieChart.getData().setAll(incomeData, expenseData);
        pieChart.setLabelsVisible(true);     // muestra  las etiquetas
        pieChart.setLegendVisible(true);     // muestra la leyenda del gráfico
    }
}
