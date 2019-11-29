package kr.ac.postech.sslab.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Document implements IType {
    private boolean activated;
    private String parent;
    private ArrayList<String> children;
    private int pages;

    private String hash;
    private ArrayList<String> signers;
    private ArrayList<String> sigIds;

    /*
    attr       | index
    ==================
    activated  | 0
    parent     | 1
    children   | 2
    pages      | 3
    hash       | 4
    signers    | 5
    sigIds     | 6
    */

    @Override
    public void assign(ArrayList<String> args) {
        this.activated = true;
        this.parent = "";
        this.children = new ArrayList<>();
        this.pages = Integer.parseInt(args.get(0));
        this.hash = args.get(1);
        this.signers = this.toList(args.get(2));
        this.sigIds = new ArrayList<>();
    }

    @Override
    public void assign(Map<String, Object> map) {
        this.activated = (boolean) map.get("activated");
        this.parent = (String) map.get("parent");
        this.children = (ArrayList<String>) map.get("children");
        this.pages = (int) map.get("pages");
        this.hash = (String) map.get("hash");
        this.signers = (ArrayList<String>) map.get("signers");
        this.sigIds = (ArrayList<String>) map.get("sigIds");
    }

    @Override
    public void setXAttr(int index, String attr) {
        switch (index) {
            case 0:
                this.deactivate();
                break;

            case 1:
                this.parent = attr;
                break;

            case 2:
                this.children = this.toList(attr);
                break;

            case 3:
                this.pages = Integer.parseInt(attr);
                break;

            case 6:
                this.sigIds.add(attr);
                break;
        }
    }

    @Override
    public String getXAttr(int index) {
        switch (index) {
            case 0:
                return Boolean.toString(this.activated);

            case 1:
                return this.parent;

            case 2:
                return this.children.toString();

            case 3:
                return Integer.toString(this.pages);

            case 4:
                return this.hash;

            case 5:
                return this.signers.toString();

            case 6:
                return this.sigIds.toString();

        }

        return null;
    }

    private ArrayList<String> toList(String string) {
        return new ArrayList<>(Arrays.asList(string.split(",")));
    }

    @Override
    public String toJSONString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this.toMap());
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("activated", this.activated);
        map.put("parent", this.parent);
        map.put("children", this.children);
        map.put("pages", this.pages);
        map.put("hash", this.hash);
        map.put("signers", this.signers);
        map.put("sigIds", this.sigIds);

        return map;
    }

    private void deactivate() {
        this.activated = false;
    }
}