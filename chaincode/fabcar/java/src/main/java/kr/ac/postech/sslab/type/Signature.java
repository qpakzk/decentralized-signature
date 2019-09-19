package kr.ac.postech.sslab.type;

import org.json.simple.JSONObject;
import java.util.List;

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
    public void assign(JSONObject object)  {
        this.hash = object.get("hash").toString();
        this.activated = Boolean.parseBoolean(object.get("activated").toString());
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
    @SuppressWarnings("unchecked")
    public String toJSONString() {
        JSONObject object = new JSONObject();
        object.put("hash", this.hash);
        object.put("activated", this.activated);

        return object.toJSONString();
    }

    private void deactivate() {
        this.activated = false;
    }
}