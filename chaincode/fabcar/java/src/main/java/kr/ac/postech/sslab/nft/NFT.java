package kr.ac.postech.sslab.nft;

import kr.ac.postech.sslab.adapter.XAtt;
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
    private String approved;
    private XAtt xatt;
    private URI uri;

    public NFT() {}

    private NFT(String id, String type, String owner, Operator operator, String approved, XAtt xatt, URI uri) {
        this.id = id;
        this.type = type;
        this.owner = owner;
        this.operator = operator;
        this.approved = approved;
        this.xatt = xatt;
        this.uri = uri;
    }

    public void mint(ChaincodeStub stub, String id, String type, String owner, String xatt, String uri) throws ParseException {
        this.id = id;
        this.type = type;
        this.owner = owner;
        this.operator = new Operator();
        this.approved = "";
        this.xatt = new XAtt(xatt, this.type);
        this.uri = new URI(uri);

        stub.putStringState(this.id, this.toJSONString());
    }

    public void burn(ChaincodeStub stub, String tokenId) {
        stub.delState(tokenId);
    }

    public static NFT read(ChaincodeStub stub, String id) throws ParseException {
        JSONObject object = (JSONObject) new JSONParser().parse(stub.getStringState(id));

        String _id = object.get("id").toString();
        String _type = object.get("type").toString();
        String _owner = object.get("owner").toString();
        Operator _operator = Operator.toList(object.get("operator").toString());
        String _approved = object.get("approved").toString();
        XAtt _xatt = new XAtt(object.get("xatt").toString(), _type);
        URI _uri = new URI(object.get("uri").toString());

        return new NFT(_id, _type, _owner, _operator, _approved, _xatt, _uri);
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

    public void setApproved(ChaincodeStub stub, String approved) {
        this.approved = approved;
        stub.putStringState(this.id, this.toJSONString());
    }

    public String getApproved() {
        return this.approved;
    }

    public void setXAtt(ChaincodeStub stub, XAtt xatt) {
        this.xatt = xatt;
        stub.putStringState(this.id, this.toJSONString());
    }

    public XAtt getXAtt() {
        return this.xatt;
    }

    public void setUri(ChaincodeStub stub, URI uri) {
        this.uri = uri;
        stub.putStringState(this.id, this.toJSONString());
    }

    public URI getUri() {
        return this.uri;
    }

    public boolean checker() {
        return this.xatt.checker();
    }

    private String toJSONString() {
        Map<String, String> map = new HashMap<>();
        map.put("id", this.id);
        map.put("type", this.type);
        map.put("owner", this.owner);
        map.put("operator", this.operator.toString());
        map.put("approved", this.approved);
        map.put("xatt", this.xatt.toJSONString());
        map.put("uri", this.uri.toJSONString());

        return new JSONObject(map).toJSONString();
    }
}