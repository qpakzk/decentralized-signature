package kr.ac.postech.sslab.type;

import java.util.ArrayList;
import java.util.Map;

public class Base implements IType {
    @Override
    public void assign(ArrayList<String> args) {
    }

    @Override
    public void assign(Map<String, Object> map) {

    }

    @Override
    public void setXAttr(int index, String attr) {

    }

    @Override
    public String getXAttr(int index) {
        return null;
    }

    @Override
    public String toJSONString() {
        return "";
    }

    @Override
    public Map<String, Object> toMap() {
        return null;
    }
}