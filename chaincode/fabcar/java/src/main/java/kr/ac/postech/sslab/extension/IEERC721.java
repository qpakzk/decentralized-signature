package kr.ac.postech.sslab.extension;

import org.hyperledger.fabric.shim.Chaincode.Response;
import org.hyperledger.fabric.shim.ChaincodeStub;
import java.util.List;

public interface IEERC721 {
    public Response mint(ChaincodeStub stub, List<String> args);
    public Response divide(ChaincodeStub stub, List<String> args);
    public Response delete(ChaincodeStub stub, List<String> args);
    public Response updateXAtt(ChaincodeStub stub, List<String> args);
    public Response updateUri(ChaincodeStub stub, List<String> args);
    public Response query(ChaincodeStub stub, List<String> args);
    public Response queryHistory(ChaincodeStub stub, List<String> args);
}