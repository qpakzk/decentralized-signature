package kr.ac.postech.sslab.extension;

import org.hyperledger.fabric.shim.Chaincode.Response;
import org.hyperledger.fabric.shim.ChaincodeStub;
import java.util.List;

public interface IEERC721 {
    Response mint(ChaincodeStub stub, List<String> args);
    Response divide(ChaincodeStub stub, List<String> args);
    Response deactivate(ChaincodeStub stub, List<String> args);
    Response setXAtt(ChaincodeStub stub, List<String> args);
    Response getXAtt(ChaincodeStub stub, List<String> args);
    Response setUri(ChaincodeStub stub, List<String> args);
    Response getUri(ChaincodeStub stub, List<String> args);
    Response query(ChaincodeStub stub, List<String> args);
    Response queryHistory(ChaincodeStub stub, List<String> args);
    Response burn(ChaincodeStub stub, List<String> args);
}