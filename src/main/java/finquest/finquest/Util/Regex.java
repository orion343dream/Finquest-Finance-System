package finquest.finquest.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static boolean isTextFieldValid(TextField textField, String text){
        String filed = "";

        switch (textField){
            case ID:
                filed = "^[0-9a-zA-Z]*$";
                break;
            case NIC:
                filed = "^([0-9]{9}[x|X|v|V]|[0-9]{12})$";
                break;
            case NAME:
                filed = "^[A-Za-z]+(?: [A-Za-z]+)*$";
                break;
            case EMAIL:
                filed = "^([A-z])([A-z0-9.]){1,}[@]([A-z0-9]){1,10}[.]([A-z]){2,5}$";
                break;
            case ADDRESS:
                filed = "^([A-z0-9]|[-\\,.@+]|\\\\s){4,}$";
                break;
            case TEL:
                filed = "^[0]([1-9]{2})([0-9]){7}$";
                break;
            case QTY:
                filed = "^[0-9]{1,5}$";
                break;
            case PRICE:
                filed = "^([0-9]){1,}[.]([0-9]){1,}$";
                break;
            case AMOUNT:
                filed = "^[0-9]+(\\.[0-9]*)?$";
                break;
            case PASSWORD:
                filed = "^[0-9a-zA-Z!@#$%^&*()_+=-]{4,}$";
                break;

        }

        Pattern pattern = Pattern.compile(filed);

        if (text != null){
            if (text.trim().isEmpty()){
                return false;
            }
        }else {
            return false;
        }

        Matcher matcher = pattern.matcher(text);

        if (matcher.matches()){
            return true;
        }
        return false;
    }
    public static boolean setTextColor(TextField location, javafx.scene.control.TextField textField){
        if (Regex.isTextFieldValid(location, textField.getText())){
            textField.setStyle("-fx-focus-color: #00FF00;");
            return true;
        }else {
            textField.setStyle("-fx-border-color: red;-fx-border-radius: 5;-fx-border-width: 3;");
            return false;
        }
    }
}
