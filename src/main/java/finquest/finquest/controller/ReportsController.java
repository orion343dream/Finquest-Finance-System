package finquest.finquest.controller;

import finquest.finquest.db.DbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ReportsController {

    @FXML
    void generateBudgetReportAction(ActionEvent event) {
        generateReport("Report/Budget.jrxml");
    }

    @FXML
    void generateExpenseReportAction(ActionEvent event) {
        generateReport("Report/Expense.jrxml");
    }

    @FXML
    void generateIncomeReportAction(ActionEvent event) {
        generateReport("Report/Income.jrxml");
    }

    private void generateReport(String reportPath) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(reportPath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Report file not found: " + reportPath);
            }

            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            Map<String, Object> data = new HashMap<>();

            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
}
