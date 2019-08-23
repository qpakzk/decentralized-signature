package kr.ac.postech.sslab.standard;

import kr.ac.postech.sslab.adapter.XAtt;

import kr.ac.postech.sslab.nft.BaseNFT;
import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.json.simple.JSONObject;

import java.util.*;

public class HFNFTMP extends ChaincodeBase {

    @Override
    public Response init(ChaincodeStub stub) {
        try {
            String func = stub.getFunction();

            if (!func.equals("init")) {
                throw new Throwable("Method other than init is not supported");
            }

            List<String> args = stub.getParameters();
            if (args.size() != 0) {
                throw new Throwable("Incorrect number of arguments. Expecting 0");
            }

            return ResponseUtils.newSuccessResponse();
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    @Override
    public Response invoke(ChaincodeStub stub) {
        try {
            String func = stub.getFunction();
            List<String> args = stub.getParameters();

            switch (func) {
                case "mint":
                    return this.mint(stub, args);

                case "transfer":
                    return this.transfer(stub, args);

                case "setOperator":
                    return this.setOperator(stub, args);

                case "approve":
                    return this.approve(stub, args);

                case "burn":
                    return this.burn(stub, args);

                case "queryToken":
                    return this.queryToken(stub, args);

                case "queryOwnedTokensCount":
                    return this.queryOwnedTokensCount(stub, args);

                default:
                    throw new Throwable("Invalid invoke method name. Expecting one of: "
                            + "[\"mint\", \"transfer\", \"approve\", \"burn\", \"queryToken\", \"queryOwnedTokensCount\"]");
            }

        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    public Response mint(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 5) {
                throw new Throwable("Incorrect number of arguments. Expecting 5");
            }

            String _id = args.get(0).toLowerCase();
            String _type = args.get(1).toLowerCase();
            String _owner = args.get(2).toLowerCase();
            String _xatt = args.get(3).toLowerCase();
            String _uri = args.get(4).toLowerCase();

            BaseNFT nft = new BaseNFT();
            nft.mint(stub, _id, _type, _owner, _xatt, _uri);

            return ResponseUtils.newSuccessResponse();
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    public Response transfer(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 3) {
                throw new Throwable("Incorrect number of arguments. Expecting 3");
            }

            String _from = args.get(0).toLowerCase();
            String _to = args.get(1).toLowerCase();
            String _id = args.get(2).toLowerCase();

            BaseNFT nft = BaseNFT.read(stub, _id);

            if (!_from.equals(nft.getOwner())) {
                throw new Throwable("HFNFTMP transfer error");
            }

            nft.setOwner(stub, _to);

            return ResponseUtils.newSuccessResponse();
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    public Response setOperator(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 3) {
                throw new Throwable("Incorrect number of arguments. Expecting 3");
            }

            String _owner = args.get(0);
            String _operator = args.get(1);
            boolean _approved = Boolean.parseBoolean(args.get(2));

            String query = "{\"owner\":\"" + _owner + "\"}";
            QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(query);
            if (_approved) {
                while(resultsIterator.iterator().hasNext()) {
                    String id = resultsIterator.iterator().next().getKey();
                    BaseNFT nft = BaseNFT.read(stub, id);
                    nft.setOperator(stub, nft.getOperator().add(_operator));
                }
            }
            else {
                while(resultsIterator.iterator().hasNext()) {
                    String id = resultsIterator.iterator().next().getKey();
                    BaseNFT nft = BaseNFT.read(stub, id);
                    nft.setOperator(stub, nft.getOperator().remove(_operator));
                }
            }

            return ResponseUtils.newSuccessResponse();
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse("HFNFTMP approve error");
        }
    }

    public Response isOperatorForOwner(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 2) {
                throw new Throwable("Incorrect number of arguments. Expecting 2");
            }

            String _owner = args.get(0);
            String _operator = args.get(1);

            String query = "{\"owner\":\"" + _owner + "\"}";
            QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(query);

            while(resultsIterator.iterator().hasNext()) {
                String id = resultsIterator.iterator().next().getKey();
                BaseNFT nft = BaseNFT.read(stub, id);
                if(!_operator.equals(nft.getOperator())) {
                    return ResponseUtils.newSuccessResponse("false");
                }
            }

            return ResponseUtils.newSuccessResponse("true");
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse("HFNFTMP approve error");
        }
    }

