package kr.ac.postech.sslab.protocol;

import java.util.HashMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class StandardToken {
    private String tokenId;
    private String owner;
    private String operator;

    public StandardToken(String tokenId, String owner) {
        this.tokenId = tokenId;
        this.owner = owner;
        this.operator = "";
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

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return this.operator;
    }

    public JSONObject constructTokenJSONObject() {
        HashMap<String, String> tokenMap = new HashMap<String, String>();
        tokenMap.put("tokenId", this.tokenId);
        tokenMap.put("owner", this.owner);
        tokenMap.put("operator", this.operator);

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