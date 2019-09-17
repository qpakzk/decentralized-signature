package kr.ac.postech.sslab.type;

import org.json.simple.JSONObject;

import java.util.List;

public class Base implements IType {
    @Override
    public void assign(List<String> args) {
    }

    @Override
    public void assign(JSONObject object) {

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
}