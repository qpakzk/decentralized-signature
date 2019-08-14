package kr.ac.postech.sslab.adapter;

import org.hyperledger.fabric.shim.ChaincodeStub;
import org.json.simple.parser.ParseException;

public interface ITypeNFT {
    void mint(ChaincodeStub stub, String id, String owner, String uri);
    void burn(ChaincodeStub stub, String id);
    String read(ChaincodeStub stub, String id) throws ParseException;
    void setOwner(ChaincodeStub stub, String owner);
    String getOwner();
    void setOperator(ChaincodeStub stub, String operator);
    String getOperator();
    void setUri(ChaincodeStub stub, String uri);
    String getUri();
}