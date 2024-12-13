package finquest.finquest.controller;

import finquest.finquest.Util.Regex;
import finquest.finquest.model.FinancialGoal;
import finquest.finquest.model.FinantialAccount;
import finquest.finquest.model.tm.FinancialGoalTM;
import finquest.finquest.model.tm.IncomeTM;
import finquest.finquest.repository.FinancialAccountRepository;
import finquest.finquest.repository.FinancialGoalRepository;
import finquest.finquest.repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.sql.Date;
import java.sql.SQLException;

public class FinancialGoalController {

    @FXML
    private TableColumn<?, ?> colamount;

    @FXML
    private TableColumn<?, ?> colgoalname;

    @FXML
    private TableColumn<?, ?> coldate;

    @FXML
    private TableColumn<?, ?> colcategory;

    @FXML
    private TableView<FinancialGoalTM> FgoalTable;

    @FXML
    private TextField GnameField;

    @FXML
    private TextField targetAmountField;

    @FXML
    private DatePicker targetDatePicker;

    @FXML
    private ChoiceBox<String> targetgoalchoisebox;

    @FXML
    public void initialize() {
        targetgoalchoisebox.setItems(FXCollections.observableArrayList(
                "Income", "Expense", "Budget"
        ));


        targetgoalchoisebox.setOnAction(event -> {
            String selectedCategory = targetgoalchoisebox.getValue();
            System.out.println("Selected category: " + selectedCategory);
        });

        loadAllData();
    }

    private void loadAllData() {
        ObservableList<FinancialGoalTM> obList = FXCollections.observableArrayList();
        try {
            obList = FinancialGoalRepository.getData();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        FgoalTable.setItems(obList);
        setCellValueFactory();
    }

    private void setCellValueFactory() {
        colgoalname.setCellValueFactory(new PropertyValueFactory<>("name"));
        colamount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colcategory.setCellValueFactory(new PropertyValueFactory<>("achieved"));
    }

    @FXML
    void addFinancialGoal(ActionEvent event) {
        if (isValied()) {
            FinancialGoal financialGoal = new FinancialGoal(GnameField.getText(), Double.parseDouble(targetAmountField.getText()), Date.valueOf(targetDatePicker.getValue()), targetgoalchoisebox.getValue());
            try {
                if (FinancialGoalRepository.save(financialGoal)) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Save successful").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Save failed").show();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            loadAllData();
        }
    }

    @FXML
    void onSearchAction(ActionEvent event) {
        try {
            if (FinancialGoalRepository.edit(GnameField.getText())){
                targetAmountField.setText(String.valueOf(FinancialGoalRepository.financialGoal.getAmount()));
                targetDatePicker.setValue(FinancialGoalRepository.financialGoal.getDate().toLocalDate());
                targetgoalchoisebox.setValue(FinancialGoalRepository.financialGoal.getCategory());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void deleteFinancialGoal(ActionEvent event) {
        try {
            if (FinancialGoalRepository.delete(GnameField.getText())) {
                new Alert(Alert.AlertType.CONFIRMATION, "delete successful").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "delete failed").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.CONFIRMATION, e.getMessage()).show();
        }
        loadAllData();
    }

    @FXML
    void UpdateFinancialGoal(ActionEvent event) {
        try {
            if (FinancialGoalRepository.edit(GnameField.getText())) {
                FinancialGoal financialGoal = getValues();
                updateValues(financialGoal);
                if (FinancialGoalRepository.update(financialGoal)) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Update failed").show();
                }
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllData();
    }

    private void updateValues(FinancialGoal financialGoal) {
        FinancialGoal existingGoal = FinancialGoalRepository.financialGoal;
        if (!financialGoal.getName().isEmpty() ) {
            existingGoal.setName(financialGoal.getName());
        }
        if (financialGoal.getAmount() != 0 ) {
            existingGoal.setAmount(financialGoal.getAmount());
        }
        if (financialGoal.getDate() != null ) {
            existingGoal.setDate(financialGoal.getDate());
        }
        if (!financialGoal.getCategory().equals("")) {
            existingGoal.setCategory(financialGoal.getCategory());
        }
    }

    private FinancialGoal getValues() {
        return new FinancialGoal(GnameField.getText(), Double.parseDouble(targetAmountField.getText()), Date.valueOf(targetDatePicker.getValue()), targetgoalchoisebox.getValue());
    }
    @FXML
    void goalTableAction(MouseEvent event) {
        FinancialGoalTM selectedItem = FgoalTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            GnameField.setText(selectedItem.getName());
        }
    }

    public boolean isValied() {
        if (!Regex.setTextColor(finquest.finquest.Util.TextField.ID,GnameField)) return false;
        if (!Regex.setTextColor(finquest.finquest.Util.TextField.AMOUNT,targetAmountField)) return false;
        return true;
    }

    @FXML
    void txtAmountAction(KeyEvent event) {
        Regex.setTextColor(finquest.finquest.Util.TextField.AMOUNT,targetAmountField);
    }

    @FXML
    void txtNameAction(KeyEvent event) {
        Regex.setTextColor(finquest.finquest.Util.TextField.ID,GnameField);
    }

}

