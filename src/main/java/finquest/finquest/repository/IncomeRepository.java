package finquest.finquest.repository;

import finquest.finquest.db.DbConnection;
import finquest.finquest.model.Budget;
import finquest.finquest.model.Income;
import finquest.finquest.model.tm.BudgetTM;
import finquest.finquest.model.tm.IncomeTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IncomeRepository {
    public static Income income;

    public static double totalIncome() throws SQLException {
        double total = 0;

        String sql = "SELECT amount FROM income";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet rs = ptsm.executeQuery();

        while (rs.next()) {
            total += rs.getDouble("amount");
        }
        return total;
    }

    public static ObservableList<IncomeTM> getData() throws SQLException {
        ObservableList<IncomeTM> obList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM income";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet rs = ptsm.executeQuery();

        while (rs.next()) {
            obList.add(new IncomeTM(rs.getString("income_id"),rs.getDouble("amount"),rs.getString("description"),rs.getDate("date"),rs.getString("category"), rs.getString("account")));
        }

        return obList;
    }

    public static boolean save(Income income) throws SQLException {
        Connection conn = DbConnection.getInstance().getConnection();
        conn.setAutoCommit(false);

        try {
            if (saveIncome(income)){
                System.out.println("1");
                if (ReminderRepository.save(income,FinancialGoalRepository.getIncomeGoal())){
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

    public static boolean saveIncome(Income income) throws SQLException {
        String sql = "INSERT INTO income (income_id, user_id, amount, description, date,category, account) VALUES (?,?,?,?,?,?,?)";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setObject(1, income.getId());
        pstm.setObject(2, income.getUserId());
        pstm.setObject(3, income.getAmount());
        pstm.setObject(4, income.getDescription());
        pstm.setObject(5, income.getDate());
        pstm.setObject(6, income.getCategory());
        pstm.setObject(7, income.getAccount());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String text) throws SQLException {
        String sql = "DELETE FROM income WHERE income_id = ?";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setString(1, text);

        return ptsm.executeUpdate() > 0;
    }

    public static boolean edit(String text) throws SQLException {
        String sql = "SELECT * FROM income WHERE income_id = ?";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setString(1, text);

        ResultSet rs = ptsm.executeQuery();

        if (rs.next()){
            income = new Income(rs.getString("income_id"),rs.getString("user_id"),rs.getDouble("amount"),rs.getString("description"),rs.getDate("date"),rs.getString("category"),rs.getString("account"));
            return true;
        } else {
            new Alert(Alert.AlertType.CONFIRMATION,"can't find name").show();
        }
        return false;
    }

    public static boolean update() throws SQLException {
        String sql = "UPDATE income SET amount = ? , description = ? , date = ? , category = ? , account = ? WHERE income_id = ?";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setDouble(1, income.getAmount());
        ptsm.setString(2, income.getDescription());
        ptsm.setDate(3, income.getDate());
        ptsm.setString(4, income.getCategory());
        ptsm.setString(5, income.getAccount());
        ptsm.setObject(6, income.getId());

        return ptsm.executeUpdate() > 0;
    }

    public static double getTotal() throws SQLException {
        double total = 0;

        String sql = "select amount from income";

        ResultSet set = DbConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();

        while (set.next()) {
            total += set.getDouble("amount");
        }
        return total;
    }
    public static int getCount() throws SQLException {
        int count = 0;
        String sql = "select count(*) from income order by income_id";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet rs = ptsm.executeQuery();

        if (rs.next()) {
            count = rs.getInt(1);
        }

        return count;
    }
}
