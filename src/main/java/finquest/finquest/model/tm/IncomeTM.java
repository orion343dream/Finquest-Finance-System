package finquest.finquest.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IncomeTM {
    private String id;
    private double amount;
    private String description;
    private Date date;
    private String category;
    private String acc;
}
