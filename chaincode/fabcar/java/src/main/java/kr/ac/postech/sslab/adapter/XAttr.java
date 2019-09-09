package kr.ac.postech.sslab.adapter;

import org.json.simple.parser.ParseException;

public class XAttr implements IXAttr {
    private XAttrAdapter adapter;

    public XAttr(String xatt, String type) throws ParseException {
        this.adapter = new XAttrAdapter(type, xatt);
    }

    public XAttr(String type) {
        this.adapter = new XAttrAdapter(type);
    }

    @Override
    public String  toJSONString() {
        return this.adapter.toJSONString();
    }

    @Override
    public void deactivate() {
        this.adapter.deactivate();
    }

    public boolean checker() {
        return this.adapter.isActivated();
    }
 }