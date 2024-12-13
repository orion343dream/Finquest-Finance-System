package finquest.finquest.controller;

import finquest.finquest.Util.Regex;
import finquest.finquest.model.Budget;
import finquest.finquest.model.tm.BudgetTM;
import finquest.finquest.repository.BudgetRepository;
import finquest.finquest.repository.ExpenseRepository;
import finquest.finquest.repository.IncomeRepository;
import finquest.finquest.repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.sql.Date;
import java.sql.SQLException;

public class BudgetController {

    @FXML
    private TableColumn<?, ?> amontCol;

    @FXML
    private TextField amountField;

    @FXML
    private TextField amountField1;

    @FXML
    private TableColumn<?, ?> endDateCol;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TableView<BudgetTM> budgetTable;

    @FXML
    private BorderPane mainPane;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private TextField nameField;

    @FXML
    private TableColumn<?, ?> startDateCol;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private Text totalBudget;

    private static String name = null;

    public void initialize(){
        setTotal();
        loadAllDate();
    }

    private void setTotal(){
        try {
            double budgetTotal = BudgetRepository.totalBudget();
            double incomeTotal = IncomeRepository.totalIncome();
            double expenseTotal= ExpenseRepository.totalExpense();
            double total = (budgetTotal + incomeTotal)- expenseTotal;
            totalBudget.setText(String.valueOf(total));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllDate() {
        ObservableList<BudgetTM> obList = FXCollections.observableArrayList();
        try {
            obList = BudgetRepository.getData();
           
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        budgetTable.setItems(obList);
        setCellValueFactory();
    }

    private void setCellValueFactory() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        amontCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
    }

    @FXML
    void addBudget(ActionEvent event) {
        if (isValied()){
            Budget budget = new Budget(UserRepository.user.getId(),nameField.getText(),Double.parseDouble(amountField.getText()), Date.valueOf(startDatePicker.getValue()),Date.valueOf(endDatePicker.getValue()));
            try {
                if (BudgetRepository.save(budget)){
                    new Alert(Alert.AlertType.CONFIRMATION,"Save successful").show();
                } else {
                    new Alert(Alert.AlertType.ERROR,"Save failed").show();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            initialize();
            setTotal();
        }
    }

    @FXML
    void DeleteBudget(ActionEvent event) {
        try {
            if (name != null){
                if (BudgetRepository.delete(name)){
                    new Alert(Alert.AlertType.CONFIRMATION,"delete successful").show();
                } else {
                    new Alert(Alert.AlertType.ERROR,"delete failed").show();
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.CONFIRMATION,e.getMessage()).show();
        }
        initialize();
        setTotal();
    }

    @FXML
    void nameAction(ActionEvent event) {
        try {
            if (BudgetRepository.edit(nameField.getText())){
                Budget budget = BudgetRepository.budget;
                amountField.setText(String.valueOf(budget.getAmount()));
                startDatePicker.setValue(budget.getStartDate().toLocalDate());
                endDatePicker.setValue(budget.getEndDate().toLocalDate());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.CONFIRMATION,e.getMessage()).show();
        }
    }

    @FXML
    void UpdateBduget(ActionEvent event) {
        try {
            Budget budget = getValues();
                updateValues(budget);
                if (BudgetRepository.update()){
                    new Alert(Alert.AlertType.CONFIRMATION,"Updated!").show();
                }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        initialize();
        setTotal();
    }

    private void updateValues(Budget budget) {
        if (!budget.getName().equals("")){
            BudgetRepository.budget.setName(budget.getName());
        }
        if (budget.getAmount() != 0){
            BudgetRepository.budget.setAmount(budget.getAmount());
        }
        if (!budget.getStartDate().equals("")) {
            BudgetRepository.budget.setStartDate(budget.getStartDate());
        }
        if (!budget.getEndDate().equals("")) {
            BudgetRepository.budget.setEndDate(budget.getEndDate());
        }
    }

    private Budget getValues() {
        return new Budget(UserRepository.user.getId(),nameField.getText(),Double.parseDouble(amountField.getText()),Date.valueOf(startDatePicker.getValue()),Date.valueOf(endDatePicker.getValue()));
    }


    @FXML
    void budgetTableAction(MouseEvent event) {
        BudgetTM selectedItem = budgetTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            name = selectedItem.getName();
        }
    }

    public boolean isValied() {
        if (!Regex.setTextColor(finquest.finquest.Util.TextField.AMOUNT,amountField)) return false;
        if (!Regex.setTextColor(finquest.finquest.Util.TextField.ID,nameField)) return false;
        return true;
    }

    @FXML
    void txtAmountAction(KeyEvent event) {
        Regex.setTextColor(finquest.finquest.Util.TextField.AMOUNT,amountField);
    }

    @FXML
    void txtNameAction(KeyEvent event) {
        Regex.setTextColor(finquest.finquest.Util.TextField.ID,nameField);
    }

}
