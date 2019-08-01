package kr.ac.postech.sslab.type;

import java.util.List;
import java.util.ArrayList;

public class IDCollection {
    private String denom;
    private List<String> ids;

    public IDCollection(String denom) {
        this.denom = denom;
        this.ids = new ArrayList<String>();
    }

    public String getDenom() {
        return this.denom;
    }

    public void addId(String id) {
        this.ids.add(id);
    }

    public List<String> getId() {
        return this.ids;
    }
}