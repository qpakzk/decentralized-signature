package kr.ac.postech.sslab.msg_type;

public class MsgEditNFTMetadata {
    private String owner;
    private String operator;
    private String id;
    private String hash;
    private String uri;
    private int params;

    public MsgEditNFTMetadata(String owner, String operator, String id, String hash, String uri) {
        this.owner = owner;
        this.operator = operator;
        this.id = id;
        this.hash = hash;
        this.uri = uri;
    }

    public MsgEditNFTMetadata(String operator, String id) {
        this.params = 2;
        this.operator = operator;
        this.id = id;
    }

    public MsgEditNFTMetadata(String id, String hash, String uri) {
        this.params = 3;
        this.id = id;
        this.hash = hash;
        this.uri = uri;
    }

    public String getOwner() {
        return this.owner;
    }

    public String getOperator() {
        return this.operator;
    }

    public String getId() {
        return this.id;
    }

    public String getHash() {
        return this.hash;
    }

    public String getUri() {
        return this.uri;
    }

    public int getParams() {
        return this.params;
    }
}