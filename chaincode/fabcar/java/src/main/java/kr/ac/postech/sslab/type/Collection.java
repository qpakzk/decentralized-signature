package kr.ac.postech.sslab.type;

import java.util.List;
import java.util.ArrayList;

public class Collection {
    private String denom;
    private List<NFT> nfts;

    public Collection(String denom) {
        this.denom = denom;
        this.nfts = new ArrayList<NFT>();
    }

    public String getDenom() {
        return this.denom;
    }

    public void addNFT(NFT nft) {
        this.nfts.add(nft);
    }

    public List<NFT> getNFT() {
        return this.nfts;
    }
}