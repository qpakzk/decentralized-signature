package kr.ac.postech.sslab.type;

import java.util.List;

public class Base implements IType {
    private String type;

    @Override
    public void assign(List<String> args) {
        this.type = args.get(0);
    }

    @Override
    public void setXAttr(int index, String attr) {

    }

    @Override
    public String getXAttr(int index) {
        if (index == 0) {
            return this.type;
        }

        return null;
    }

    @Override
    public String toJSONString() {
        return "";
    }
}