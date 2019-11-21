package kr.ac.postech.sslab.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

public class XAttr implements IXAttr {
    private XAttrAdapter adapter;

    public void assign(String type, List<String> args) {
        this.adapter = new XAttrAdapter(type);
        this.adapter.assign(args);
    }

    public void assign(String type, String jsonString) throws IOException {
        this.adapter = new XAttrAdapter(type);
        this.adapter.assign(jsonString);
    }

    @Override
    public void setXAttr(int index, String attr) {
        this.adapter.setXAttr(index, attr);
    }

    @Override
    public String getXAttr(int index) {
        return this.adapter.getXAttr(index);
    }

    @Override
    public String  toJSONString() throws JsonProcessingException {
        return this.adapter.toJSONString();
    }
 }