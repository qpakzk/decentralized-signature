package kr.ac.postech.sslab.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Signature implements IType {
    private boolean activated;
    private String hash;

    /*
   attr       | index
   ==================
   activated  | 0
   hash       | 4
   */

    @Override
    public void assign(ArrayList<String> args) {
        this.activated = true;
        this.hash = args.get(0);
    }

    @Override
    public void assign(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonString);
        this.activated = node.get("activated").asBoolean();
        this.hash = node.get("hash").asText();
    }

    @Override
    public void setXAttr(int index, String attr) {
        if (index == 0) {
            this.deactivate();
        }
    }

    @Override
    public String getXAttr(int index) {
        switch (index) {
            case 0:
                return Boolean.toString(this.activated);

            case 4:
                return this.hash;
        }

        return null;
    }

    @Override
    public String toJSONString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, String> map = new HashMap<>();

        map.put("activated", Boolean.toString(this.activated));
        map.put("hash", this.hash);

        return mapper.writeValueAsString(map);
    }

    private void deactivate() {
        this.activated = false;
    }
}