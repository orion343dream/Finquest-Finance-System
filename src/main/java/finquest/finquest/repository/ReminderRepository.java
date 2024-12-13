package finquest.finquest.repository;

import finquest.finquest.db.DbConnection;
import finquest.finquest.model.*;
import finquest.finquest.model.tm.ReminderTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReminderRepository {
    public static boolean save(Income income, ObservableList<Finacialgoal> incomeGoal) throws SQLException {
        for (Finacialgoal finacialgoal : incomeGoal) {
            double value = finacialgoal.getAmount() - (IncomeRepository.totalIncome());
            double percentage = (IncomeRepository.totalIncome() / finacialgoal.getAmount()) * 100;
            String text = null;
            if (value > 0){
                text = "Your " + finacialgoal.getName() + ", " + finacialgoal.getCategory() + " type goal need " + value + " (" +  percentage + "%) to complete the goal";
            } else {
                text = "Your " + finacialgoal.getName() + ", " + finacialgoal.getCategory() + " type goal is complete succusfully ";
                try {
                    if (!FinancialGoalRepository.updateAchieve(finacialgoal.getId())){
                        return false;
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            Reminder reminder = new Reminder(UserRepository.user.getId(), Date.valueOf(LocalDate.now()),text, finacialgoal.getId());
            if (!save(reminder)){
                return false;
            }
        }
        return true;
    }

    private static boolean save(Reminder reminder) throws SQLException {
        String sql = "INSERT INTO reminder (user_id, reminder_date, reminder_text, goal_id) VALUES (?,?,?,?)";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setObject(1,reminder.getUserId());
        ptsm.setObject(2,reminder.getDate());
        ptsm.setString(3,reminder.getText());
        ptsm.setObject(4,reminder.getGoalId());

        return ptsm.executeUpdate() > 0;
    }

    public static ObservableList<ReminderTM> getData() throws SQLException {
        ObservableList<ReminderTM> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM reminder";

        ResultSet rs = DbConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();
        while (rs.next()){
            list.add(new ReminderTM(Date.valueOf(LocalDate.now()),rs.getString("reminder_text")));
        }
        return list;
    }

    public static boolean save(Budget budget, ObservableList<Finacialgoal> budgetGoal) throws SQLException {
        for (Finacialgoal finacialgoal : budgetGoal) {
            double value = finacialgoal.getAmount() - (BudgetRepository.totalBudget());
            double percentage = (BudgetRepository.totalBudget() / finacialgoal.getAmount()) * 100;
            String text = null;
            if (value > 0){
                text = "Your " + finacialgoal.getName() + ", " + finacialgoal.getCategory() + "  type goal need " + value + " (" +  percentage + "%) to complete the goal";
            } else {
                text = "Your" + finacialgoal.getName() + ", " + finacialgoal.getCategory() +" type goal is complete succusfully ";
                try {
                    if (!FinancialGoalRepository.updateAchieve(finacialgoal.getId())){
                        return false;
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            Reminder reminder = new Reminder(UserRepository.user.getId(), Date.valueOf(LocalDate.now()),text, finacialgoal.getId());
            if (!save(reminder)){
                return false;
            }
        }
        return true;
    }

    public static boolean save(Expense expense, ObservableList<Finacialgoal> expenseGoal) throws SQLException {
        for (Finacialgoal finacialgoal : expenseGoal) {
            double value = (BudgetRepository.totalBudget() + IncomeRepository.totalIncome()) - finacialgoal.getAmount() ;
            double percentage = (finacialgoal.getAmount() / (BudgetRepository.totalBudget() + IncomeRepository.totalIncome())) * 100;
            String text = null;
            if (value > 0){
                text = "Your " + finacialgoal.getName() + ", " + finacialgoal.getCategory() + " type goal could be able to spent " + value + " (" +  percentage + "%) of money without fail the goal";
            } else {
                text = "Your " + finacialgoal.getName() + ", " + finacialgoal.getCategory() + " type goal is failed";
                try {
                    if (!FinancialGoalRepository.updateFailed(finacialgoal.getId())){
                        return false;
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            Reminder reminder = new Reminder(UserRepository.user.getId(), Date.valueOf(LocalDate.now()),text, finacialgoal.getId());
            if (!save(reminder)){
                return false;
            }
        }
        return true;
    }

    public static boolean delete(String text) throws SQLException {
        String sql = "Delete from reminder where reminder_text = ?";

        PreparedStatement ptsm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ptsm.setString(1,text);

        return ptsm.executeUpdate() > 0;
    }

    public static int getCount() throws SQLException {
        String sql = "select count(*) from reminder order by reminder_id";

        ResultSet set = DbConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();
        if (set.next()){
            return set.getInt(1);
        }
        return 0;
    }

}
