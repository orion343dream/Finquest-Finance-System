package finquest.finquest.controller;

import finquest.finquest.Util.Regex;
import finquest.finquest.model.Income;
import finquest.finquest.model.tm.ExpenseTM;
import finquest.finquest.model.tm.IncomeTM;
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

public class IncomeController {

    @FXML
    private Label lblTotalIncome;

    @FXML
    private TableColumn<?, ?> accCol;

    @FXML
    private ChoiceBox<String> accchoisebox;

    @FXML
    private TextField amountFeild;

    @FXML
    private TableColumn<?, ?> categorcol;

    @FXML
    private ChoiceBox<String> catogorychoisebox;

    @FXML
    private TableColumn<?, ?> colamount;

    @FXML
    private TableColumn<?, ?> dateCol;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField desFeild;

    @FXML
    private TableColumn<?, ?> descriptionColumn;

    @FXML
    private TableView<IncomeTM> incomeTable;

    @FXML
    private BorderPane mainPane;

    @FXML
    private TextField txtIncomeId;

    @FXML
    public void initialize() {
        catogorychoisebox.setItems(FXCollections.observableArrayList(
                "Salary", "Freelance Income", "Rental Income", "Investment Income",
                "Business Income", "Gifts and Donations", "Refunds and Reimbursements",
                "Royalties", "Other Income"
        ));

                accchoisebox.setItems(FXCollections.observableArrayList(
                "Checking Account", "Savings Account", "Credit Card", "Investment Account"
        ));

        catogorychoisebox.setOnAction(event -> {
            String selectedCategory = catogorychoisebox.getValue();
            System.out.println("Selected category: " + selectedCategory);
        });

        accchoisebox.setOnAction(event -> {
            String selectedAccount = accchoisebox.getValue();
            System.out.println("Selected account: " + selectedAccount);
        });

        loadAllDate();
        getTotalIncome();
    }

    private void getTotalIncome() {
        try {
            double total = IncomeRepository.getTotal();
            lblTotalIncome.setText("Rs. " + total);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllDate() {
        ObservableList<IncomeTM> obList = FXCollections.observableArrayList();
        try {
            obList = IncomeRepository.getData();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        incomeTable.setItems(obList);
        setCellValueFactory();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colamount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        categorcol.setCellValueFactory(new PropertyValueFactory<>("category"));
        accCol.setCellValueFactory(new PropertyValueFactory<>("acc"));
    }

    @FXML
    void addIncome(ActionEvent event) {
        if (isValied()) {
            Income income = new Income(txtIncomeId.getText(), UserRepository.user.getId(), Double.parseDouble(amountFeild.getText()), desFeild.getText(), Date.valueOf(datePicker.getValue()), catogorychoisebox.getValue(), accchoisebox.getValue());
            try {
                if (IncomeRepository.save(income)) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Save successful").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Save failed").show();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            loadAllDate();
            getTotalIncome();
        }
    }

    @FXML
    void deleteIncome(ActionEvent event) {
        try {
            if (IncomeRepository.delete(txtIncomeId.getText())) {
                new Alert(Alert.AlertType.CONFIRMATION, "delete successful").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "delete failed").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.CONFIRMATION, e.getMessage()).show();
        }
        loadAllDate();
        getTotalIncome();
    }

    @FXML
    void idAction(ActionEvent event) {
        try {
            if (IncomeRepository.edit(txtIncomeId.getText())) {
                Income income = IncomeRepository.income;
                amountFeild.setText(String.valueOf(income.getAmount()));
                desFeild.setText(String.valueOf(income.getDescription()));
                datePicker.setValue(income.getDate().toLocalDate());
                catogorychoisebox.setValue(income.getCategory());
                accchoisebox.setValue(income.getAccount());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.CONFIRMATION, e.getMessage()).show();
        }
    }

    @FXML
    void updateIncome(ActionEvent event) {
        try {
            Income income = getValues();
            updateValues(income);
            if (IncomeRepository.update()) {
                new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllDate();
        getTotalIncome();
    }

    private void updateValues(Income income) {
        if (!income.getDescription().equals("")) {
            IncomeRepository.income.setDescription(income.getDescription());
        }
        if (income.getAmount() != 0 ) {
            IncomeRepository.income.setAmount(income.getAmount());
        }
        if (!income.getDate().equals("")) {
            IncomeRepository.income.setDate(income.getDate());
        }
        if (!income.getCategory().equals("")) {
            IncomeRepository.income.setCategory(income.getCategory());
        }
        if (!income.getAccount().equals("")) {
            IncomeRepository.income.setAccount(income.getAccount());
        }
    }

    private Income getValues() {
        return new Income(txtIncomeId.getText(), UserRepository.user.getId(), Double.parseDouble(amountFeild.getText()), desFeild.getText(), Date.valueOf(datePicker.getValue()), catogorychoisebox.getValue(), accchoisebox.getValue());
    }
    @FXML
    void incomeTableAction(MouseEvent event) {
        IncomeTM selectedItem = incomeTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            txtIncomeId.setText(selectedItem.getId());
        }
    }

    public boolean isValied() {
        if (!Regex.setTextColor(finquest.finquest.Util.TextField.ID,txtIncomeId)) return false;
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
        Regex.setTextColor(finquest.finquest.Util.TextField.ID,txtIncomeId);
    }

    @FXML
    void txtDesAction(KeyEvent event) {
        Regex.setTextColor(finquest.finquest.Util.TextField.ID,desFeild);
    }

}
