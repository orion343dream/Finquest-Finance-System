package finquest.finquest.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpenseTM {
    private String id;
    private double amount;
    private String description;
    private Date date;
    private String category;
}