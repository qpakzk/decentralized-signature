package kr.ac.postech.sslab.nft;

import kr.ac.postech.sslab.adapter.IType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

public class Document implements IType {
    private String hash;
    private List<String> signers;
    private List<String> signatures;
    private boolean activated;
    private boolean freezed;

    public Document(String xatt) throws ParseException {
        this.parse(xatt);
    }

    private void parse(String xatt) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(xatt);

        this.hash = object.get("hash").toString();
        this.signers = this.toList(object.get("signers").toString());
        this.signatures = this.toList(object.get("signatures").toString());
        this.activated = Boolean.parseBoolean(object.get("activated").toString());
        this.freezed = Boolean.parseBoolean(object.get("freezed").toString());
    }

    private Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("hash", this.hash);
        map.put("signers", this.toString(signers));
        map.put("signatures", this.toString(signatures));
        map.put("activated", Boolean.toString(this.activated));
        map.put("freezed", Boolean.toString(this.freezed));

        return map;
    }

    private String toString(List<String> list) {
        String result = "";

        if (list.size() > 0) {
            for (String item : list) {
                result = result + item + ",";
            }

            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    public List<String> toList(String string) {
        return new ArrayList<>(Arrays.asList(string.split(",")));
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

    public void freeze() {
        this.freezed = true;
    }
}