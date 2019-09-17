package kr.ac.postech.sslab.type;

import org.json.simple.JSONObject;

import java.util.List;

public interface IType {
    void assign(List<String> args);
    void assign(JSONObject object);
    void setXAttr(int index, String attr);
    String getXAttr(int index);
    String toJSONString();
}