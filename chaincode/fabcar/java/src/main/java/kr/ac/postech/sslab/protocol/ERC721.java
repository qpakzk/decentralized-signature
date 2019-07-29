package kr.ac.postech.sslab.protocol;

import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.List;

import kr.ac.postech.sslab.structure.Token;

abstract public class ERC721 extends ChaincodeBase implements IERC721 {
	private long tokensCount;
	private String tokenId;

	public ERC721() {
		this.tokensCount = 0;
		this.tokenId = "";
	}

	private void increaseTokensCount(ChaincodeStub stub) {
		this.tokensCount += 1;
		stub.putStringState("tokensCount", String.valueOf(this.tokensCount));
	}

	protected String getRecentCreatedTokenId() {
		return this.tokenId;
	}

    @Override
    abstract public Response init(ChaincodeStub stub);

    @Override
    abstract public Response invoke(ChaincodeStub stub);

	@Override
	public Response balanceOf(ChaincodeStub stub, List<String> args) {
		try {
			
			//parameter
			String owner = args.get(0);
			
			String queryString = "{\"owner\":\"" + owner + "\"}";
			
			//return
			String balance = getNumberOfTokensForQueryString(stub, queryString);

			if(balance == null) {
				return newErrorResponse();
			}

		    return newSuccessResponse();
		} catch (Throwable e) {
		    return newErrorResponse();
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
				return newErrorResponse();
			}

			//return
			String owner = _ownerOf(stub, tokenId);

		    return newSuccessResponse(owner.getBytes("utf-8"));
	    } catch (Throwable e) {
			e.printStackTrace();
		    return newErrorResponse();
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
			
			Token token;
			JSONObject tokenJsonObject;

			if(from.equals("") == true) {
				this.tokenId = String.valueOf(this.tokensCount);
				token = new Token(this.tokenId, to);
				tokenJsonObject = token.constructTokenJSONObject();

				stub.putStringState(token.getTokenId(), tokenJsonObject.toString());

				increaseTokensCount(stub);
				return newSuccessResponse(token.getTokenId());
			}
			else {
				if(tokenId.equals("") == true) {
					return newErrorResponse();
				}

				String stringState = stub.getStringState(tokenId);
				if(stringState == null) {
					return newErrorResponse();
				}

				token = Token.retrieveToken(stringState);
				
				if(token.getOwner().equals(from) == false) {
					return newErrorResponse();
				}

				token.setOwner(to);
				tokenJsonObject = token.constructTokenJSONObject();
				
				stub.putStringState(token.getTokenId(), tokenJsonObject.toString());
				return newSuccessResponse(tokenJsonObject.toString());
			}
		} catch(Throwable e) {
			return newErrorResponse();
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
					return newErrorResponse();
				}

				Token token = Token.retrieveToken(stringState);

			token.setOperator(approved);
			JSONObject tokenJsonObject = token.constructTokenJSONObject();

			stub.putStringState(token.getTokenId(), tokenJsonObject.toString());
			return newSuccessResponse(token.getOperator());
		} catch (Throwable e) {
			return newErrorResponse();
		}
	}
	
	@Override
	public Response setApprovalForAll(ChaincodeStub stub, List<String> args) {
		try {
			return newSuccessResponse();
		} catch (Throwable e) {
			return newErrorResponse();
		}
	}

	@Override
    public Response getApproved(ChaincodeStub stub, List<String> args) {
		try {
			return newSuccessResponse();
		} catch (Throwable e) {
			return newErrorResponse();
		}
	}

	@Override
	public Response isApprovedForAll(ChaincodeStub stub, List<String> args) {
		try {
			return newSuccessResponse();
		} catch (Throwable e) {
			return newErrorResponse();
		}
	}
}