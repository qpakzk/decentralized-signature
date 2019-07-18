package kr.ac.postech.sslab.structure;

import org.hyperledger.fabric.shim.ChaincodeStub;
import org.json.simple.JSONObject;

public class DocToken implements TypeToken {
    private String tokenType;

    //Token class fields
    private String tokenId;
    private String owner;
    private String operator;

    //document token fields
    private String hash;
    private String[] sigs;

    public DocToken() {
        this.tokenType = "DOC";
    }

    @Override
    public void initalize(ChaincodeStub stub, String tokenId) {
        String tokenString = stub.getStringState(tokenId);
        Token token = Token.retrieveToken(tokenString);
        
        this.tokenId = token.getTokenId();
        this.owner = token.getOwner();
        this.operator = token.getOperator();
    }
}