    public Response approve(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 2) {
                throw new Throwable("Incorrect number of arguments. Expecting 2");
            }

            String _approved = args.get(0);
            String _id = args.get(1);

            BaseNFT nft = BaseNFT.read(stub, _id);
            nft.setApproved(stub, _approved);

            return ResponseUtils.newSuccessResponse();
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse("HFNFTMP approve error");
        }
    }

    public Response burn(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                throw new Throwable("Incorrect number of arguments. Expecting 1");
            }

            String _id = args.get(0).toLowerCase();

            BaseNFT nft = BaseNFT.read(stub, _id);
            nft.burn(stub, _id);

            return ResponseUtils.newSuccessResponse();
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    public Response queryToken(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                throw new Throwable("Incorrect number of arguments. Expecting 1");
            }

            String _id = args.get(0);

            BaseNFT nft = BaseNFT.read(stub, _id);
            Map<String, String> map = new HashMap<>();
            map.put("id", nft.getId());
            map.put("owner", nft.getOwner());
            map.put("operator", nft.getOperator().toString());
            map.put("approved", nft.getApproved());
            map.put("xatt", nft.getXAtt().toJSONString());
            map.put("uri", nft.getUri().toJSONString());

            String _query = new JSONObject(map).toJSONString();
            return ResponseUtils.newSuccessResponse(_query);
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    public Response queryOwnedTokensCount(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                throw new Throwable("Incorrect number of arguments. Expecting 1");
            }

            String _owner = args.get(0);

            long ownedTokensCount = 0;
            String query = "{\"owner\":\"" + _owner + "\"}";

            QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(query);
            while(resultsIterator.iterator().hasNext()) {
                resultsIterator.iterator().next();
                ownedTokensCount++;
            }

            return ResponseUtils.newSuccessResponse(Long.toString(ownedTokensCount));
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    public Response queryOwner(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                throw new Throwable("Incorrect number of arguments. Expecting 1");
            }

            String _id = args.get(0);

            BaseNFT nft = BaseNFT.read(stub, _id);

            String owner = nft.getOwner();
            return ResponseUtils.newSuccessResponse(owner);
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
	}

    public Response queryOperator(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                throw new Throwable("Incorrect number of arguments. Expecting 1");
            }

            String _id = args.get(0);

            BaseNFT nft = BaseNFT.read(stub, _id);

            String operator = nft.getOperator().toString();

            return ResponseUtils.newSuccessResponse(operator);
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
     }

    public Response queryApproved(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                throw new Throwable("Incorrect number of arguments. Expecting 1");
            }

            String _id = args.get(0);

            BaseNFT nft = BaseNFT.read(stub, _id);

            String approved = nft.getApproved();

            return ResponseUtils.newSuccessResponse(approved);
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    public Response queryHistory(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                throw new Throwable("Incorrect number of arguments. Expecting 1");
            }

            String _id = args.get(0);

            List<String> history = new LinkedList<>();
            QueryResultsIterator<KeyModification> resultsIterator = stub.getHistoryForKey(_id);
            while (resultsIterator.iterator().hasNext()) {
                history.add(resultsIterator.iterator().next().getStringValue());
            }

            Iterator<String> it = history.iterator();
            String result = "";
            while (it.hasNext()) {
                result += it.next() + ", ";
            }

            result = result.substring(0, result.length() - 1);
            return ResponseUtils.newSuccessResponse(result);
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    public static void main(String[] args) {
        new HFNFTMP().start(args);
    }
}