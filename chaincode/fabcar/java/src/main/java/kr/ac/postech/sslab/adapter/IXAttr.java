package kr.ac.postech.sslab.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IXAttr {
    void setXAttr(int index, String attr);
    String getXAttr(int index);
    String toJSONString() throws JsonProcessingException;
}