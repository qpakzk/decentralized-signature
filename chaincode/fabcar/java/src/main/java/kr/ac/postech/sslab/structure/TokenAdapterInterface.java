package kr.ac.postech.sslab.structure;

import org.hyperledger.fabric.shim.ChaincodeStub;

public interface TokenAdapterInterface {
    public void initialize(ChaincodeStub stub, String tokenId);
}