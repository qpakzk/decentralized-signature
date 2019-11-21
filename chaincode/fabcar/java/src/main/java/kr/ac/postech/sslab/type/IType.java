package kr.ac.postech.sslab.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.ArrayList;

public interface IType {
    void assign(ArrayList<String> args);
    void assign(String jsonString) throws IOException;
    void setXAttr(int index, String attr);
    String getXAttr(int index);
    String toJSONString() throws JsonProcessingException;
}