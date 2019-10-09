package kr.ac.postech.sslab.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Signature implements IType {
    private String hash;
    private boolean activated;

    /*
   attr       | index
   ==================
   hash       | 0
   activated  | 1
   */

    @Override
    public void assign(List<String> args) {
        this.hash = args.get(0);
        this.activated = true;
    }

    @Override
    public void assign(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonString);
        this.hash = node.get("hash").asText();
        this.activated = node.get("activated").asBoolean();
    }

    @Override
    public void setXAttr(int index, String attr) {
        if (index == 1) {
            this.deactivate();
        }
    }

    @Override
    public String getXAttr(int index) {
        switch (index) {
            case 0:
                return this.hash;

            case 1:
                return Boolean.toString(this.activated);
        }

        return null;
    }

    @Override
    public String toJSONString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();

        map.put("hash", this.hash);
        map.put("activated", Boolean.toString(this.activated));

        return mapper.writeValueAsString(map);
    }

    private void deactivate() {
        this.activated = false;
    }
}