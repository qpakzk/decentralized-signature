package kr.ac.postech.sslab.adapter;

public interface IXAttr {
    void setXAttr(int index, String attr);
    String getXAttr(int index);
    String toJSONString();
}