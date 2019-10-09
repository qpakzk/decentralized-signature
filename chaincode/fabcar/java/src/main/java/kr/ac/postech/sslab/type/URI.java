package kr.ac.postech.sslab.type;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class URI {
    private String path;
    private String hash;

    public URI() {
        this.path = "";
        this.hash = "";
    }

    public URI(String path, String hash) {
        this.path = path;
        this.hash = hash;
    }

    public String toJSONString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();

        map.put("path", this.path);
        map.put("hash", this.hash);

        return mapper.writeValueAsString(map);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return this.hash;
    }

    public void assign(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonString);
        this.path = node.get("path").asText();
        this.hash = node.get("hash").asText();
    }
}
