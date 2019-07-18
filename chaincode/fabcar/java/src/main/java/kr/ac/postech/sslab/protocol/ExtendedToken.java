package kr.ac.postech.sslab.protocol;

import org.json.simple.JSONObject;

public class ExtendedToken {
    String tokenId;
    String tokenType;
    String owner;
    String approved;

    public ExtendedToken(String tokenId, String tokenType) {
        this.tokenId = tokenId;
        this.tokenType = tokenType;
    }
    public JSONObject writeTransfer(String from, String to, String tokenId) {
        
        JSONObject jsonObject = new JSONObject();

        return jsonObject;
    }
}