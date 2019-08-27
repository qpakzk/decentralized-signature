package kr.ac.postech.sslab.adapter;

import kr.ac.postech.sslab.nft.*;
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

    @Override
    public void deactivate() {
        this.type.deactivate();
    }
 }