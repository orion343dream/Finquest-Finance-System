package finquest.finquest.model.tm;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class BudgetTM {
    private String name;
    private double amount;
    private Date startDate;
    private Date endDate;
}

