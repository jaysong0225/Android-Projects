package com.example.w0302272.mathcalculator;

/**
 * Created by Jay on 2017-09-24.
 */

public class myCalculator {
    public double requestCalculation(String strQuery) {
        // split first number, operator, second number
        String[] component = findComponents(strQuery);

        // Do calculation accordingly with the operator
        if(component[1] == "+")
            return calcAddition(Double.parseDouble(component[0]), Double.parseDouble(component[2]));
        else if(component[1] == "-")
            return calcSubtraction(Double.parseDouble(component[0]), Double.parseDouble(component[2]));
        else if(component[1] == "x")
            return calcMultiplication(Double.parseDouble(component[0]), Double.parseDouble(component[2]));
        else
            return calcDivision(Double.parseDouble(component[0]), Double.parseDouble(component[2]));
    }

    /*
        function to decompose the inputs
        return a String array of {firstNum, operator, secondNum}
    */
    private String[] findComponents(String input) {
        String[] values = new String[3];
        boolean bIsFirstNumNeg = false;
        boolean bIsSecondNumNeg = false;

        // split each component
        String[] components = input.split("[-+x/]");
        // decide +/- sign
        if(components[0].length() == 0 && components[components.length-2].length() == 0) {
            bIsFirstNumNeg = true;
            bIsSecondNumNeg = true;
        } else if(components[0].length() == 0 && components[components.length-2].length() != 0) {
            bIsFirstNumNeg = true;
        } else if(components[0].length() != 0 && components[components.length-2].length() == 0) {
            bIsSecondNumNeg = true;
        } else {
            /* Do nothing. Both are positive */
        }

        // get the Numbers (firstNum, secondNum)
        if(bIsFirstNumNeg && bIsSecondNumNeg) {
            values[0] = "-" + components[1];
            values[2] = "-" + components[3];
        } else if(bIsFirstNumNeg && !bIsSecondNumNeg) {
            values[0] = "-" + components[1];
            values[2] = components[2];
        } else if(!bIsFirstNumNeg && bIsSecondNumNeg) {
            values[0] = components[0];
            values[2] = "-" + components[2];
        } else {
            values[0] = components[0];
            values[2] = components[1];
        }

        // get the Operator
        if(input.indexOf('+') > 0)
            values[1] = "+";
        else if(input.indexOf('x') > 0)
            values[1] = "x";
        else if(input.indexOf('/') > 0)
            values[1] = "/";
        else
            values[1] = "-";

        return values;
    }

    // function for addition
    private double calcAddition(double a, double b) {
        return (a+b);
    }

    // function for subtraction
    private double calcSubtraction(double a, double b) {
        return (a-b);
    }

    // function for multiplication
    private double calcMultiplication(double a, double b) {
        return (a*b);
    }

    // function for division
    private double calcDivision(double a, double b) {
        return (a/b);
    }
}
