package kr.ac.postech.sslab.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public void assign(Map<String, Object> map) {
        this.activated = (boolean) map.get("activated");
        this.hash = (String) map.get("hash");
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
        return mapper.writeValueAsString(this.toMap());
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("activated", this.activated);
        map.put("hash", this.hash);

        return map;
    }

    private void deactivate() {
        this.activated = false;
    }
}