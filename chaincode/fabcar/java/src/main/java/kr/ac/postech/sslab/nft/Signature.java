package kr.ac.postech.sslab.nft;

import kr.ac.postech.sslab.adapter.IType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

public class Signature implements IType {
    private String hash;
    private boolean activated;
    private boolean freezed;

    public Signature(String xatt) throws ParseException {
        this.parse(xatt);
    }

    private void parse(String xatt) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(xatt);

        this.hash = object.get("hash").toString();
        this.activated = Boolean.parseBoolean(object.get("activated").toString());
        this.freezed = Boolean.parseBoolean(object.get("freezed").toString());
    }

    public void deactivate() {
        this.activated = false;
    }

    private Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("hash", this.hash);
        map.put("activated", Boolean.toString(this.activated));
        map.put("freezed", Boolean.toString(this.freezed));

        return map;
    }

    @Override
    public String toJSONString() {
        return new JSONObject(this.toMap()).toJSONString();
    }
    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return this.hash;
    }
}