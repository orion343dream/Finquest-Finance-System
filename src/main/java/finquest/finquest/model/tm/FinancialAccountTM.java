package finquest.finquest.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FinancialAccountTM {
    private String Id;
    private String name;
    private int accNumber;
    private String accType;
    private double balance;
}
