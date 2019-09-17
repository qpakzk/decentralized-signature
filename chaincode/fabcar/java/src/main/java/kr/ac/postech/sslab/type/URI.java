package kr.ac.postech.sslab.type;


import org.json.simple.JSONObject;

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

    public String toJSONString() {
        Map<String, String> map = new HashMap<>();

        map.put("path", this.path);
        map.put("hash", this.hash);

        return new JSONObject(map).toJSONString();
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

    public void assign(JSONObject object) {
        this.path = object.get("path").toString();
        this.hash = object.get("hash").toString();
    }
}
