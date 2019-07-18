package kr.ac.postech.sslab.structure;

import org.hyperledger.fabric.shim.ChaincodeStub;

public class TokenAdapter implements TokenAdapterInterface {
    private TypeToken token;
 
    public TokenAdapter(String tokenType) {
        if(tokenType.equalsIgnoreCase("DOC")) {
            token = new DocToken();		
            
        }
        else if (tokenType.equalsIgnoreCase("SIG")) {
            token = new SigToken();
        }
    }
 
    @Override
    public void initialize(ChaincodeStub stub, String tokenId) {
        token.initalize(stub, tokenId);
    }
 }