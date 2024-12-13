package finquest.finquest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Finacialgoal {
    private int id;
    private String name;
    private double amount;
    private Date date;
    private String category;
}