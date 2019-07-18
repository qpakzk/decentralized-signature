package kr.ac.postech.sslab.structure;

import org.hyperledger.fabric.shim.ChaincodeStub;

public class SigToken  implements TypeToken {
    private String tokenType;

    //Token class fields
    private String tokenId;
    private String owner;
    private String operator;

    //signature token fields
    private String hash;
    public SigToken() {
        this.tokenType = "SIG";
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