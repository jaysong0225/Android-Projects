package com.example.w0302272.mathcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // TextView to display the calculation
    private TextView txtDisplay;

    // ID Array of all the numeric buttons (0~9)
    private int[] numericButtons = {R.id.btnOne, R.id.btnTwo, R.id.btnThree, R.id.btnFour, R.id.btnFive,
            R.id.btnSix, R.id.btnSeven, R.id.btnEight, R.id.btnNine, R.id.btnZero};

    // ID Array of all the operator buttons
    private int[] operatorButtons = {R.id.btnAddition, R.id.btnSubtraction, R.id.btnMultiple, R.id.btnDivision, R.id.btnCalculate};

    // set of flags to control inputs
    private boolean b_lastNumeric;
    private boolean b_operator;

    // create an instance for the calculator
    myCalculator calculator = new myCalculator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Flag initialization
        this.b_lastNumeric = false;
        this.b_operator = false;

        // Data initialization

        // Find the TextView
        this.txtDisplay = (TextView) findViewById(R.id.txtDisplay);

        // Find and set OnClickListener for the numeric buttons (0 ~ 9)
        setNumericOnClickListener();

        // Find and set OnClickListener for the operator buttons (+ - * / =)
        setOperatorOnClickListener();

        // Find and set OnClickListener for other buttons (<, C, "+/-", .)
        setOperatorOnOtherListener();
    }

    /*
        Function to set OnClickListener for the numeric buttons
    */
    private void setNumericOnClickListener() {
        // Create a numeric button listener
        View.OnClickListener numericBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button numericBtn = (Button) view;

                // Handle the numeric button click here
                if(txtDisplay.getText().toString().equals("division by zero")) {
                    // clear the screen
                    txtDisplay.setText("");
                }
                txtDisplay.append(numericBtn.getText());
                b_lastNumeric = true;
            }
        }; // end annon inner class

        // Assign the listener to all the numeric buttons
        for(int id : numericButtons) {
            findViewById(id).setOnClickListener(numericBtnListener);
        }
    }

    /*
        Function to set OnClickListener for the operator buttons
    */
    private void setOperatorOnClickListener() {
        // Create a operator button listener
        View.OnClickListener operatorBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button operatorBtn = (Button) view;
                String strOperator = operatorBtn.getText().toString();

                // Handle the numeric button click here
                if(b_lastNumeric == true && b_operator == false && !strOperator.equals("=")) {
                    // allow operator
                    txtDisplay.append(operatorBtn.getText());
                    b_operator = true;
                    b_lastNumeric = false;
                }
                else if(b_lastNumeric == true && b_operator == true) {
                    // execute the calculation with the previous operator
                    // step1: calculate
                    double myAnswer = calculator.requestCalculation(txtDisplay.getText().toString());

                    // step2: update the txtDisplay
                    if(strOperator.equals("+") || strOperator.equals("-") || strOperator.equals("x") || strOperator.equals("/")) {
                        // applying rolling calculation
                        if(!Double.toString(myAnswer).equals("Infinity") && !Double.toString(myAnswer).equals("NaN")
                                && !Double.toString(myAnswer).equals("-Infinity")) {
                            if((myAnswer == (int)myAnswer))
                                txtDisplay.setText(String.format("%d", (int)myAnswer) + operatorBtn.getText());
                            else
                                txtDisplay.setText(String.format("%s", myAnswer) + operatorBtn.getText());

                            b_operator = true;
                            b_lastNumeric = false;
                        } else {
                            // detect division by zero. Display error message
                            txtDisplay.setText("division by zero");
                            b_operator= false;
                            b_lastNumeric = false;
                        }
                    }
                    else {
                        if(Double.toString(myAnswer).equals("Infinity") || Double.toString(myAnswer).equals("NaN")
                                || Double.toString(myAnswer).equals("-Infinity")) {
                            // Display error message
                            txtDisplay.setText("division by zero");
                            b_operator= false;
                            b_lastNumeric = false;
                        } else {
                            // handling "=" operation
                            if((myAnswer == (int)myAnswer))
                                txtDisplay.setText(String.format("%d", (int)myAnswer));
                            else
                                txtDisplay.setText(String.format("%s", myAnswer));
                            b_operator= false;
                            b_lastNumeric = true;
                        }
                    }
                }
                else {
                    // Input error: Do not allow the input key
                }
            }
        }; // end annon inner class

        // Assign the listener to all the numeric buttons
        for(int id : operatorButtons) {
            findViewById(id).setOnClickListener(operatorBtnListener);
        }
    }

    /*
    Function to set OnClickListener for the remaining buttons
    */
    private void setOperatorOnOtherListener() {

        // Create a decimal button listener
        findViewById(R.id.btnDecimal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button decimalBtn = (Button) view;
                String txt = txtDisplay.getText().toString();
                // Handle the decimal button here
                if(b_lastNumeric) {
                    if(!b_operator) {
                        if(txt.indexOf('.') < 0) {
                            txtDisplay.append(decimalBtn.getText());
                            b_lastNumeric = false;
                        }
                    } else {
                        String[] numbers = txt.split("[-+x/]");
                        if(numbers[numbers.length-1].indexOf('.') < 0) {
                            txtDisplay.append(decimalBtn.getText());
                            b_lastNumeric = false;
                        }
                    }
                }
            }
        }); // end annon inner class

        // Create a Neg/Pos button listener
        findViewById(R.id.btnNegPos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button negPosBtn = (Button) view;
                // Handle the Neg/Pos button here
                if(b_lastNumeric) {
                    String txt = txtDisplay.getText().toString();
                    if(!b_operator) {
                        // update the first number
                        if(txt.charAt(0) == '-') {
                            //remove the '-' sign
                            txt = txt.substring(1);
                        } else {
                            //add the '-' sign
                            txt = "-" + txt;
                        }
                    } else {
                        // update the second number
                        StringBuilder str = new StringBuilder(txt);
                        if(txt.indexOf('+') > 0) {
                            // replace "+" to "-"
                            str.setCharAt(txt.indexOf('+'), '-');
                        } else if(txt.lastIndexOf('-') > 0 && txt.indexOf('x') < 0 && txt.indexOf('/') < 0) {
                            // replace "-" to "+"
                            str.setCharAt(txt.lastIndexOf('-'), '+');
                        } else if(txt.indexOf('x') > 0) {
                            if(txt.charAt(txt.indexOf('x')+1) == '-')   // replace "-" to "+"
                                str.deleteCharAt(txt.indexOf('x')+1);
                            else  //replace "+" to "-"
                                str.insert(txt.indexOf('x')+1, '-');
                        } else {
                            if(txt.charAt(txt.indexOf('/')+1) == '-')   // replace "-" to "+"
                                str.deleteCharAt(txt.indexOf('/')+1);
                            else  //replace "+" to "-"
                                str.insert(txt.indexOf('/')+1, '-');
                        }
                        txt = str.toString();
                    }
                    txtDisplay.setText(txt);
                }
            }
        }); // end annon inner class

        // Create a Remove Digit button listener
        findViewById(R.id.btnRemoveDigit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button removeDigitBtn = (Button) view;
                // Handle the Remove Digit button here
                String txt = txtDisplay.getText().toString();
                StringBuilder str = new StringBuilder(txt);

                // Delete the last digit or operation
                if(b_operator) {
                    if(b_lastNumeric && txt.charAt(txt.length()-2) == '-'
                            && ((txt.charAt(txt.length() - 3) == 'x') || (txt.charAt(txt.length()-3) == '/'))) {
                        // delete with "-" "[0-9]"
                        str.setLength(str.length()-2);
                    } else {
                        str.deleteCharAt(txt.length()-1);
                    }
                } else {
                    if(txt.length() > 0) {
                        if(txt.equals("division by zero")) {
                            // Clear the screen
                            str.setLength(0);
                        } else if (txt.charAt(0) == '-' && txt.length() == 2) {
                            // first number: delete the negative first digit
                            str.delete(0,str.length());
                        } else {
                            str.deleteCharAt(txt.length()-1);
                        }
                    }
                }

                 // update the txt view
                 txt = str.toString();
                 txtDisplay.setText(txt);

                 // update the flag status
                 b_lastNumeric = txt.matches("(.*)[0-9]") ? true: false;
                 b_operator = txt.matches("(.*)[+,-,x,/](.*)") ? true: false;
            }
        }); // end annon inner classStringBuilder

        // Create a Clear button listener
        findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button clearBtn = (Button) view;
                // Handle the Clear button here
                txtDisplay.setText("");
                // reset the flags
                b_lastNumeric = false;
                b_operator = false;

            }
        }); // end annon inner class

    }
}
