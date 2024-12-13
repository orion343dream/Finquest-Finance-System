package finquest.finquest.controller;

import finquest.finquest.Util.Regex;
import finquest.finquest.model.Budget;
import finquest.finquest.model.Expense;
import finquest.finquest.model.FinantialAccount;
import finquest.finquest.model.tm.BudgetTM;
import finquest.finquest.model.tm.FinancialAccountTM;
import finquest.finquest.model.tm.IncomeTM;
import finquest.finquest.repository.*;
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

public class FinancialAccountController {

    @FXML
    private TableColumn<?, ?> amountColumn;

    @FXML
    private TableColumn<?, ?> amountColumn1;

    @FXML
    private ChoiceBox<String> acctypechoisebox;

    @FXML
    private TextField balanaceFeild;

    @FXML
    private TableColumn<?, ?> colAccNumber;

    @FXML
    private TableColumn<?, ?> colAccType;

    @FXML
    private TableColumn<?, ?> colBalance;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableView<FinancialAccountTM> FacctableTable;

    @FXML
    private BorderPane mainPane;

    @FXML
    private TextField nameField;

    @FXML
    private TextField numberFeild;

    @FXML
    private TextField idFeild;

    @FXML
    public void initialize() {
        acctypechoisebox.setItems(FXCollections.observableArrayList(
                "Checking Account","Savings Account","Credit Card Account","Investment Account","Loan Account",
                "Cash Account","Expense Account","Income Account","Asset Account","Liability Account"
        ));


        acctypechoisebox.setOnAction(event -> {
            String selectedCategory = acctypechoisebox.getValue();
            System.out.println("Selected category: " + selectedCategory);
        });

        loadAllDate();
    }

    private void loadAllDate() {
        ObservableList<FinancialAccountTM> obList = FXCollections.observableArrayList();
        try {
            obList = FinancialAccountRepository.getData();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        FacctableTable.setItems(obList);
        setCellValueFactory();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAccNumber.setCellValueFactory(new PropertyValueFactory<>("accNumber"));
        colAccType.setCellValueFactory(new PropertyValueFactory<>("accType"));
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
    }

    @FXML
    void addFinancialAccount(ActionEvent event) {
        if (isValied()) {
            FinantialAccount account = new FinantialAccount(idFeild.getText(), UserRepository.user.getId(), nameField.getText(), Integer.parseInt(numberFeild.getText()), acctypechoisebox.getValue(), Double.parseDouble(balanaceFeild.getText()));
            try {
                if (FinancialAccountRepository.save(account)) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Save successful").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Save failed").show();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            initialize();
        }
    }

    @FXML
    void onSearchAction(ActionEvent event) {
        try {
            if (FinancialAccountRepository.edit(idFeild.getText())) {
                FinantialAccount financialAccount = FinancialAccountRepository.financialAccount;
                nameField.setText(financialAccount.getName());
                numberFeild.setText(String.valueOf(financialAccount.getAccountNumber()));
                acctypechoisebox.setValue(financialAccount.getType());
                balanaceFeild.setText(String.valueOf(financialAccount.getBalance()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void deleteFinancialAccount(ActionEvent event) {
        try {
            if (FinancialAccountRepository.delete(idFeild.getText())){
                new Alert(Alert.AlertType.CONFIRMATION,"delete successful").show();
            } else {
                new Alert(Alert.AlertType.ERROR,"delete failed").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.CONFIRMATION,e.getMessage()).show();
        }
        initialize();
    }

    @FXML
    void UpdateFinancialAccount(ActionEvent event) {
        try {
            if (FinancialAccountRepository.edit(idFeild.getText())){
                FinantialAccount financialAccount = getValues();
                updateValues(financialAccount);
                if (FinancialAccountRepository.update()) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
                }
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllDate();
    }

    private void updateValues(FinantialAccount financialAccount) {
        FinantialAccount finantialAccount1 = FinancialAccountRepository.financialAccount;

        if (!financialAccount.getName().equals("")){
            FinancialAccountRepository.financialAccount.setName(financialAccount.getName());
        }

        if (financialAccount.getAccountNumber() != 0) {
            FinancialAccountRepository.financialAccount.setAccountNumber(financialAccount.getAccountNumber());
        }
        if (!financialAccount.getType().equals("")) {
            FinancialAccountRepository.financialAccount.setType(financialAccount.getType());
        }
        if (financialAccount.getBalance() != 0) {
            FinancialAccountRepository.financialAccount.setBalance(financialAccount.getBalance());
        }
    }
private FinantialAccount getValues() {
        return new FinantialAccount(idFeild.getText(),UserRepository.user.getId(),nameField.getText(), Integer.parseInt(numberFeild.getText()),acctypechoisebox.getValue(),Double.parseDouble(balanaceFeild.getText()));
    }
    @FXML
    void accountTableAction(MouseEvent event) {
        FinancialAccountTM selectedItem = FacctableTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            idFeild.setText(selectedItem.getId());
        }
    }

    public boolean isValied() {
        if (!Regex.setTextColor(finquest.finquest.Util.TextField.ID,idFeild)) return false;
        if (!Regex.setTextColor(finquest.finquest.Util.TextField.ID,nameField)) return false;
        if (!Regex.setTextColor(finquest.finquest.Util.TextField.AMOUNT,numberFeild)) return false;
        if (!Regex.setTextColor(finquest.finquest.Util.TextField.AMOUNT,balanaceFeild)) return false;
        return true;
    }

    @FXML
    void txtNameAction(KeyEvent event) {
        Regex.setTextColor(finquest.finquest.Util.TextField.ID,nameField);
    }

    @FXML
    void txtIdAction(KeyEvent event) {
        Regex.setTextColor(finquest.finquest.Util.TextField.ID,idFeild);
    }

    @FXML
    void txtNumberAction(KeyEvent event) {
        Regex.setTextColor(finquest.finquest.Util.TextField.AMOUNT,numberFeild);
    }
    @FXML
    void txtBalanceAction(KeyEvent event) {
        Regex.setTextColor(finquest.finquest.Util.TextField.AMOUNT,balanaceFeild);
    }
}

