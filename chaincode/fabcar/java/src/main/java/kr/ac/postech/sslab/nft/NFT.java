package kr.ac.postech.sslab.nft;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.postech.sslab.adapter.XAttr;
import kr.ac.postech.sslab.type.URI;
import org.hyperledger.fabric.shim.ChaincodeStub;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class NFT {
    private String id;
    private String type;
    private String owner;
    private String approvee;
    private XAttr xattr;
    private URI uri;

    public NFT() {}

    private NFT(String id, String type, String owner, String approvee, XAttr xattr, URI uri) {
        this.id = id;
        this.type = type;
        this.owner = owner;
        this.approvee = approvee;
        this.xattr = xattr;
        this.uri = uri;
    }

    public void mint(ChaincodeStub stub, String id, String type, String owner, XAttr xattr, URI uri) throws JsonProcessingException {
        this.id = id;
        this.type = type;
        this.owner = owner;
        this.approvee = "";
        this.xattr = xattr;
        this.uri = uri;

        stub.putStringState(this.id, this.toJSONString());
    }

    public static NFT read(ChaincodeStub stub, String id) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = stub.getStringState(id);
        JsonNode node = mapper.readTree(jsonString);

        String type = node.get("type").asText();
        String owner = node.get("owner").asText();
        String approvee = node.get("approvee").asText();

        XAttr xattr = new XAttr();
        JsonNode xattrNode = node.get("xattr");
        xattr.assign(type, xattrNode.asText());

        URI uri = new URI();
        JsonNode uriNode = node.get("uri");
        uri.assign(uriNode.asText());

        return new NFT(id, type, owner, approvee, xattr, uri);
    }

    public void burn(ChaincodeStub stub, String id) {
        stub.delState(id);
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public void setOwner(ChaincodeStub stub, String owner) throws JsonProcessingException {
        this.owner = owner;
        stub.putStringState(this.id, this.toJSONString());
    }

    public String getOwner() {
        return this.owner;
    }

    public void setApprovee(ChaincodeStub stub, String approvee) throws JsonProcessingException {
        this.approvee = approvee;
        stub.putStringState(this.id, this.toJSONString());
    }

    public String getApprovee() {
        return this.approvee;
    }

    public void setXAttr(ChaincodeStub stub, int index, String attr) throws JsonProcessingException {
        this.xattr.setXAttr(index, attr);
        stub.putStringState(this.id, this.toJSONString());
    }

    public XAttr getXAttr() {
        return this.xattr;
    }

    public String getXAttr(int index) {
        return this.xattr.getXAttr(index);
    }

    public void setURI(ChaincodeStub stub) throws JsonProcessingException {
        stub.putStringState(this.id, this.toJSONString());
    }

    public URI getURI() {
        return this.uri;
    }

    private String toJSONString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();

        map.put("id", this.id);
        map.put("type", this.type);
        map.put("owner", this.owner);
        map.put("approvee", this.approvee);
        map.put("xattr", this.xattr.toJSONString());
        map.put("uri", this.uri.toJSONString());

        return mapper.writeValueAsString(map);
    }
}