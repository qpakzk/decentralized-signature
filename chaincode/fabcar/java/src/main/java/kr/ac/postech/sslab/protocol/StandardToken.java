package kr.ac.postech.sslab.protocol;

import org.json.simple.JSONObject;

public class StandardToken {
    private String tokenId;
    private String owner;

    public StandardToken(String tokenId, String owner) {
        this.tokenId = tokenId;
        this.owner = owner;
    }

    public JSONObject constructJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tokenId", tokenId);
        jsonObject.put("owner", owner);
        
        return jsonObject;
    }
}