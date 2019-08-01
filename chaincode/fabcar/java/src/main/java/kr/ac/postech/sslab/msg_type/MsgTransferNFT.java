package kr.ac.postech.sslab.msg_type;

public class MsgTransferNFT {
    private String sender;
    private String recipient;
    private String id;

    public MsgTransferNFT(String sender, String recipient, String id) {
        this.sender = sender;
        this.recipient = recipient;
        this.id = id;
    }

    public String getSender() {
        return this.sender;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public String getId() {
        return this.id;
    }
}