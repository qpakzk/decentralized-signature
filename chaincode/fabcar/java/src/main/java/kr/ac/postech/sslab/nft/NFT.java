package kr.ac.postech.sslab.nft;

import kr.ac.postech.sslab.adapter.XAttr;
import kr.ac.postech.sslab.type.Operator;
import kr.ac.postech.sslab.type.URI;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

public class NFT {
    private String id;
    private String type;
    private String owner;
    private Operator operator;
    private String approvee;
    private XAttr xattr;
    private URI uri;

    public NFT() {}

    private NFT(String id, String type, String owner, Operator operator, String approvee, XAttr xattr, URI uri) {
        this.id = id;
        this.type = type;
        this.owner = owner;
        this.operator = operator;
        this.approvee = approvee;
        this.xattr = xattr;
        this.uri = uri;
    }

    public void mint(ChaincodeStub stub, String id, String type, String owner, XAttr xattr, URI uri) {
        this.id = id;
        this.type = type;
        this.owner = owner;
        this.operator = new Operator();
        this.approvee = "";
        this.xattr = xattr;
        this.uri = uri;

        stub.putStringState(this.id, this.toJSONString());
    }

    public void burn(ChaincodeStub stub, String id) {
        stub.delState(id);
    }

    public static NFT read(ChaincodeStub stub, String id) throws ParseException {
        JSONObject object = (JSONObject) new JSONParser().parse(stub.getStringState(id));

        String type = object.get("type").toString();
        String owner = object.get("owner").toString();
        Operator operator = Operator.toList(object.get("operator").toString());
        String approvee = object.get("approvee").toString();
        XAttr xattr = object.get("xattr").toString().equals("") ? new XAttr(type) :  new XAttr(object.get("xattr").toString(), type);
        URI uri = new URI(object.get("uri").toString());

        return new NFT(id, type, owner, operator, approvee, xattr, uri);
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public void setOwner(ChaincodeStub stub, String owner) {
        this.owner = owner;
        stub.putStringState(this.id, this.toJSONString());
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOperator(ChaincodeStub stub, Operator operator) {
        this.operator = operator;
        stub.putStringState(this.id, this.toJSONString());
    }

    public Operator getOperator() {
        return this.operator;
    }

    public void setApprovee(ChaincodeStub stub, String approvee) {
        this.approvee = approvee;
        stub.putStringState(this.id, this.toJSONString());
    }

    public String getApprovee() {
        return this.approvee;
    }

    public void setXAttr(ChaincodeStub stub, XAttr xattr) {
        this.xattr = xattr;
        stub.putStringState(this.id, this.toJSONString());
    }

    public XAttr getXAttr() {
        return this.xattr;
    }

    public void setURI(ChaincodeStub stub, int index, String attribute) throws Throwable {
        switch (index) {
            case 0:
                this.uri.setPath(attribute);
                break;

            case 1:
                this.uri.setHash(attribute);
                break;

            default:
                throw new Throwable("Incorrect index. Expecting 0 or 1");
        }

        stub.putStringState(this.id, this.toJSONString());
    }

    public URI getURI() {
        return this.uri;
    }

    public String getURI(int index) throws Throwable {
        switch (index) {
            case 0:
                return uri.getPath();

            case 1:
                return uri.getHash();

            default:
                throw new Throwable("Incorrect index. Expecting 0 or 1");
        }
    }

    private String toJSONString() {
        Map<String, String> map = new HashMap<>();
        map.put("id", this.id);
        map.put("type", this.type);
        map.put("owner", this.owner);
        map.put("operator", this.operator.toString());
        map.put("approvee", this.approvee);
        map.put("xattr", this.xattr.toJSONString());
        map.put("uri", this.uri.toJSONString());

        return new JSONObject(map).toJSONString();
    }
}