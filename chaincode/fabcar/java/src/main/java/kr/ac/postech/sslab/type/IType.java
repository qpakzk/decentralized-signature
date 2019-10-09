package kr.ac.postech.sslab.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.List;

public interface IType {
    void assign(List<String> args);
    void assign(String jsonString) throws IOException;
    void setXAttr(int index, String attr);
    String getXAttr(int index);
    String toJSONString() throws JsonProcessingException;
}