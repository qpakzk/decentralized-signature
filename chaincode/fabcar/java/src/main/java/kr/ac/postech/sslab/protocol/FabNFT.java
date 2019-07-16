package kr.ac.postech.sslab.protocol;

import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.List;

public class FabNFT extends ChaincodeBase implements StandardFabNFT {

	long tokensCount;

	public FabNFT () {
		this.tokensCount = 0;
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
			else if(func.equals("transferFrom")) {
				return transferFrom(stub, args);
			}

			return newErrorResponse("Invalid invoke function name.");
		} catch (Throwable e) {
			return newErrorResponse(e.getMessage());
		}
	}

	@Override
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
			e.printStackTrace();
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
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Response transferFrom(ChaincodeStub stub, List<String> args) {
		try {
			//parameter
			String from = args.get(0);
			String to = args.get(1);
			String tokenId = args.get(2);
			
			StandardToken token;
			JSONObject tokenJsonObject;

			if(from.equals("") == true) {
				token = new StandardToken(String.valueOf(this.tokensCount), to);
				tokenJsonObject = token.constructTokenJSONObject();

				stub.putStringState(token.getTokenId(), tokenJsonObject.toString());

				this.tokensCount += 1;
				return newSuccessResponse(token.getTokenId());
			}
			else {
				if(tokenId.equals("") == true) {
					return newErrorResponse("-1");
				}

				String stringState = stub.getStringState(tokenId);
				if(stringState == null) {
					return newErrorResponse("-1");
				}

				token = StandardToken.retrieveToken(stringState);
				
				if(token.getOwner().equals(from) == false) {
					return newErrorResponse("-1");
				}

				token.setOwner(to);
				tokenJsonObject = token.constructTokenJSONObject();
				
				stub.putStringState(token.getTokenId(), tokenJsonObject.toString());
				return newSuccessResponse(tokenJsonObject.toString());
			}
		} catch(Throwable e) {
			return newErrorResponse("-1");
		}
	}

    public static void main(String[] args) {
        new FabNFT().start(args);
    }
}
