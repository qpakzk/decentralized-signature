package kr.ac.postech.sslab.type;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

public class Document extends Base {
    private String hash;
    private List<String> signers;
    private List<String> signatures;
    private boolean activated;

    public Document(String xatt) throws ParseException {
        super("doc");

        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(xatt);

        this.hash = object.get("hash").toString();
        this.signers = this.toList(object.get("signers").toString());
        this.signatures = this.toList(object.get("signatures").toString());
        this.activated = true;
    }

    private String toString(List<String> list) {
        String result = "";

        if (list.size() > 0) {
            for (String item : list) {
                result += (item + ",");
            }

            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    private List<String> toList(String string) {
        return new ArrayList<>(Arrays.asList(string.split(",")));
    }

    @Override
    public String toJSONString() {
        Map<String, String> map = new HashMap<>();
        map.put("hash", this.hash);
        map.put("signers", this.toString(signers));
        map.put("signatures", this.toString(signatures));
        map.put("activated", Boolean.toString(this.activated));

        return new JSONObject(map).toJSONString();
    }

    public String getHash() {
        return this.hash;
    }

    public void setSigners(List<String> signers) {
        this.signers = signers;
    }

    public List<String> getSigners() {
        return signers;
    }

    public void setSignatures(List<String> signatures) {
        this.signatures = signatures;
    }

    public List<String> getSignatures() {
        return this.signatures;
    }

    public void deactivate() {
        this.activated = false;
    }

    public boolean isActivated() {
        return this.activated;
    }
}