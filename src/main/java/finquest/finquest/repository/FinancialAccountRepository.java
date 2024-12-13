package finquest.finquest.repository;

import finquest.finquest.db.DbConnection;
import finquest.finquest.model.FinantialAccount;
import finquest.finquest.model.tm.ExpenseTM;
import finquest.finquest.model.tm.FinancialAccountTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FinancialAccountRepository {
    public static FinantialAccount financialAccount;

    public static ObservableList<FinancialAccountTM> getData() throws SQLException {
        ObservableList<FinancialAccountTM> obList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM financialaccount";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet rs = ptsm.executeQuery();

        while (rs.next()) {
            obList.add(new FinancialAccountTM(rs.getString("account_id"),rs.getString("account_name"), rs.getInt("number"), rs.getString("account_type"),rs.getDouble("balance") ));
        }

        return obList;
    }

    public static boolean save(FinantialAccount account) throws SQLException {
        String sql = "INSERT INTO financialAccount (account_id, user_id,account_name,number, account_type,balance) VALUES (?,?,?,?,?,?) ";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setObject(1,account.getId());
        ptsm.setObject(2,account.getUserId());
        ptsm.setObject(3,account.getName());
        ptsm.setObject(4,account.getAccountNumber());
        ptsm.setObject(5,account.getType());
        ptsm.setObject(6,account.getBalance());


        return ptsm.executeUpdate() > 0;
    }

    public static boolean delete(String text) throws SQLException {
        String sql = "DELETE FROM financialAccount WHERE account_id = ?";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setString(1, text);

        return ptsm.executeUpdate() > 0;
    }
    public static boolean edit(String id) throws SQLException {
        String sql = "SELECT * FROM financialAccount WHERE account_id = ?";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setString(1, id);

        ResultSet rs = ptsm.executeQuery();

        if (rs.next()) {
            financialAccount = new FinantialAccount(
                    rs.getString("account_id"),
                    rs.getString("user_id"),
                    rs.getString("account_name"),
                    rs.getInt("number"),
                    rs.getString("account_type"),
                    rs.getDouble("balance")
            );
            return true;
        } else {
            new Alert(Alert.AlertType.CONFIRMATION,"can't find name").show();
        }
        return false;
    }


    public static boolean update() throws SQLException {
        String sql = "UPDATE financialAccount SET account_name = ? , number = ? , account_type = ? , balance = ? WHERE account_id = ?";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setObject(1, financialAccount.getName());
        ptsm.setObject(2, financialAccount.getAccountNumber());
        ptsm.setObject(3, financialAccount.getType());
        ptsm.setObject(4, financialAccount.getBalance());
        ptsm.setObject(5, financialAccount.getId());

        return ptsm.executeUpdate() > 0;
    }
}