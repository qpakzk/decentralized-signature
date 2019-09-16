package kr.ac.postech.sslab.adapter;

import kr.ac.postech.sslab.type.Base;
import kr.ac.postech.sslab.type.IType;
import kr.ac.postech.sslab.type.Document;
import kr.ac.postech.sslab.type.Signature;

import java.util.List;

public class XAttrAdapter implements IXAttr {
    private IType xattr;

    XAttrAdapter(List<String> args) {
        String type = args.get(0);
        switch (type) {
            case "base":
                this.xattr = new Base();
                break;

            case "doc":
                this.xattr = new Document();
                this.xattr.assign(args);
                break;

            case "sig":
                this.xattr = new Signature();
                this.xattr.assign(args);
                break;
        }
    }

    @Override
    public void assign(List<String> args) {
        this.xattr.assign(args);
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