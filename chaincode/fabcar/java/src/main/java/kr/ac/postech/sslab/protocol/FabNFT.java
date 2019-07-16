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

	public FabNFT () {
		tokensCount = 0;
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
			
			if (func.equals("ownerOf")) {
				return ownerOf(stub, args);
			}

			return newErrorResponse("Invalid invoke function name.");
		} catch (Throwable e) {
			return newErrorResponse(e.getMessage());
		}
	}

	public Response ownerOf(ChaincodeStub stub, List<String> args) {
	    try {
			//parameter
			String tokenId = args.get(0);

			if(tokenId == null) {
				return newErrorResponse("-1");
			}

			//return
			String owner = _ownerOf(stub, tokenId);

		    return newSuccessResponse(owner.getBytes("utf-8"));
	    } catch (Throwable e) {
			e.printStackTrace(System.out);
		    return newErrorResponse("-1");
	    }
	}

	private String _ownerOf(ChaincodeStub stub, String tokenId) {
		try {
			String jsonString = stub.getStringState(tokenId);
			
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(jsonString);

			return String.valueOf(jsonObject.get("owner"));
		} catch (Throwable e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

    public static void main(String[] args) {
        new FabNFT().start(args);
    }
}
