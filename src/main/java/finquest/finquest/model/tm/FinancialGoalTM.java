package finquest.finquest.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FinancialGoalTM {
    private String name;
    private double amount;
    private Date date;
    private String achieved;
}