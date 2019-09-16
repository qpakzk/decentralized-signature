package kr.ac.postech.sslab.type;

import org.json.simple.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Signature implements IType {
    private String id;
    private String parentId;
    private String type;
    private String hash;
    private boolean activated;

    /*
   attr       | index
   ==================
   type       | 0
   id         | 1
   parentId   | 2
   hash       | 3
   activated  | 4
   */

    @Override
    public void assign(List<String> args) {
        this.type = args.get(0);
        this.id = args.get(1);
        this.parentId = args.get(2);
        this.hash = args.get(3);
        this.activated = true;
    }

    @Override
    public void setXAttr(int index, String attr) {
        if (index == 4) {
            this.deactivate();
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
                return Boolean.toString(this.activated);
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String toJSONString() {
        JSONObject object = new JSONObject();
        object.put("type", this.type);
        object.put("id", this.id);
        object.put("parentId", this.parentId);
        object.put("hash", this.hash);
        object.put("activated", this.activated);

        return object.toJSONString();
    }

    private void deactivate() {
        this.activated = false;
    }
}