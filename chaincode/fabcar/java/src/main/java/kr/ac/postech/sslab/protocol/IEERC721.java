package kr.ac.postech.sslab.protocol;

import org.hyperledger.fabric.shim.Chaincode.Response;
import org.hyperledger.fabric.shim.ChaincodeStub;
import java.util.List;

public interface IEERC721 {
    public Response initialize(ChaincodeStub stub, List<String> args);
    public Response divide(ChaincodeStub stub, List<String> args);
    public Response delete(ChaincodeStub stub, List<String> args);
    public Response update(ChaincodeStub stub, List<String> args);
    public Response query(ChaincodeStub stub, List<String> args);
    public Response queryTokenHistory(ChaincodeStub stub, List<String> args);
}