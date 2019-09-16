package kr.ac.postech.sslab.type;

import java.util.List;

public interface IType {
    void assign(List<String> args);
    void setXAttr(int index, String attr);
    String getXAttr(int index);
    String toJSONString();
}