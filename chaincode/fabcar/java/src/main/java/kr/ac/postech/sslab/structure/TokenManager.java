package kr.ac.postech.sslab.structure;

import org.hyperledger.fabric.shim.ChaincodeStub;

public class TokenManager implements TokenAdapterInterface {
    private TokenAdapter tokenAdapter;
    private String tokenType;
 
    public TokenManager(String tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public void initialize(ChaincodeStub stub, String tokenId) {
        tokenAdapter = new TokenAdapter(this.tokenType);
        tokenAdapter.initialize(stub, tokenId);
    }
 }