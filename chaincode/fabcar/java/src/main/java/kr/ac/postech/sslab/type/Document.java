package kr.ac.postech.sslab.type;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class Document implements IType {
    private String type;
    private String id;
    private String parentId;
    private String hash;
    private List<String> signers;
    private List<String> sigIds;
    private boolean activated;

    /*
    attr       | index
    ==================
    type       | 0
    id         | 1
    parentId   | 2
    hash       | 3
    signers    | 4
    sigIds     | 5
    activated  | 6
    */

    @Override
    public void assign(List<String> args) {
        this.type = args.get(0);
        this.id = args.get(1);
        this.parentId = args.get(2);
        this.hash = args.get(3);
        this.signers = this.toList(args.get(4));
        this.sigIds = new ArrayList<>();
        this.activated = true;
    }

    @Override
    public void setXAttr(int index, String attr) {
        switch (index) {
            case 5:
                this.sigIds.add(attr);
                break;

            case 6:
                this.deactivate();
                break;
        }
    }

    @Override
    public String getXAttr(int index) {
        switch (index) {
            case 0:
                return this.type;

            case 1:
                return this.id;

            case 2:
                return this.parentId;

            case 3:
                return this.hash;

            case 4:
                return this.toString(this.signers);

            case 5:
                return this.toString(this.sigIds);

            case 6:
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
        object.put("type", this.type);
        object.put("id", this.id);
        object.put("parentId", this.parentId);
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