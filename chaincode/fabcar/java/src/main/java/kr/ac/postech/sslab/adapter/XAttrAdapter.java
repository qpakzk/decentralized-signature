package kr.ac.postech.sslab.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.ac.postech.sslab.type.IType;
import kr.ac.postech.sslab.type.Document;
import kr.ac.postech.sslab.type.Signature;
import java.util.ArrayList;
import java.util.Map;

public class XAttrAdapter implements IXAttr {
    private IType xattr;

    XAttrAdapter(String type) {
        switch (type) {
            case "doc":
                this.xattr = new Document();
                break;

            case "sig":
                this.xattr = new Signature();
                break;
        }
    }

    public void assign(Map<String, Object> map) {
        this.xattr.assign(map);
    }

    public void assign(ArrayList<String> args) {
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
    public String toJSONString() throws JsonProcessingException {
        return this.xattr.toJSONString();
    }

    @Override
    public Map<String, Object> toMap() {
        return this.xattr.toMap();
    }
 }