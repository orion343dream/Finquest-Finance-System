package finquest.finquest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Expense {
    private String id;
    private String userId;
    private double amount;
    private String description;
    private Date date;
    private String category;
}
