package kr.ac.postech.sslab.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import kr.ac.postech.sslab.type.Base;
import kr.ac.postech.sslab.type.IType;
import kr.ac.postech.sslab.type.Document;
import kr.ac.postech.sslab.type.Signature;

import java.io.IOException;
import java.util.List;

public class XAttrAdapter implements IXAttr {
    private IType xattr;

    XAttrAdapter(String type) {
        switch (type) {
            case "base":
            case "erc721":
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

    public void assign(String jsonString) throws IOException {
        this.xattr.assign(jsonString);
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
 }