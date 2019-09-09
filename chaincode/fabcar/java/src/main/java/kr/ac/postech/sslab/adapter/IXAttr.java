package kr.ac.postech.sslab.adapter;

import org.json.simple.parser.ParseException;

public interface IXAttr {
    String toJSONString();
    void deactivate();
}