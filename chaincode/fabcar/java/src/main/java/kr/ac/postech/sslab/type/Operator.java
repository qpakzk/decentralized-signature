package kr.ac.postech.sslab.type;

import org.json.simple.JSONArray;
import java.util.ArrayList;
import java.util.List;

public class Operator {
    private List<String> operators;

    public Operator() {
        this.operators = new ArrayList<>();
    }

    private Operator(List<String> operators) {
        this.operators = operators;
    }

    @SuppressWarnings("unchecked")
    public JSONArray toJSONArray() {
        JSONArray array = new JSONArray();
        array.addAll(this.operators);

        return array;
    }

    public static Operator toList(JSONArray array) {
        Operator operator = new Operator();
        for (Object object : array) {
            operator.operators.add(object.toString());
        }

        return operator;
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
