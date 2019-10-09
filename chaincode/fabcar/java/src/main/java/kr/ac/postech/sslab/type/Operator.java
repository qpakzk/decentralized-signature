package kr.ac.postech.sslab.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Operator {
    private List<String> operators;

    public Operator() {
        this.operators = new ArrayList<>();
    }

    public String toJSONArray() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this.operators);
    }

    public static Operator toList(String jsonArray) throws IOException {
        Operator operator = new Operator();
        ObjectMapper mapper = new ObjectMapper();

        operator.setOperators(mapper.readValue(jsonArray, List.class));
        return operator;
    }

    private void setOperators(List<String> operators) {
        this.operators = operators;
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
