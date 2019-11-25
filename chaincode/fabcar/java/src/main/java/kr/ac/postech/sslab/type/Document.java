package kr.ac.postech.sslab.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
    public void assign(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonString);

        this.activated = node.get("activated").asBoolean();
        this.parent = node.get("parent").asText();

        String childrenJsonArray = node.get("children").asText();
        TypeReference typeArrayList = new TypeReference<ArrayList<String>>() {};
        this.children = mapper.readValue(childrenJsonArray, typeArrayList);

        this.pages = node.get("pages").asInt();
        this.hash = node.get("hash").asText();

        String signersJsonArray = node.get("signers").asText();
        this.signers = mapper.readValue(signersJsonArray, typeArrayList);

        String sigIdsJsonArray = node.get("sigIds").asText();
        this.sigIds = mapper.readValue(sigIdsJsonArray, typeArrayList);
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

        HashMap<String, String> map = new HashMap<>();
        map.put("activated", Boolean.toString(this.activated));
        map.put("parent", this.parent);
        map.put("children", mapper.writeValueAsString(this.children));
        map.put("pages", Integer.toString(this.pages));
        map.put("hash", this.hash);
        map.put("signers", mapper.writeValueAsString(this.signers));
        map.put("sigIds", mapper.writeValueAsString(this.sigIds));

        return mapper.writeValueAsString(map);
    }

    private void deactivate() {
        this.activated = false;
    }
}