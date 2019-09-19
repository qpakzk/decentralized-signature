package kr.ac.postech.sslab.type;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class Document implements IType {
    private String hash;
    private List<String> signers;
    private List<String> sigIds;
    private boolean activated;

    /*
    attr       | index
    ==================
    hash       | 0
    signers    | 1
    sigIds     | 2
    activated  | 3
    */

    @Override
    public void assign(List<String> args) {
        this.hash = args.get(0);
        this.signers = this.toList(args.get(1));
        this.sigIds = new ArrayList<>();
        this.activated = true;
    }

    public void assign(JSONObject object) {
        this.hash = object.get("hash").toString();

        JSONArray signers = (JSONArray) object.get("signers");
        this.signers = new ArrayList<>();
        for (Object signer : signers) {
            this.signers.add((String) signer);
        }

        JSONArray sigIds = (JSONArray) object.get("sigIds");
        this.sigIds = new ArrayList<>();
        for (Object sigId : sigIds) {
            this.sigIds.add((String) sigId);
        }

        this.activated = Boolean.parseBoolean(object.get("activated").toString());
    }

    @Override
    public void setXAttr(int index, String attr) {
        switch (index) {
            case 2:
                this.sigIds.add(attr);
                break;

            case 3:
                this.deactivate();
                break;
        }
    }

    @Override
    public String getXAttr(int index) {
        switch (index) {
            case 0:
                return this.hash;

            case 1:
                return this.toString(this.signers);

            case 2:
                return this.toString(this.sigIds);

            case 3:
                return Boolean.toString(this.activated);
        }

        return null;
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
    @SuppressWarnings("unchecked")
    public String toJSONString() {
        JSONObject object = new JSONObject();
        object.put("hash", this.hash);

        JSONArray signers = new JSONArray();
        signers.addAll(this.signers);
        object.put("signers", signers);

        JSONArray sigIds = new JSONArray();
        sigIds.addAll(this.sigIds);
        object.put("sigIds", sigIds);

        object.put("activated", Boolean.toString(this.activated));

        return object.toJSONString();
    }

    private void deactivate() {
        this.activated = false;
    }
}