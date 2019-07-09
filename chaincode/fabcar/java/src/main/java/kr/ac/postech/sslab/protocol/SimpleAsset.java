package org.hyperledger.fabric.example;

import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * SimpleAsset implements a simple chaincode to manage an asset
 */
public class SimpleAsset extends ChaincodeBase {

    int numberOfToken = 0;

    /**
     * Init is called during chaincode instantiation to initialize any
     * data. Note that chaincode upgrade also calls this function to reset
     * or to migrate data.
     *
     * @param stub {@link ChaincodeStub} to operate proposal and ledger
     * @return response
     */
    @Override
    public Response init(ChaincodeStub stub) {

    /*	    try {
            // Get the args from the transaction proposal
            List<String> args = stub.getStringArgs();
            if (args.size() != 2) {
                newErrorResponse("Incorrect arguments. Expecting a key and a value");
            }
            // Set up any variables or assets here by calling stub.putState()
            // We store the key and the value on the ledger
            stub.putStringState(args.get(0), args.get(1));
            return newSuccessResponse();
        } catch (Throwable e) {
            return newErrorResponse("Failed to create asset");
        }*/
	    return newSuccessResponse("init success");
    }

    /**
     * Invoke is called per transaction on the chaincode. Each transaction is
     * either a 'get' or a 'set' on the asset created by Init function. The Set
     * method may create a new asset by specifying a new key-value pair.
     *
     * @param stub {@link ChaincodeStub} to operate proposal and ledger
     * @return response
     */
    @Override
    public Response invoke(ChaincodeStub stub) {
        try {
            // Extract the function and args from the transaction proposal
            String func = stub.getFunction();
            List<String> params = stub.getParameters();
            if (func.equals("initToken")) {
                // Return result as success payload
		    return initToken(stub, params);
            } else if (func.equals("transferToken")) {
		    return transferToken(stub, params);	    
	    } else if (func.equals("queryToken")) {
                // Return result as success payload
		    return queryToken(stub, params);
            } else if (func.equals("queryTokensByOwner")) {
		    return queryTokensByOwner(stub, params);
	    } else if (func.equals("approve")) {
    		    return approve(stub, params);
	    } else if (func.equals("setApprovalForAll")) {
		    //return setApprovalForAll(stub, params);
	    } else if (func.equals("getApproved")) {
		    //return getApproved(stub, params);
	    } else if (func.equals("isApprovedForAll")) {
		    //return isApprovedForAll(stub, params);
	    } else if (func.equals("balanceOf")) {
		    return balanceOf(stub, params);
	    } else if (func.equals("ownerOf")) {
		    return ownerOf(stub, params);
	    } else if (func.equals("divide")) {
		    return divide(stub, params);
	    } else if (func.equals("delete")) {
		    return delete(stub, params);
	    } else if (func.equals("update")) {
		    return update(stub, params);
	    } else if (func.equals("queryTokenHistory")) {
		    return queryTokenHistory(stub, params);
	    }
            return newErrorResponse("Invalid invoke function name.");
        } catch (Throwable e) {
            return newErrorResponse(e.getMessage());
        }
    }

    public Response initToken(ChaincodeStub stub, List<String>args) {

	    //      0       1      2      3      4      5
	    //    token   woori   100   owner  issuer  sig
	String tokenName;
	String bankName;
	String owner;
	boolean status;
	String issuer;
	String value;
//       	ArrayList<String> operators = new ArrayList<String>();
	String operator;
	String parentTokenName;
	String bankSig;
	JSONObject jsonObject = new JSONObject();

	try {
		tokenName = "token" + String.valueOf(numberOfToken);
		bankName = args.get(1);
		value = args.get(2);
		owner = args.get(3);
		issuer = args.get(4);
		operator = "";
		status = true;
		parentTokenName = "";
		bankSig = args.get(5);

		jsonObject.put("tokenName", tokenName);
		jsonObject.put("bankName", bankName);
		jsonObject.put("owner", owner);
		jsonObject.put("value", value);
		jsonObject.put("status", String.valueOf(status));
		jsonObject.put("issuer", issuer);
		jsonObject.put("operator", operator);
		jsonObject.put("parent", parentTokenName);
		jsonObject.put("bankSig", bankSig);

		//String getToken = null;
		//getToken = stub.getStringState(tokenName);
		/*if(getToken != null) {
			return newErrorResponse("token already exists");
		}*/

		stub.putStringState(tokenName, jsonObject.toString());
		numberOfToken++;
		return newSuccessResponse("token init success");
	} catch (Throwable e) {
		return newErrorResponse("token init failure");
	}
    }

