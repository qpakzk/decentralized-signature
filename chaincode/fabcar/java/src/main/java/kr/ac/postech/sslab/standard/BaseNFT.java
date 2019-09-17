package kr.ac.postech.sslab.standard;

import kr.ac.postech.sslab.adapter.XAttr;
import kr.ac.postech.sslab.nft.NFT;
import kr.ac.postech.sslab.type.URI;
import org.hyperledger.fabric.shim.Chaincode.Response;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.util.ArrayList;
import java.util.List;

public class BaseNFT implements IBaseNFT {
    @Override
    public Response mint(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 3) {
                throw new Throwable("Incorrect number of arguments. Expecting 3");
            }

            String id = args.get(0).toLowerCase();
            String type = args.get(1).toLowerCase();
            String owner = args.get(2).toLowerCase();

            XAttr xattr = new XAttr();
            xattr.assign(type, new ArrayList<>());

            URI uri = new URI();

            //Check Client Identity

            NFT nft = new NFT();
            nft.mint(stub, id, type, owner, xattr, uri);

            return ResponseUtils.newSuccessResponse("Success");
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    @Override
    public Response burn(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                throw new Throwable("Incorrect number of arguments. Expecting 1");
            }

            String id = args.get(0).toLowerCase();

            //Check Client Identity

            NFT nft = NFT.read(stub, id);
            nft.burn(stub, id);

            return ResponseUtils.newSuccessResponse("Success");
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    @Override
    public Response getType(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                throw new Throwable("Incorrect number of arguments. Expecting 1");
            }

            String id = args.get(0).toLowerCase();

            NFT nft = NFT.read(stub, id);
            String type = nft.getType();

            return ResponseUtils.newSuccessResponse(type);
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    //write
    @Override
    public Response setOwner(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 3) {
                throw new Throwable("Incorrect number of arguments. Expecting 3");
            }

            String sender = args.get(0).toLowerCase();
            String receiver = args.get(1).toLowerCase();
            String id = args.get(2).toLowerCase();

            NFT nft = NFT.read(stub, id);

            if (!sender.equals(nft.getOwner())) {
                throw new Throwable("Transfer of token that is not own");
            }

            //Check Client Identity

            nft.setOwner(stub, receiver);

            return ResponseUtils.newSuccessResponse("Success");
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    //read
    @Override
    public Response getOwner(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                throw new Throwable("Incorrect number of arguments. Expecting 1");
            }

            String id = args.get(0).toLowerCase();

            NFT nft = NFT.read(stub, id);
            String owner = nft.getOwner();

            return ResponseUtils.newSuccessResponse(owner);
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    @Override
    public Response setOperator(ChaincodeStub stub, List<String> args) {
        try {
            //should be modified as 2 arguments
            if (args.size() != 3) {
                throw new Throwable("Incorrect number of arguments. Expecting 3");
            }

            //caller should be deleted
            String caller = args.get(0).toLowerCase();
            String operator = args.get(1).toLowerCase();
            boolean approved = Boolean.parseBoolean(args.get(2));

            if (operator.equals(caller)) {
                throw new Throwable("Operator shouldn't be the same as owner");
            }

            //Check Client Identity

            String query = "{\"selector\":{\"owner\":\"" + caller + "\"}}";
            QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(query);

            if (approved) {
                while(resultsIterator.iterator().hasNext()) {
                    String id = resultsIterator.iterator().next().getKey();
                    NFT nft = NFT.read(stub, id);
                    nft.getOperator().add(operator);
                    nft.setOperator(stub);
                }

                return ResponseUtils.newSuccessResponse(operator + " is added to operator of the token owned by " + caller);
            }
            else {
                while(resultsIterator.iterator().hasNext()) {
                    String id = resultsIterator.iterator().next().getKey();
                    NFT nft = NFT.read(stub, id);
                    nft.getOperator().remove(operator);
                    nft.setOperator(stub);
                }

                return ResponseUtils.newSuccessResponse(operator + " is removed from operator of the token owned by " + caller);
            }
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    @Override
    public Response getOperator(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                throw new Throwable("Incorrect number of arguments. Expecting 1");
            }

            String id = args.get(0).toLowerCase();
            NFT nft = NFT.read(stub, id);

            String operator = nft.getOperator().toString();

            return ResponseUtils.newSuccessResponse(operator);
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    @Override
    public Response setApprovee(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 2) {
                throw new Throwable("Incorrect number of arguments. Expecting 2");
            }

            String approvee = args.get(0).toLowerCase();
            String id = args.get(1).toLowerCase();

            //Check Client Identity

            NFT nft = NFT.read(stub, id);
            nft.setApprovee(stub, approvee);

            return ResponseUtils.newSuccessResponse("Success");
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    @Override
    public Response getApprovee(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                throw new Throwable("Incorrect number of arguments. Expecting 1");
            }

            String id = args.get(0).toLowerCase();

            NFT nft = NFT.read(stub, id);
            String approvee = nft.getApprovee();

            return ResponseUtils.newSuccessResponse(approvee);
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    @Override
    public Response setURI(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 3) {
                throw new Throwable("Incorrect number of arguments. Expecting 3");
            }

            int index = Integer.parseInt(args.get(0));
            String attribute = args.get(1).toLowerCase();
            String id = args.get(2).toLowerCase();

            //Check Client Identity

            NFT nft = NFT.read(stub, id);

            switch (index) {
                case 0:
                    nft.getURI().setPath(attribute);
                    break;

                case 1:
                    nft.getURI().setHash(attribute);
                    break;
            }

            nft.setURI(stub);

            return ResponseUtils.newSuccessResponse("Success");
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    @Override
    public Response getURI(ChaincodeStub stub, List<String> args) {
        try {
            switch (args.size()) {
                case 1: {
                    String id = args.get(0).toLowerCase();
                    NFT nft = NFT.read(stub, id);
                    URI uri = nft.getURI();
                    return ResponseUtils.newSuccessResponse(uri.toJSONString());
                }

                case 2: {
                    int index = Integer.parseInt(args.get(0));
                    String id = args.get(1).toLowerCase();
                    NFT nft = NFT.read(stub, id);

                    String attribute;
                    switch (index) {
                        case 0:
                             attribute = nft.getURI().getPath();
                            break;

                        case 1:
                            attribute = nft.getURI().getHash();
                            break;

                        default:
                            throw new Throwable("Incorrect index. Expecting 0 or 1");
                    }

                    return ResponseUtils.newSuccessResponse(attribute);
                }

                default:
                    throw new Throwable("Incorrect number of arguments. Expecting 1 or 2");
            }
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }
}
