package kr.ac.postech.sslab.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.Map;

public class XAttr implements IXAttr {
    private XAttrAdapter adapter;

    public void assign(String type, Map<String, Object> map) {
        this.adapter = new XAttrAdapter(type);
        this.adapter.assign(map);
    }
    public void assign(String type, ArrayList<String> args) {
        this.adapter = new XAttrAdapter(type);
        this.adapter.assign(args);
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

    @Override
    public Map<String, Object> toMap() {
        return this.adapter.toMap();
    }
 }