    public Response queryToken(ChaincodeStub stub, List<String>args) {

	    //   0
	    // token0
	    String tokenName = args.get(0);

	    try {
		    byte[] result = stub.getState(tokenName);
		    //String result = new String(stub.getStringState(tokenNumber));
		   // return newSuccessResponse(stub.getStringState(tokenNumber));
		  /*  return newSuccessResponse(String.valueOf(getToken));*/
		    return newSuccessResponse(result);
	    } catch (Throwable e) {
		    return newErrorResponse("token query failure");
	    }
    }

    public Response transferToken(ChaincodeStub stub, List<String>args) {
	    
	    //   0       1
	    // token0  owner
	    try {
		    String tokenName = args.get(0);
		    String newOwner = args.get(1);
	
		    String getToken = stub.getStringState(tokenName);

		    JSONParser parser = new JSONParser();
		    Object obj = parser.parse(getToken);
		    JSONObject jsonObject = (JSONObject) obj;

		    jsonObject.put("owner", newOwner);
		    stub.putStringState(tokenName, jsonObject.toString());
		    return newSuccessResponse("token transfer success");
	    } catch (Throwable e) {
		    return newErrorResponse("token transfer failure");
	    }
	    
    }


    public Response approve(ChaincodeStub stub, List<String>args) {

	    // 0       1        2
	    // owner token0  operator
	    try {
		    String owner = args.get(0);
		    String tokenName = args.get(1);
		    String operator = args.get(2);

		    String getToken = stub.getStringState(tokenName);

		    JSONParser parser = new JSONParser();
		    Object obj = parser.parse(getToken);
		    JSONObject jsonObject = (JSONObject) obj;

		    jsonObject.put("operator", operator);
		    stub.putStringState(tokenName, jsonObject.toString());
		    return newSuccessResponse("token approve success");
	    } catch (Throwable e) {
		    return newErrorResponse("token approve failure");
	    }
    }
/*
    public Response setApprovalForAll(ChaincodeStub stub, List<String>args) {

    }

    public Response getApproved(ChaincodeStub stub, List<String>args) {

    }

    public Response isApprovedForAll(ChaincodeStub stub, List<String>args) {

    }
*/
    public Response balanceOf(ChaincodeStub stub, List<String>args) {

	    //   0
	    // owner
	    try {
		    String owner = args.get(0);
		    String queryString = "{\"selector\":{\"owner\":\"" + owner + "\"}}";

		    String _balanceOf = getNumberOfTokenForQueryString(stub, queryString);
		    //String balanceOfToken = String.valueOf(_balanceOf);
		    return newSuccessResponse(_balanceOf.getBytes("utf-8"));
	    } catch (Throwable e) {
		    return newErrorResponse("balanceOf failure");
	    }
    } 

    public Response ownerOf(ChaincodeStub stub, List<String>args) {

	    //   0 
	    // token0
	    try {
		    String tokenName = args.get(0);
		    String getToken = stub.getStringState(tokenName);

		    JSONParser parser = new JSONParser();
		    Object obj = parser.parse(getToken);
		    JSONObject jsonObject = (JSONObject) obj;

		    String owner = String.valueOf(jsonObject.get("owner"));
		    return newSuccessResponse(owner.getBytes("utf-8"));
	    } catch (Throwable e) {
		    return newErrorResponse("ownerOf Failure");
	    }
    }


  
    public Response queryTokensByOwner(ChaincodeStub stub, List<String>args) {

	    //   0
	    // owner
	    try {
		    String owner = args.get(0);
		    String queryString = "{\"selector\":{\"owner\":\"" + owner + "\"}}";

		    String _balanceOf = getQueryResultForQueryString(stub, queryString);
		    //String balanceOfToken = String.valueOf(_balanceOf);
		    return newSuccessResponse(_balanceOf.getBytes("utf-8"));
	    } catch (Throwable e) {
		    return newErrorResponse("queryToknesByOwner Failure");
	    }

    }

    public String getNumberOfTokenForQueryString(ChaincodeStub stub, String queryString) {

	    int numberOfToken = 0;

	    try {
		    QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(queryString);
		    while(resultsIterator.iterator().hasNext()) {
			    resultsIterator.iterator().next().getStringValue();
			    numberOfToken++;
		    }
	    } catch (Throwable e) {
		    return "getting number of token fail";
	    }


	  return String.valueOf(numberOfToken);
    } 

