package kr.ac.postech.sslab.protocol;

import java.util.HashMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class StandardToken {
    private String tokenId;
    private String owner;

    public StandardToken(String tokenId, String owner) {
        this.tokenId = tokenId;
        this.owner = owner;
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return this.owner;
    }

    public JSONObject constructTokenJSONObject() {
        HashMap<String, String> tokenMap = new HashMap<String, String>();
        tokenMap.put("tokenId", this.tokenId);
        tokenMap.put("owner", this.owner);

        JSONObject jsonObject = new JSONObject(tokenMap);
        
        return jsonObject;
    }

    public static StandardToken retrieveToken(String jsonString) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonString);

            String tokenId = String.valueOf(jsonObject.get("tokenId"));
            String owner = String.valueOf(jsonObject.get("owner"));

            return new StandardToken(tokenId, owner);
        } catch(Throwable e) {
            e.printStackTrace(System.out);
            return null;
        }
    }    
}