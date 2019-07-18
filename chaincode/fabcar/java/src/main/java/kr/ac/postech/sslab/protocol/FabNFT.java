package kr.ac.postech.sslab.protocol;

import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.List;

public class FabNFT extends ChaincodeBase implements FabNFTInterface {

	private long tokensCount;

	public FabNFT () {
		this.tokensCount = 0;
	}

	private void increaseTokensCount(ChaincodeStub stub) {
		this.tokensCount += 1;
		stub.putStringState("tokensCount", String.valueOf(this.tokensCount));
	}

    @Override
    public Response init(ChaincodeStub stub) {

    	try {
			String tokensCountString = stub.getStringState("tokensCount");
			tokensCount = Long.parseLong(tokensCountString);

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
				return this.balanceOf(stub, args);
			}
			else if (func.equals("ownerOf")) {
				return this.ownerOf(stub, args);
			}
			else if(func.equals("transferFrom")) {
				return this.transferFrom(stub, args);
			}
			else if (func.equals("approve")) {
				return this.approve(stub, args);
			}

			return newErrorResponse("Invalid invoke function name.");
		} catch (Throwable e) {
			return newErrorResponse(e.getMessage());
		}
	}

	@Override
	public Response balanceOf(ChaincodeStub stub, List<String> args) {
		try {
			//parameter
			String owner = args.get(0);
			
			String queryString = "{\"owner\":\"" + owner + "\"}";
			
			//return
			String balance = getNumberOfTokensForQueryString(stub, queryString);

			if(balance == null) {
				return newErrorResponse("-1");
			}

		    return newSuccessResponse(balance);
		} catch (Throwable e) {
		    return newErrorResponse("-1");
	    }
	}

	private String getNumberOfTokensForQueryString(ChaincodeStub stub, String queryString) {

	    int numberOfTokens = 0;

	    try {
		    QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(queryString);
		    while(resultsIterator.iterator().hasNext()) {
			    resultsIterator.iterator().next().getStringValue();
				numberOfTokens++;
			}
			
			return String.valueOf(numberOfTokens);
	    } catch (Throwable e) {
		    return null;
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

				increaseTokensCount(stub);
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

	@Override
	public Response approve(ChaincodeStub stub, List<String> args) {
		try {
			//parameter
			String approved = args.get(0);
			String tokenId = args.get(1);

			String stringState = stub.getStringState(tokenId);
				if(stringState == null) {
					return newErrorResponse("-1");
				}

			StandardToken token = StandardToken.retrieveToken(stringState);

			token.setOperator(approved);
			JSONObject tokenJsonObject = token.constructTokenJSONObject();

			stub.putStringState(token.getTokenId(), tokenJsonObject.toString());
			return newSuccessResponse(token.getOperator());
		} catch (Throwable e) {
			return newErrorResponse("-1");
		}
	}
	
    public static void main(String[] args) {
        new FabNFT().start(args);
    }
}
