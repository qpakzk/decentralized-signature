package kr.ac.postech.sslab.type;

public class Base implements IType {
    private String type;

    public Base() {
        this.type = "base";
    }

    public Base(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String toJSONString() {
        return "";
    }
}