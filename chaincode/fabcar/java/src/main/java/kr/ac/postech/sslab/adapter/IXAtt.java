package kr.ac.postech.sslab.adapter;

import org.json.simple.parser.ParseException;

public interface IXAtt {
    String toJSONString();
    void deactivate();
}