package finquest.finquest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FinantialAccount{
    private String id;
    private String userId;
    private String name;
    private int accountNumber;
    private String type;
    private double balance;
}