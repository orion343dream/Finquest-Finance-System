package finquest.finquest.repository;

import finquest.finquest.db.DbConnection;
import finquest.finquest.model.User;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;

public class UserRepository {
    public static User user;

    public static boolean isValidLogin(String username, String password) throws SQLException {
        String sql = "SELECT password,user_id FROM user WHERE username = ?";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setObject(1,username);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){
            if (password.equals(resultSet.getString("password"))){
                user = new User(resultSet.getString(2),username,password);
                return true;
            } else {
                new Alert(Alert.AlertType.ERROR,"Incorrect password !!").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR,"Incorrect username !!").show();
        }
        return false;
    }

    public static boolean saveUser(User user) throws SQLException {
        String sql = "INSERT INTO user (user_id, username, password) VALUES(?, ?, ?)";


        Connection connection = DbConnection.getInstance().getConnection();

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, user.getId());
        pstm.setObject(2, user.getUsername());
        pstm.setObject(3, user.getPassword());

        return pstm.executeUpdate() > 0;
    }
}