    public String getQueryResultForQueryString(ChaincodeStub stub, String queryString) {
	    
	    String result="";
	    try {
		   QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(queryString);
		   while(resultsIterator.iterator().hasNext()) {
			   result += resultsIterator.iterator().next().getStringValue();
		   }
	    } catch (Throwable e) {
		    return "getting tokens fail";
	    }

	  return result;
	//return String.valueOf(i);
    }

    public Response divide(ChaincodeStub stub, List<String>args) {

	    //   0       1      2
	    // owner  token0  value
	    String owner = args.get(0);
	    String tokenName = args.get(1);
	    String value = args.get(2);

	    String newTokenName, dividedTokenName;
    	    String newBankName, dividedBankName;
    	    String newOwner, dividedOwner;
    	    String newStatus, dividedStatus;
    	    String newIssuer, dividedIssuer;
    	    String newValue, dividedValue;
	    String newOperator, dividedOperator;
	    String newParent, dividedParent;

	    int intNewValue, intValue;
    	    //ArrayList<String> operators = new ArrayList<String>();
	    JSONObject jsonObject = new JSONObject();
    	    JSONObject newJSONObject = new JSONObject();
	    JSONObject dividedJSONObject = new JSONObject();

    	    try {
		    String getToken = stub.getStringState(tokenName);
		    JSONParser parser = new JSONParser();
		    Object obj = parser.parse(getToken);
		    jsonObject = (JSONObject) obj;
		    
		    newTokenName = "token" + String.valueOf(numberOfToken);
		    newBankName = String.valueOf(jsonObject.get("bankName"));
		    newOwner = String.valueOf(jsonObject.get("owner"));
		    newStatus = String.valueOf(jsonObject.get("status"));
		    newIssuer = String.valueOf(jsonObject.get("issuer"));
		    newOperator = "";

		    newParent = tokenName;
		    newValue = String.valueOf(jsonObject.get("value"));
		    dividedValue = "0";

		    intNewValue = Integer.parseInt(newValue);
		    intValue = Integer.parseInt(value);
		    if(intNewValue >= intValue) {
			    newValue = String.valueOf(intNewValue - intValue);
			    dividedValue = value;
		    }

		    dividedTokenName = "token" + String.valueOf(numberOfToken+1);
		    dividedBankName = String.valueOf(jsonObject.get("bankName"));
		    dividedOwner = owner;//String.valueOf(jsonObject.get("owner"));
		    dividedStatus = String.valueOf(jsonObject.get("status"));
		    dividedIssuer = String.valueOf(jsonObject.get("issuer"));
		    dividedOperator = "";

		    dividedParent = tokenName;
		    
		    // for existing owner
		newJSONObject.put("tokenName", newTokenName);
		newJSONObject.put("bankName", newBankName);
		newJSONObject.put("owner", newOwner);
		newJSONObject.put("value", newValue);
		newJSONObject.put("status", newStatus);
		newJSONObject.put("issuer", newIssuer);
		newJSONObject.put("operator", newOperator);
		newJSONObject.put("parent", newParent);
		
		    newJSONObject.put("tokenName", newTokenName);

		// for owner of divided token
		dividedJSONObject.put("tokenName", dividedTokenName);
		dividedJSONObject.put("bankName", dividedBankName);
		dividedJSONObject.put("owner", dividedOwner);
		dividedJSONObject.put("value", dividedValue);
		dividedJSONObject.put("status", dividedStatus);
		dividedJSONObject.put("issuer", dividedIssuer);
		dividedJSONObject.put("operator", dividedOperator);
		dividedJSONObject.put("parent", dividedParent);

		//String getToken = null;
		//getToken = stub.getStringState(tokenName);
		/*if(getToken != null) {
			return newErrorResponse("token already exists");
		}*/

		stub.putStringState(newTokenName, newJSONObject.toString());
		stub.putStringState(dividedTokenName, dividedJSONObject.toString());
		numberOfToken += 2;
//		numberOfToken++;		

		// change previous token status to false

//		    JSONParser parser = new JSONParser();
//		    Object obj = parser.parse(getToken);
//		    jsonObject = (JSONObject) obj;

		    jsonObject.put("status", "false");
		    stub.putStringState(tokenName, jsonObject.toString());

		return newSuccessResponse("token division success");
	} catch (Throwable e) {
		return newErrorResponse("token division failure : " + e);
	}
    }

