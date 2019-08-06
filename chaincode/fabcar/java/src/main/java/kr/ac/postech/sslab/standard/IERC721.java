package kr.ac.postech.sslab.standard;

import org.hyperledger.fabric.shim.Chaincode.Response;
import org.hyperledger.fabric.shim.ChaincodeStub;
import java.util.List;

public interface IERC721 {
    public Response balanceOf(ChaincodeStub stub, List<String> args);
    public Response ownerOf(ChaincodeStub stub, List<String> args);
    public Response transferFrom(ChaincodeStub stub, List<String> args);
    public Response approve(ChaincodeStub stub, List<String> args);
    public Response setApprovalForAll(ChaincodeStub stub, List<String> args);
    public Response getApproved(ChaincodeStub stub, List<String> args);
    public Response isApprovedForAll(ChaincodeStub stub, List<String> args);
}