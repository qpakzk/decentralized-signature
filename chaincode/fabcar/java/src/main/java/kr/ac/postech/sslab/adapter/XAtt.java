package kr.ac.postech.sslab.adapter;

import org.json.simple.parser.ParseException;

public class XAtt implements IXAtt {
    private XAttAdapter adapter;

    public XAtt(String xatt, String type) throws ParseException {
        this.adapter = new XAttAdapter(type, xatt);
    }

    @Override
    public String  toJSONString() {
        return this.adapter.toJSONString();
    }

    @Override
    public void deactivate() {
        this.adapter.deactivate();
    }
 }