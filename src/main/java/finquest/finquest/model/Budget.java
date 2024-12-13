package finquest.finquest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Budget {
    private String userId;
    private String name;
    private double amount;
    private Date startDate;
    private Date endDate;
}

