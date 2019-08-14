package kr.ac.postech.sslab.nft;

import kr.ac.postech.sslab.adapter.ITypeNFT;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BaseNFT implements ITypeNFT {
    private String id;
    private String owner;
    private String operator;
    private String uri;

    @Override
    public void mint(ChaincodeStub stub, String id, String owner, String uri) {
        this.id = id;
        this.owner = owner;
        this.operator = "";
        this.uri = uri == null ? "" : uri;

        stub.putStringState(this.id, this.toJSONString());
    }

    @Override
    public void burn(ChaincodeStub stub, String id) {
        stub.delState(id);
    }

    @Override
    public String read(ChaincodeStub stub, String id) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(stub.getStringState(id));

        this.id = object.get("id").toString();
        this.owner = object.get("owner").toString();
        this.operator = object.get("operator").toString();
        this.uri = object.get("uri").toString();

        return this.toJSONString();
    }

    @Override
    public void setOwner(ChaincodeStub stub, String owner) {
        this.owner = owner;
        stub.putStringState(this.id, this.toJSONString());
    }

    @Override
    public String getOwner() {
        return this.owner;
    }

    @Override
    public void setOperator(ChaincodeStub stub, String operator) {
        this.operator = operator;
        stub.putStringState(this.id, this.toJSONString());
    }

    @Override
    public String getOperator() {
        return this.operator;
    }


    @Override
    public void setUri(ChaincodeStub stub, String uri) {
        this.uri = uri;
        stub.putStringState(this.id, this.toJSONString());
    }

    @Override
    public String getUri() {
        return this.uri;
    }

    private String toJSONString() {
        return "{\"id\":\"" + this.id + "\","
                + "\"owner\":\"" + this.owner + "\","
                + "\"operator\":\"" + this.operator + "\","
                + "\"uri\":\"" + this.uri + "\"}";
    }
}