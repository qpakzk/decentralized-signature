package kr.ac.postech.sslab.adapter;

import kr.ac.postech.sslab.type.Base;
import kr.ac.postech.sslab.type.IType;
import kr.ac.postech.sslab.type.Document;
import kr.ac.postech.sslab.type.Signature;
import org.json.simple.JSONObject;

import java.util.List;

public class XAttrAdapter implements IXAttr {
    private IType xattr;

    XAttrAdapter(String type) {
        switch (type) {
            case "base":
                this.xattr = new Base();
                break;

            case "doc":
                this.xattr = new Document();
                break;

            case "sig":
                this.xattr = new Signature();
                break;
        }
    }

    public void assign(List<String> args) {
        this.xattr.assign(args);
    }

    public void assign(JSONObject object) {
        this.xattr.assign(object);
    }

    @Override
    public void setXAttr(int index, String attr) {
        this.xattr.setXAttr(index, attr);
    }

    @Override
    public String getXAttr(int index) {
        return this.xattr.getXAttr(index);
    }

    @Override
    public String toJSONString() {
        return this.xattr.toJSONString();
    }
 }