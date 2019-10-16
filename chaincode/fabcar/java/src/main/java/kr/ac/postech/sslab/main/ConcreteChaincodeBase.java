package kr.ac.postech.sslab.main;

import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.List;

public class ConcreteChaincodeBase extends ChaincodeBase {
    @Override
    public Response init(ChaincodeStub stub) {
        try {
            String func = stub.getFunction();

            if (!func.equals("init")) {
                throw new Throwable("FAILURE");
            }

            List<String> args = stub.getParameters();
            if (args.size() != 0) {
                throw new Throwable("FAILURE");
            }

            return newSuccessResponse();
        } catch (Throwable throwable) {
            return newErrorResponse("FAILURE");
        }
    }

    @Override
    public Response invoke(ChaincodeStub stub) {
        return newSuccessResponse("SUCCESS");
    }
}
