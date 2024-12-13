package finquest.finquest.controller;

import finquest.finquest.model.tm.ReminderTM;
import finquest.finquest.repository.BudgetRepository;
import finquest.finquest.repository.ReminderRepository;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.sql.SQLException;

public class ReminderController {

    @FXML
    private TableColumn<?, ?> amountColumn;


    @FXML
    private TableColumn<?, ?> dateColumn;

    @FXML
    private TableView<ReminderTM> reminderTable;

    private ReminderTM reminderTM;

    public void initialize() {
        setCellValueFactory();
        loadData();
    }

    private void loadData() {
        try {
            ObservableList<ReminderTM> list = ReminderRepository.getData();
            reminderTable.setItems(list);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("text"));
    }

    @FXML
    void RemiderTblAction(MouseEvent event) {
        reminderTM = null;
        reminderTM = reminderTable.getSelectionModel().getSelectedItem();
    }


    @FXML
    void DeleteReminder(ActionEvent event) {
        try {
            if (reminderTM != null){
                if (ReminderRepository.delete(reminderTM.getText())){
                    new Alert(Alert.AlertType.CONFIRMATION,"Reminder deleted successfully").show();
                } else {
                    new Alert(Alert.AlertType.ERROR,"Failed to delete reminder").show();
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadData();
    }

}
