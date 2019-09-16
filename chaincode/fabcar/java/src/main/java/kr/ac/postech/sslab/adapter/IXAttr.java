package kr.ac.postech.sslab.adapter;

import java.util.List;

public interface IXAttr {
    void assign(List<String> args);
    void setXAttr(int index, String attr);
    String getXAttr(int index);
    String toJSONString();
}