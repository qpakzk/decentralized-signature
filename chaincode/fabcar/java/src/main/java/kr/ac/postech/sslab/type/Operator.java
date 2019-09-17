package kr.ac.postech.sslab.type;

import org.json.simple.JSONArray;
import java.util.ArrayList;
import java.util.List;

public class Operator {
    private List<String> operators;

    public Operator() {
        this.operators = new ArrayList<>();
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

    public void add(String operator) {
        if (!existOperator(operator)) {
            this.operators.add(operator);
        }
    }

    public void remove(String operator) {
        this.operators.remove(operator);
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
