package kr.ac.postech.sslab.adapter;

import java.util.List;

public class XAttr implements IXAttr {
    private XAttrAdapter adapter;

    @Override
    public void assign(List<String> args) {
        this.adapter = new XAttrAdapter(args);
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