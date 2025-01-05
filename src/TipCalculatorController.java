import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class TipCalculatorController {

    //Fromatters for currency(Para birimi) and percentages
    private static final NumberFormat currency = NumberFormat.getCurrencyInstance();
    private static final NumberFormat percentage = NumberFormat.getPercentInstance();

    //A BigDecimal object named "tipPercentage" to store the current tip percentage for calculation
    private BigDecimal tipPercentage = new BigDecimal(0.15); // %15 default

    @FXML
    private TextField amountTextField;

    @FXML
    private Label tipPercentageLabel;

    @FXML
    private Slider tipPercentageSlider;

    @FXML
    private TextField tipTextField;

    @FXML
    private TextField totalTextField;

    //initialize method to set the default values and listen the slider
    @FXML
    public void initialize() {

        System.out.println("Initialize çalışıyor!");


        //Set initial rounding mode
        currency.setRoundingMode(java.math.RoundingMode.HALF_UP); // (A >= 5)--> A++ || (A < 5) -->  A

        /*
        * tipPercentageSlider.valueProperty() --> Slider'ın o anki değerini temsil eden ObservableValue nesnesi
        * Slider üzerindeki kullanıcı etkileşimleriyle bu değer değişir
        *
        * .addListener() --> Slider'ın değerindeki her değişikliği takip etmek için bir Listener ekler
        * Bu dinleyici, bir ChangeListener nesnesidir.
        * Değeri değiştiğinde verilen changed methodu otomatik çalıştırılır
        *
        * new ChangeListener<Number>() --> arayüzdür ve slider'ın değeri değşitinde çalışacak changed methodunu sağlar
        * Parametreleri
        *   observableValue --> Dinlenilen Slider'ın valueProperty özelliği
        */
        tipPercentageSlider.valueProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {

                        //Convert newValue to int (because its Number) --> If slider 15.0, cast to 15
                        //Divide to 100 to convert it to percentage --> 15 / 100 = 0.15
                        //Cast to BigDecimal
                        tipPercentage = BigDecimal.valueOf(newValue.intValue() / 100.0);

                        //percentage kullanılarak tipPercentage değeri okunabilir bir yüzde olur --> 0.15 becomes %15
                        tipPercentageLabel.setText(percentage.format(tipPercentage));
                    }
                }
        );
    }

    //Calculates and displays the tip and total amounts
    @FXML
    void calculateButtonPressed(ActionEvent event) { //Event handler must return void and get ActionEvent type parameter

        try {
            //Get the amount in the textField and cast to big decimal
            BigDecimal amount = new BigDecimal(amountTextField.getText());

            //Calculate the tip
            BigDecimal tip = amount.multiply(tipPercentage);

            //Calculate the total
            BigDecimal total = amount.add(tip);

            //Display the results:
            tipTextField.setText(currency.format(tip)); //Tip amount
            totalTextField.setText(currency.format(total)); //Total amount

        } catch (NumberFormatException ex) {
            //If an invalid amount is entered
            amountTextField.setText("Enter amount"); //Error message
            amountTextField.selectAll(); //Select all text
            amountTextField.requestFocus(); //Focus to text field
        }

    }

}
