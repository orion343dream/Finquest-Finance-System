package finquest.finquest.controller;

import finquest.finquest.Util.Regex;
import finquest.finquest.model.Expense;
import finquest.finquest.model.tm.BudgetTM;
import finquest.finquest.model.tm.ExpenseTM;
import finquest.finquest.repository.ExpenseRepository;
import finquest.finquest.repository.IncomeRepository;
import finquest.finquest.repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.sql.Date;
import java.sql.SQLException;

public class ExpenseController {

    @FXML
    private TextField amountFeild;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private Label lblTotalExpense;

    @FXML
    private TableColumn<?, ?> colCategory;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDes;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TextField desFeild;

    @FXML
    private TableView<ExpenseTM> expenseTable;

    @FXML
    private BorderPane mainPane;

    @FXML
    private ChoiceBox<String> categorychoisebox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private TextField txtExpenseId;

    @FXML
    public void initialize() {

        categorychoisebox.setItems(FXCollections.observableArrayList(
                "Food", "Transportation", "Housing", "Utilities", "Healthcare",
                "Insurance", "Entertainment", "Education", "Debt", "Other"
        ));


        categorychoisebox.setOnAction(event -> {
            String selectedCategory = categorychoisebox.getValue();

            System.out.println("Selected category: " + selectedCategory);
        });

        loadAllDate();
        getTotalExpense();
    }
    private void getTotalExpense() {
        try {
            double total = ExpenseRepository.getTotal();
            lblTotalExpense.setText("Rs. " + total);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void loadAllDate() {
        ObservableList<ExpenseTM> obList = FXCollections.observableArrayList();
        try {
            obList = ExpenseRepository.getData();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        expenseTable.setItems(obList);
        setCellValueFactory();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDes.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
    }

    @FXML
    void addExpense(ActionEvent event) {
        if (isValied()) {
            Expense expense = new Expense(txtExpenseId.getText(), UserRepository.user.getId(), Double.parseDouble(amountFeild.getText()), desFeild.getText(), Date.valueOf(startDatePicker.getValue()), categorychoisebox.getValue());
            try {
                if (ExpenseRepository.save(expense)) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Save successful").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Save failed").show();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            loadAllDate();
            getTotalExpense();
        }
    }
    @FXML
    void deleteExpense(ActionEvent event) {
        try {
            if (ExpenseRepository.delete(txtExpenseId.getText())) {
                new Alert(Alert.AlertType.CONFIRMATION, "delete successful").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "delete failed").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.CONFIRMATION, e.getMessage()).show();
        }
        loadAllDate();
        getTotalExpense();
    }

    @FXML
    void idAction(ActionEvent event) {
        try {
            if (ExpenseRepository.edit(txtExpenseId.getText())) {
                Expense expense = ExpenseRepository.expense;
                amountFeild.setText(String.valueOf(expense.getAmount()));
                desFeild.setText(String.valueOf(expense.getDescription()));
                startDatePicker.setValue(expense.getDate().toLocalDate());
                categorychoisebox.setValue(expense.getCategory());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.CONFIRMATION, e.getMessage()).show();
        }
    }

    @FXML
    void updateExpense(ActionEvent event) {
        try {
            Expense expense = getValues();
            updateValues(expense);
            if (ExpenseRepository.update()) {
                new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllDate();
        getTotalExpense();
    }

    private void updateValues(Expense expense) {
        if (!expense.getDescription().equals("")) {
            ExpenseRepository.expense.setDescription(expense.getDescription());
        }
        if (expense.getAmount() != 0) {
            ExpenseRepository.expense.setAmount(expense.getAmount());
        }
        if (!expense.getDate().equals("")) {
            ExpenseRepository.expense.setDate(expense.getDate());
        }
        if (!expense.getCategory().equals("")) {
            ExpenseRepository.expense.setCategory(expense.getCategory());
        }
    }

    private Expense getValues() {
        return new Expense(txtExpenseId.getText(), UserRepository.user.getId(), Double.parseDouble(amountFeild.getText()), desFeild.getText(), Date.valueOf(startDatePicker.getValue()), categorychoisebox.getValue());
    }
    @FXML
    void expenseTableAction(MouseEvent event) {
        ExpenseTM selectedItem = expenseTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            txtExpenseId.setText(selectedItem.getId());
        }
    }

    public boolean isValied() {
        if (!Regex.setTextColor(finquest.finquest.Util.TextField.ID,txtExpenseId)) return false;
        if (!Regex.setTextColor(finquest.finquest.Util.TextField.AMOUNT,amountFeild)) return false;
        if (!Regex.setTextColor(finquest.finquest.Util.TextField.ID,desFeild)) return false;
        return true;
    }

    @FXML
    void txtAmountAction(KeyEvent event) {
        Regex.setTextColor(finquest.finquest.Util.TextField.AMOUNT,amountFeild);
    }

    @FXML
    void txtIdAction(KeyEvent event) {
        Regex.setTextColor(finquest.finquest.Util.TextField.ID,txtExpenseId);
    }

    @FXML
    void txtDesAction(KeyEvent event) {
        Regex.setTextColor(finquest.finquest.Util.TextField.ID,desFeild);
    }

}

