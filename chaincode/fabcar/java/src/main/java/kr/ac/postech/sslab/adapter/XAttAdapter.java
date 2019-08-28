package kr.ac.postech.sslab.adapter;

import kr.ac.postech.sslab.type.Base;
import kr.ac.postech.sslab.type.IType;
import kr.ac.postech.sslab.type.Document;
import kr.ac.postech.sslab.type.Signature;
import org.json.simple.parser.ParseException;

public class XAttAdapter implements IXAtt {
    private IType type;
 
    public XAttAdapter(String type, String xatt) throws ParseException {
        switch (type) {
            case "doc":
                this.type = new Document(xatt);
                break;

            case "sig":
                this.type = new Signature(xatt);
                break;

            case "base":
                this.type = new Base();
                break;

            default:
                this.type = null;
                break;
        }
    }

    @Override
    public String toJSONString() {
        return this.type.toJSONString();
    }

    boolean isActivated() {
        switch (this.type.getType()) {
            case "doc":
                return ((Document) this.type).isActivated();

            case "sig":
                return ((Signature) this.type).isActivated();
        }

        return true;
    }

    @Override
    public void deactivate() {
        switch (this.type.getType()) {
            case "doc":
                ((Document) this.type).deactivate();
                break;

            case "sig":
                ((Signature) this.type).deactivate();
                break;
        }
    }
 }