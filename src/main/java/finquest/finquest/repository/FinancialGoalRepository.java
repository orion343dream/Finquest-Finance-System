package finquest.finquest.repository;

import finquest.finquest.db.DbConnection;
import finquest.finquest.model.Finacialgoal;
import finquest.finquest.model.FinancialGoal;
import finquest.finquest.model.tm.FinancialGoalTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FinancialGoalRepository {

    public static FinancialGoal financialGoal;

    public static ObservableList<FinancialGoalTM> getData() throws SQLException {
        ObservableList<FinancialGoalTM> obList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM financialgoal";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet rs = ptsm.executeQuery();

        while (rs.next()) {
            obList.add(new FinancialGoalTM(rs.getString("name"), rs.getDouble("target_amount"), rs.getDate("target_date"), rs.getString("category")));
        }

        return obList;
    }

    public static boolean save(FinancialGoal financialGoal) throws SQLException {
        String sql = "INSERT INTO financialgoal (name, target_amount, target_date, category) VALUES (?, ?, ?, ?) ";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setString(1, financialGoal.getName());
        ptsm.setDouble(2, financialGoal.getAmount());
        ptsm.setDate(3, financialGoal.getDate());
        ptsm.setString(4, financialGoal.getCategory());

        return ptsm.executeUpdate() > 0;
    }

    public static boolean delete(String name) throws SQLException {
        String sql = "DELETE FROM financialGoal WHERE name = ?";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setString(1, name);

        return ptsm.executeUpdate() > 0;
    }
    public static boolean edit(String name) throws SQLException {
        String sql = "SELECT * FROM financialGoal WHERE name = ?";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setString(1, name);

        ResultSet rs = ptsm.executeQuery();

        if (rs.next()) {
            financialGoal = new FinancialGoal(
                    rs.getString("name"),
                    rs.getDouble("target_amount"),
                    rs.getDate("target_date"),
                    rs.getString("category")
            );
            return true;
        } else {
            new Alert(Alert.AlertType.CONFIRMATION,"can't find name").show();
        }
        return false;
    }
    public static boolean update(FinancialGoal financialGoal) throws SQLException {
        String sql = "UPDATE financialGoal SET target_amount = ?, target_date = ?, category = ? WHERE name = ?";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setDouble(1, financialGoal.getAmount());
        ptsm.setDate(2, financialGoal.getDate());
        ptsm.setString(3, financialGoal.getCategory());
        ptsm.setString(4, financialGoal.getName());

        return ptsm.executeUpdate() > 0;
    }

    public static ObservableList<Finacialgoal> getIncomeGoal() throws SQLException {
        ObservableList<Finacialgoal> obList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM financialgoal";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet rs = ptsm.executeQuery();
        while (rs.next()) {
            if (rs.getString("category").equals("Income") && rs.getString("achieved") == null){
                obList.add(new Finacialgoal(rs.getInt("goal_id"),rs.getString("name"),rs.getDouble("target_amount"),rs.getDate("target_date"),rs.getString("category")));
            }
        }
        return obList;
    }

    public static ObservableList<Finacialgoal> getExpenseGoal() throws SQLException {
        ObservableList<Finacialgoal> obList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM financialgoal";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet rs = ptsm.executeQuery();
        while (rs.next()) {
            if (rs.getString("category").equals("Expense") && rs.getString("achieved") == null){
                obList.add(new Finacialgoal(rs.getInt("goal_id"),rs.getString("name"),rs.getDouble("target_amount"),rs.getDate("target_date"),rs.getString("category")));
            }
        }
        return obList;
    }

    public static ObservableList<Finacialgoal> getBudgetGoal() throws SQLException {
        ObservableList<Finacialgoal> obList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM financialgoal";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet rs = ptsm.executeQuery();
        while (rs.next()) {
            if (rs.getString("category").equals("Budget") && rs.getString("achieved") == null){
                obList.add(new Finacialgoal(rs.getInt("goal_id"),rs.getString("name"),rs.getDouble("target_amount"),rs.getDate("target_date"),rs.getString("category")));
            }
        }
        return obList;
    }

    public static boolean updateAchieve(int id) throws SQLException {
        String sql = "UPDATE financialgoal SET achieved = ? WHERE goal_id = ?";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setObject(1,"Achieved");
        ptsm.setInt(2, id);
        return ptsm.executeUpdate() > 0;
    }

    public static boolean updateFailed(int id) throws SQLException {
        String sql = "UPDATE financialgoal SET achieved = ? WHERE goal_id = ?";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setObject(1,"Failed");
        ptsm.setInt(2, id);
        return ptsm.executeUpdate() > 0;
    }

    public static int getCount() throws SQLException {
        String sql = "select count(*) from financialgoal order by goal_id";

        ResultSet set = DbConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();
        if (set.next()){
            return set.getInt(1);
        }
        return 0;
    }

    public static int getIncome() throws SQLException {
        String sql = "select count(*) from financialgoal where category = 'Income' order by goal_id";

        ResultSet set = DbConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();
        if (set.next()){
            return set.getInt(1);
        }
        return 0;
    }

    public static int getExpense() throws SQLException {
        String sql = "select count(*) from financialgoal where category = 'Expense' order by goal_id";

        ResultSet set = DbConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();
        if (set.next()){
            return set.getInt(1);
        }
        return 0;
    }

    public static int getBudget() throws SQLException {
        String sql = "select count(*) from financialgoal where category = 'Budget' order by goal_id";

        ResultSet set = DbConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();
        if (set.next()){
            return set.getInt(1);
        }
        return 0;
    }
}
