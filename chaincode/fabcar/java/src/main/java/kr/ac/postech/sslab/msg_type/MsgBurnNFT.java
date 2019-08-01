package kr.ac.postech.sslab.msg_type;

public class MsgBurnNFT {
    private String sender;
    private String id;

    public MsgBurnNFT(String sender, String id, String denom) {
        this.sender = sender;
        this.id = id;
    }

    public String getSender() {
        return this.sender;
    }

    public String getId() {
        return this.id;
    }
}