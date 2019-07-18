package kr.ac.postech.sslab.protocol;

import org.hyperledger.fabric.shim.ChaincodeStub;
import java.util.List;

public class EFabNFT extends FabNFT implements EFabNFTInterface {
    private String tokenType;

    public EFabNFT(String tokenType) {
        super();
        this.tokenType = tokenType;
    }

    @Override
    public Response invoke(ChaincodeStub stub) {

        try {
            String func = stub.getFunction();
			List<String> args = stub.getParameters();
			
			if (func.equals("balanceOf")) {
				return super.balanceOf(stub, args);
			}
			else if (func.equals("ownerOf")) {
				return super.ownerOf(stub, args);
			}
			else if(func.equals("transferFrom")) {
				return super.transferFrom(stub, args);
			}
			else if (func.equals("approve")) {
				return super.approve(stub, args);
			}
            else if (func.equals("divide")) {
				return this.divide(stub, args);
			}
			else if (func.equals("delete")) {
				return this.delete(stub, args);
			}
			else if (func.equals("update")) {
				return this.update(stub, args);
			}
			else if (func.equals("query")) {
				return this.query(stub, args);
			}
			else if (func.equals("queryTokenHistory")) {
				return this.queryTokenHistory(stub, args);
			}

			return newErrorResponse("Invalid invoke function name.");
		} catch (Throwable e) {
			return newErrorResponse(e.getMessage());
		}
	}

	@Override
	public Response divide(ChaincodeStub stub, List<String> args) {
		try {
            return newSuccessResponse("Succeeded 'divide' function");
        } catch (Throwable e) {
            return newErrorResponse("Failed 'divide' function");
		}
	}

	@Override
    public Response delete(ChaincodeStub stub, List<String> args) {
		try {
            return newSuccessResponse("Succeeded 'delete' function");
        } catch (Throwable e) {
            return newErrorResponse("Failed 'delete' function");
		}
	}

	@Override
    public Response update(ChaincodeStub stub, List<String> args) {
		try {
            return newSuccessResponse("Succeeded 'update' function");
        } catch (Throwable e) {
            return newErrorResponse("Failed 'update' function");
		}
	}

	@Override
    public Response query(ChaincodeStub stub, List<String> args) {
		try {
            return newSuccessResponse("Succeeded 'query' function");
        } catch (Throwable e) {
            return newErrorResponse("Failed 'query' function");
		}
	}

	@Override
    public Response queryTokenHistory(ChaincodeStub stub, List<String> args) {
		try {
            return newSuccessResponse("Succeeded 'queryTokenHistory' function");
        } catch (Throwable e) {
            return newErrorResponse("Failed 'queryTokenHistory' function");
		}
	}
}