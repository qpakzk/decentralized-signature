package kr.ac.postech.sslab.type;

import java.util.HashMap;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class NFT {
    private String id;
    private String owner;
    private String operator;
    private String hash;
    private String uri;
    private boolean isActivated;

    private JSONObject constructNFTJSON() {
        HashMap<String, String> nftMap = new HashMap<>();
        nftMap.put("id", this.id);
        nftMap.put("owner", this.owner);
        nftMap.put("operator", this.operator);
        nftMap.put("hash", this.hash);
        nftMap.put("uri", this.uri);
        nftMap.put("isActivated", String.valueOf(isActivated));

        return new JSONObject(nftMap);
    }

    public String stringNFTJSON() {
        JSONObject nftJSON = this.constructNFTJSON();

        return String.valueOf(nftJSON);
    }

    private void mint(ChaincodeStub stub, String id, String hash, String uri) {
        this.id = id;
        this.owner = "";
        this.operator = "";
        this.hash = hash;
        this.uri = uri;
        this.isActivated = true;

        this.store(stub);
    }

    public void burn(ChaincodeStub stub, String id) {
        stub.delState(id);
    }

    public NFT(ChaincodeStub stub, String id, String hash, String uri) {
        this.mint(stub, id, hash, uri);
    }

    public NFT() {

    }

    public String getId() {
        return this.id;
    }

    public void setOwner(ChaincodeStub stub, String owner) {
        this.owner = owner;
        this.store(stub);
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOperator(ChaincodeStub stub, String operator) {
        this.operator = operator;
        this.store(stub);
    }

    public String getOperator() {
        return this.operator;
    }

    public String getHash() {
        return this.hash;
    }

    public String getUri() {
        return this.uri;
    }

    public boolean getIsActivated() {
        return this.isActivated;
    }

    public void setIsActivated(ChaincodeStub stub, boolean isActivated) {
        this.isActivated = false;
        this.store(stub);
    }

    public void editMetadata(ChaincodeStub stub, String hash, String uri) {
        this.hash = hash;
        this.uri = uri;
        this.store(stub);
    }

    private void store(ChaincodeStub stub) {
        String nftString = this.stringNFTJSON();
        stub.putStringState(this.id, nftString);
    }

    private boolean load(ChaincodeStub stub, String id) {
        try {
            String nftString = stub.getStringState(id);
            JSONParser parser = new JSONParser();
            JSONObject nftJSON = (JSONObject) parser.parse(nftString);

            this.id = String.valueOf(nftJSON.get("id"));
            this.owner = String.valueOf(nftJSON.get("owner"));
            this.operator = String.valueOf(nftJSON.get("operator"));
            this.hash = String.valueOf(nftJSON.get("hash"));
            this.uri = String.valueOf(nftJSON.get("uri"));
            this.isActivated =  (Boolean) nftJSON.get("isActivated");

            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public static NFT retrieve(ChaincodeStub stub, String id) {
        NFT nft = new NFT();
        boolean isLoaded = nft.load(stub, id);
        if(isLoaded) {
            return nft;
        }
        else {
            return null;
        }
    }
}