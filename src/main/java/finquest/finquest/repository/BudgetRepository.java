package finquest.finquest.repository;

import finquest.finquest.db.DbConnection;
import finquest.finquest.model.Budget;
import finquest.finquest.model.tm.BudgetTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringJoiner;

public class BudgetRepository {
    public static Budget budget;

    public static boolean save(Budget budget) throws SQLException {
        Connection conn = DbConnection.getInstance().getConnection();
        conn.setAutoCommit(false);

        try {
            if (saveBudget(budget)){
                if (ReminderRepository.save(budget,FinancialGoalRepository.getBudgetGoal())){
                    conn.commit();
                    return true;
                }
            }
            conn.rollback();
            return false;
        } catch (Exception e){
            System.out.println(e.getMessage());
            conn.rollback();
            return false;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public static boolean saveBudget(Budget budget) throws SQLException {
        String sql = "INSERT INTO budget (user_id,budget_name,amount,start_date,end_date) VALUES (?,?,?,?,?)";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, budget.getUserId());
        pstm.setString(2, budget.getName());
        pstm.setDouble(3, budget.getAmount());
        pstm.setDate(4, budget.getStartDate());
        pstm.setDate(5, budget.getEndDate());

        return pstm.executeUpdate() > 0;
    }


    public static boolean delete(String text) throws SQLException {
        String sql = "DELETE FROM budget WHERE budget_name = ?";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setString(1, text);

        return ptsm.executeUpdate() > 0;
    }

    public static boolean edit(String text) throws SQLException {
        budget = null;
        String sql = "SELECT * FROM budget WHERE budget_name = ?";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setString(1, text);

        ResultSet rs = ptsm.executeQuery();

        if (rs.next()){
            budget = new Budget(rs.getString("user_id"), rs.getString("budget_name"),rs.getDouble("amount"),rs.getDate("start_date"),rs.getDate("end_date"));
            return true;
        } else {
            new Alert(Alert.AlertType.CONFIRMATION,"can't find name").show();
        }
        return false;
    }

    public static ObservableList<BudgetTM> getData() throws SQLException {
        ObservableList<BudgetTM> obList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM budget";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet rs = ptsm.executeQuery();

        while (rs.next()) {
            obList.add(new BudgetTM(rs.getString("budget_name"), rs.getDouble("amount"), rs.getDate("start_date"), rs.getDate("end_date")));
        }

        return obList;
    }

    public static boolean update() throws SQLException {
        String sql = "UPDATE budget SET budget_name = ?, amount = ?, start_date = ?, end_date = ? WHERE budget_name = ?";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setString(1, budget.getName());
        ptsm.setDouble(2, budget.getAmount());
        ptsm.setDate(3, budget.getStartDate());
        ptsm.setDate(4, budget.getEndDate());
        ptsm.setString(5, budget.getName());

        return ptsm.executeUpdate() > 0;
    }

    public static double totalBudget() throws SQLException {
        double total = 0;

        String sql = "SELECT amount FROM budget";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet rs = ptsm.executeQuery();

        while (rs.next()) {
            total += rs.getDouble("amount");
        }
        return total;
    }

public static int getCount() throws SQLException {
    int count = 0;
    String sql = "select count(*) from budget order by budget_name";

    PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
    ResultSet rs = ptsm.executeQuery();

    if (rs.next()) {
        count = rs.getInt(1);
    }

    return count;
}
}