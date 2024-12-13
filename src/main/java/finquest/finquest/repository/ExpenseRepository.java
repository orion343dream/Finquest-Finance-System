package finquest.finquest.repository;

import finquest.finquest.db.DbConnection;
import finquest.finquest.model.Budget;
import finquest.finquest.model.Expense;
import finquest.finquest.model.Income;
import finquest.finquest.model.tm.BudgetTM;
import finquest.finquest.model.tm.ExpenseTM;
import finquest.finquest.model.tm.IncomeTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExpenseRepository {
    public static Expense expense;

    public static double totalExpense() throws SQLException {
        double total = 0;

        String sql = "SELECT amount FROM expense";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet rs = ptsm.executeQuery();

        while (rs.next()) {
            total += rs.getDouble("amount");
        }
        return total;
    }

    public static ObservableList<ExpenseTM> getData() throws SQLException {
        ObservableList<ExpenseTM> obList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM expense";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet rs = ptsm.executeQuery();

        while (rs.next()) {
            obList.add(new ExpenseTM(rs.getString("expense_id"),rs.getDouble("amount"),rs.getString("description"),rs.getDate("date"),rs.getString("category")));
        }

        return obList;
    }

    public static boolean save(Expense expense) throws SQLException {
        Connection conn = DbConnection.getInstance().getConnection();
        conn.setAutoCommit(false);

        try {
            if (saveExpense(expense)){
                if (ReminderRepository.save(expense,FinancialGoalRepository.getExpenseGoal())){
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

    public static boolean saveExpense(Expense expense) throws SQLException {
        String sql = "INSERT INTO expense (expense_id, user_id, amount, description, date,category) VALUES (?,?,?,?,?,?)";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setObject(1, expense.getId());
        pstm.setObject(2, expense.getUserId());
        pstm.setObject(3, expense.getAmount());
        pstm.setObject(4, expense.getDescription());
        pstm.setObject(5, expense.getDate());
        pstm.setObject(6, expense.getCategory());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String text) throws SQLException {
        String sql = "DELETE FROM expense WHERE expense_id = ?";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setString(1, text);

        return ptsm.executeUpdate() > 0;
    }

    public static boolean edit(String text) throws SQLException {
        String sql = "SELECT * FROM expense WHERE expense_id = ?";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setString(1, text);

        ResultSet rs = ptsm.executeQuery();

        if (rs.next()){
            expense = new Expense(rs.getString("expense_id"),rs.getString("user_id"),rs.getDouble("amount"),rs.getString("description"),rs.getDate("date"),rs.getString("category"));
            return true;
        } else {
            new Alert(Alert.AlertType.CONFIRMATION,"can't find name").show();
        }
        return false;
    }

    public static boolean update() throws SQLException {
        String sql = "UPDATE expense SET amount = ? , description = ? , date = ? , category = ? WHERE expense_id = ?";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setDouble(1, expense.getAmount());
        ptsm.setString(2, expense.getDescription());
        ptsm.setDate(3, expense.getDate());
        ptsm.setString(4, expense.getCategory());
        ptsm.setObject(5, expense.getId());

        return ptsm.executeUpdate() > 0;
    }
    public static double getTotal() throws SQLException {
        double total = 0;

        String sql = "select amount from expense";

        ResultSet set = DbConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();

        while (set.next()) {
            total += set.getDouble("amount");
        }
        return total;
      }
    public static int getCount() throws SQLException {
        int count = 0;
        String sql = "select count(*) from expense order by expense_id";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet rs = ptsm.executeQuery();

        if (rs.next()) {
            count = rs.getInt(1);
        }

        return count;
    }

}
