package kr.ac.postech.sslab.type;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

public class Signature extends Base {
    private String hash;
    private boolean activated;

    public Signature(String xatt) throws ParseException {
        super("sig");

        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(xatt);

        this.hash = object.get("hash").toString();
        this.activated = true;
    }

    public String getHash() {
        return this.hash;
    }

    @Override
    public String toJSONString() {
        Map<String, String> map = new HashMap<>();
        map.put("hash", this.hash);

        return new JSONObject(map).toJSONString();
    }

    public boolean isActivated() {
        return this.activated;
    }

    public void deactivate() {
        this.activated = false;
    }
}