    public Response delete(ChaincodeStub stub, List<String>args) {

	    //   0     1
	    // owner token0
	    try {
		    String owner = args.get(0);
		    String tokenName = args.get(1);

		    String getToken = stub.getStringState(tokenName);

		    JSONParser parser = new JSONParser();
		    Object obj = parser.parse(getToken);
		    JSONObject jsonObject = (JSONObject) obj;

		    String _owner = String.valueOf(jsonObject.get("owner"));
		    
		    if(owner.equals(_owner)) {
			    jsonObject.put("status", "false");
			    stub.putStringState(tokenName, jsonObject.toString());
		    } else {
			    newErrorResponse("You are not owner");
		    }
		    return newSuccessResponse(owner);
	    } catch (Throwable e) {
		    return newErrorResponse("ownerOf Failure");
	    }
    } 

    public Response update(ChaincodeStub stub, List<String>args) {

	    //   0     1      2 
	    // owner token0 value
	    try {
		    String owner = args.get(0);
		    String tokenName = args.get(1);
		    String value = args.get(2);

		    String getToken = stub.getStringState(tokenName);

		    JSONParser parser = new JSONParser();
		    Object obj = parser.parse(getToken);
		    JSONObject jsonObject = (JSONObject) obj;

		    String _owner = String.valueOf(jsonObject.get("owner"));
		    
		    if(owner.equals(_owner)) {
			    jsonObject.put("value", value);
			    stub.putStringState(tokenName, jsonObject.toString());
		    } else {
			    newErrorResponse("You are not owner");
		    }
		    return newSuccessResponse(owner);
	    } catch (Throwable e) {
		    return newErrorResponse("ownerOf Failure");
	    }
    }

    public Response queryTokenHistory(ChaincodeStub stub, List<String>args) {

	//   0
	// token0

	String allParentToken = "";
	ArrayList<String> parent_token = new ArrayList<String>();
	try {
		String tokenName = args.get(0);

		String getToken = stub.getStringState(tokenName);

	    	JSONParser parser = new JSONParser();
	     	Object obj = parser.parse(getToken);
    		JSONObject jsonObject = (JSONObject) obj;

		String parent = String.valueOf(jsonObject.get("parent"));
				
		while(true) {
			if(parent.equals(""))
				break;
			parent_token.add(parent);

			tokenName = parent;

			getToken = stub.getStringState(tokenName);
			obj = parser.parse(getToken);
			jsonObject = (JSONObject) obj;

			parent = String.valueOf(jsonObject.get("parent"));
		}
		//String queryString = "{\"selector\":{\"owner\":\"" + owner + "\"}}";

		//String _balanceOf = getQueryResultForQueryString(stub, queryString);
		//String balanceOfToken = String.valueOf(_balanceOf);

		//return newSuccessResponse(_balanceOf);
		
		for(int i=0; i<parent_token.size(); i++) {
			allParentToken += parent_token.get(i);
			allParentToken += " ";
		}
		return newSuccessResponse(allParentToken.getBytes("utf-8"));
	} catch (Throwable e) {

		return newErrorResponse("queryToknesByOwner Failure");
	}
    }

/*
    public Response constructQueryResponseFromIterator(ChaincodeStub stub, List<String>args) {

	    // buffer is a JSON array containing QueryResults
	    var buffer bytes.Buffer
	    buffer.WriteString("[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return nil, err
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"Key\":")
		buffer.WriteString("\"")
		buffer.WriteString(queryResponse.Key)
		buffer.WriteString("\"")

		buffer.WriteString(", \"Record\":")
		// Record is a JSON object, so we write as-is
		buffer.WriteString(string(queryResponse.Value))
		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")

	return &buffer, nil
    } */
    /*
    /**
     * get returns the value of the specified asset key
     *
     * @param stub {@link ChaincodeStub} to operate proposal and ledger
     * @param args key
     * @return value
    
    private String get(ChaincodeStub stub, List<String> args) {
        if (args.size() != 2) {
            throw new RuntimeException("Incorrect arguments. Expecting a key");
        }

        String value = stub.getStringState(args.get(0));
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("Asset not found: " + args.get(0));
        }
        return value;
    }

    /**
     * set stores the asset (both key and value) on the ledger. If the key exists,
     * it will override the value with the new one
     *
     * @param stub {@link ChaincodeStub} to operate proposal and ledger
     * @param args key and value
     * @return value
    
    private String set(ChaincodeStub stub, List<String> args) {
        if (args.size() != 2) {
            throw new RuntimeException("Incorrect arguments. Expecting a key and a value");
        }
        stub.putStringState(args.get(0), args.get(1));
        return args.get(1);
    }
*/
    public static void main(String[] args) {
        new SimpleAsset().start(args);
    }

}
