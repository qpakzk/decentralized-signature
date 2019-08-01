package kr.ac.postech.sslab.type;

import java.util.List;
import java.util.ArrayList;

public class Owner {
    private String addr;
    private List<IDCollection> idCollections;

    public Owner(String addr) {
        this.addr = addr;
        idCollections = new ArrayList<IDCollection>();
    }

    public String getAddr() {
        return this.addr;
    }

    public void addIDCollection(IDCollection idCollection) {
        this.idCollections.add(idCollection);
    }

    public List<IDCollection> getIDCollection() {
        return this.idCollections;
    }
}