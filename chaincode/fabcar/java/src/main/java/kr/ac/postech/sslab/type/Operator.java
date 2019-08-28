package kr.ac.postech.sslab.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Operator {
    List<String> operators;

    public Operator() {
        this.operators = new ArrayList<>();
    }

    public Operator(List<String> operators) {
        this.operators = operators;
    }

    @Override
    public String toString() {
        String result = "";

        if(this.operators.size() > 0) {
            for (String operator : this.operators) {
                result += (operator + ",");
            }

            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    public static Operator toList(String string) {
        return new Operator(new ArrayList<>(Arrays.asList(string.split(","))));
    }

    public Operator add(String operator) {
        if (!existOperator(operator)) {
            this.operators.add(operator);
        }
        return new Operator(this.operators);
    }

    public Operator remove(String operator) {
        this.operators.remove(operator);
        return new Operator(this.operators);
    }

    public boolean existOperator(String inputOperator) {
        for (String operator : this.operators) {
            if (operator.equals(inputOperator)) {
                return true;
            }
        }

        return false;
    }
}
