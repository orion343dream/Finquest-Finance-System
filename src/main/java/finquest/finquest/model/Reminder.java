package finquest.finquest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Reminder {
    private String userId;
    private Date date;
    private String text;
    private int goalId;
}
