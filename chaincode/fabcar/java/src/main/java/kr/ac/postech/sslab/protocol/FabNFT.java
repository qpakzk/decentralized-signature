package kr.ac.postech.sslab.protocol;

import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class FabNFT extends ChaincodeBase {

	long tokensCount;
	Map<String, String> tokenOwner;
	Map<String, String> tokenApprovals;
	Map<String, Long> ownedTokensCount;

	public FabNFT () {
		tokensCount = 0;
		tokenOwner = new HashMap<String, String>();
		tokenApprovals = new HashMap<String, String>();
		ownedTokensCount = new HashMap<String, Long>();
	}

    @Override
    public Response init(ChaincodeStub stub) {

    	try {
            return newSuccessResponse("Succeeded 'init' function");
        } catch (Throwable e) {
            return newErrorResponse("Failed 'init' function");
		}
    }

    @Override
    public Response invoke(ChaincodeStub stub) {

        try {
            String func = stub.getFunction();
			List<String> args = stub.getParameters();

			if (func.equals("balanceOf")) {
				return balanceOf(stub, args);
			}
			else if (func.equals("ownerOf")) {
				return ownerOf(stub, args);
			}
			else if (func.equals("safeTransferFrom")) {
				return safeTransferFrom(stub, args);
			}
            else if (func.equals("transferFrom")) {
				return transferFrom(stub, args);
			}
			else if (func.equals("approve")) {
				return approve(stub, args);
			}
			else if (func.equals("setApprovalForAll")) {
				return setApprovalForAll(stub, args);
			}
			else if (func.equals("getApproved")) {
				return getApproved(stub, args);
			}
			else if (func.equals("isApprovedForAll")) {
				return isApprovedForAll(stub, args);
			}

			return newErrorResponse("Invalid invoke function name.");
		} catch (Throwable e) {
			return newErrorResponse(e.getMessage());
		}
	}

	public Response balanceOf(ChaincodeStub stub, List<String> args) {

		try {
		    return newSuccessResponse("Succeeded 'balanceOf' function");
		} catch (Throwable e) {
		    return newErrorResponse("Failed 'balanceOf' function");
	    }
    }

	public Response ownerOf(ChaincodeStub stub, List<String> args) {

	    try {
		    return newSuccessResponse("Succeeded 'ownerOf' function");
	    } catch (Throwable e) {
		    return newErrorResponse("Failed 'ownerOf' function");
	    }
	}

	public Response safeTransferFrom(ChaincodeStub stub, List <String>args) {

		try {
			return newSuccessResponse("Succeeded 'safeTransferFrom' function");
		} catch (Exception e) {
			return newErrorResponse("Failed 'safeTransferFrom' function");
		}
    }

    public Response transferFrom(ChaincodeStub stub, List<String> args) {
		
		try {
			return newSuccessResponse("Succeeded 'transferFrom' function");
		} catch (Throwable e) {
			return newErrorResponse("Failed 'transferFrom' function");
		}
    }

    public Response approve(ChaincodeStub stub, List<String> args) {

	    try {
		    return newSuccessResponse("Succeeded 'approve' function");
	    } catch (Throwable e) {
		    return newErrorResponse("Failed 'approve' function");
	    }
    }

    public Response setApprovalForAll(ChaincodeStub stub, List<String> args) {
		try {
			return newSuccessResponse("Succeeded 'setApprovalForAll' function");
	    } catch (Throwable e) {
		    return newErrorResponse("Failed 'setApprovalForAll' function");
	    }
    }

    public Response getApproved(ChaincodeStub stub, List<String> args) {
		try {
			return newSuccessResponse("Succeeded 'getApproved' function");
	    } catch (Throwable e) {
		    return newErrorResponse("Failed 'getApproved' function");
	    }
    }

    public Response isApprovedForAll(ChaincodeStub stub, List<String> args) {
		try {
			return newSuccessResponse("Succeeded 'isApprovedForAll' function");
	    } catch (Throwable e) {
		    return newErrorResponse("Failed 'isApprovedForAll' function");
	    }
	}

    public static void main(String[] args) {
        new FabNFT().start(args);
    }
}
