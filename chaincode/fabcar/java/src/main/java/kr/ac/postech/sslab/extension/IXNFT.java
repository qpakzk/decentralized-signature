package kr.ac.postech.sslab.extension;

import org.hyperledger.fabric.shim.Chaincode.Response;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.List;

public interface IXNFT {
    Response setXAttr(ChaincodeStub stub, List<String> args);
    Response getXAttr(ChaincodeStub stub, List<String> args);
}
