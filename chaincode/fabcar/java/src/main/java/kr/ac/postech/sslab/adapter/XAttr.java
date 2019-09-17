package kr.ac.postech.sslab.adapter;

import org.json.simple.JSONObject;

import java.util.List;

public class XAttr implements IXAttr {
    private XAttrAdapter adapter;

    @Override
    public void assign(List<String> args) {
        this.adapter = new XAttrAdapter(args);
        this.adapter.assign(args);
    }

    public void assign(JSONObject object, String type) {
        this.adapter = new XAttrAdapter(type);
        this.adapter.assign(object);
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
    public String  toJSONString() {
        return this.adapter.toJSONString();
    }
 }