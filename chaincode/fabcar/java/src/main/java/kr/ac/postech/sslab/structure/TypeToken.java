package kr.ac.postech.sslab.structure;

import org.hyperledger.fabric.shim.ChaincodeStub;

public interface TypeToken {
    public void initalize(ChaincodeStub stub, String tokenId);
}