package kr.ac.postech.sslab.msg_type;

public class MsgMintNFT {
    private String hash;
    private String uri;

    public MsgMintNFT(String hash, String uri) {
        this.hash = hash;
        this.uri = uri;
    }

    public String getHash() {
        return this.hash;
    }

    public String getUri() {
        return this.uri;
    }
}