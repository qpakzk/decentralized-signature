package kr.ac.postech.sslab.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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

    @Override
    public void assign(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonString);
        this.hash = node.get("hash").asText();

        String signersJsonArray = node.get("signers").asText();
        this.signers = mapper.readValue(signersJsonArray, List.class);

        String sigIdsJsonArray = node.get("sigIds").asText();
        this.sigIds = mapper.readValue(sigIdsJsonArray, ArrayList.class);

        this.activated = node.get("activated").asBoolean();
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
    public String toJSONString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, String> map = new HashMap<>();
        map.put("hash", this.hash);
        map.put("signers", mapper.writeValueAsString(this.signers));
        map.put("sigIds", mapper.writeValueAsString(this.sigIds));
        map.put("activated", Boolean.toString(this.activated));

        return mapper.writeValueAsString(map);
    }

    private void deactivate() {
        this.activated = false;
    }
}