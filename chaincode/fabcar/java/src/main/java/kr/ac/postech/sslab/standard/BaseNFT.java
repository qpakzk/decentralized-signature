package kr.ac.postech.sslab.standard;

import kr.ac.postech.sslab.adapter.XAttr;
import kr.ac.postech.sslab.main.ConcreteChaincodeBase;
import kr.ac.postech.sslab.nft.NFT;
import kr.ac.postech.sslab.type.URI;
import org.hyperledger.fabric.shim.ChaincodeStub;
import java.util.ArrayList;
import java.util.List;

public class BaseNFT extends ConcreteChaincodeBase implements IBaseNFT {
    @Override
    public Response mint(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 3) {
                throw new Throwable("FAILURE");
            }

            String id = args.get(0);
            String type = args.get(1);
            String owner = args.get(2);

            XAttr xattr = new XAttr();
            xattr.assign(type, new ArrayList<String>());

            URI uri = new URI();

            //Check Client Identity

            NFT nft = new NFT();
            nft.mint(stub, id, type, owner, xattr, uri);
            return newSuccessResponse("SUCCESS");
        } catch (Throwable throwable) {
            return newErrorResponse("FAILURE");
        }
    }

    @Override
    public Response burn(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                throw new Throwable("FAILURE");
            }

            String id = args.get(0);

            //Check Client Identity

            NFT nft = NFT.read(stub, id);
            nft.burn(stub, id);

            return newSuccessResponse("SUCCESS");
        } catch (Throwable throwable) {
            return newErrorResponse("FAILURE");
        }
    }

    @Override
    public Response getType(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                throw new Throwable("FAILURE");
            }

            String id = args.get(0);

            NFT nft = NFT.read(stub, id);
            String type = nft.getType();

            return newSuccessResponse(type);
        } catch (Throwable throwable) {
            return newErrorResponse("FAILURE");
        }
    }

    //write
    @Override
    public Response setOwner(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 3) {
                throw new Throwable("FAILURE");
            }

            String sender = args.get(0);
            String receiver = args.get(1);
            String id = args.get(2);

            NFT nft = NFT.read(stub, id);

            if (!sender.equals(nft.getOwner())) {
                throw new Throwable("FAILURE");
            }

            //Check Client Identity

            nft.setOwner(stub, receiver);

            return newSuccessResponse("SUCCESS");
        } catch (Throwable throwable) {
            return newErrorResponse("FAILURE");
        }
    }

    //read
    @Override
    public Response getOwner(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                throw new Throwable("FAILURE");
            }

            String id = args.get(0);

            NFT nft = NFT.read(stub, id);
            String owner = nft.getOwner();

            return newSuccessResponse(owner);
        } catch (Throwable throwable) {
            return newErrorResponse("FAILURE");
        }
    }

    @Override
    public Response setOperatorForCaller(ChaincodeStub stub, List<String> args) {
        try {
            //should be modified as 2 arguments
            if (args.size() != 3) {
                throw new Throwable("FAILURE");
            }

            //caller should be deleted
            String caller = args.get(0);
            String operator = args.get(1);
            boolean approved = Boolean.parseBoolean(args.get(2));

            if (operator.equals(caller)) {
                throw new Throwable("FAILURE");
            }

            if (this.getOperatorsApproval() == null) {
                throw new Throwable();
            }
            //Check Client Identity
            this.setOperatorsApproval(stub, caller, operator, approved);

            return newSuccessResponse("SUCCESS");
        } catch (Throwable throwable) {
            return newErrorResponse("FAILURE");
        }
    }

    @Override
    public Response isOperatorForCaller(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 2) {
                throw new Throwable("FAILURE");
            }

            String owner = args.get(0);
            String operator = args.get(1);

            boolean approved = this.isOperatorForOwner(owner, operator);

            return newSuccessResponse(Boolean.toString(approved).toUpperCase());
        } catch (Throwable throwable) {
            return newErrorResponse("FAILURE");
        }
    }

    @Override
    public Response setApprovee(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 2) {
                throw new Throwable("FAILURE");
            }

            String approvee = args.get(0);
            String id = args.get(1);

            //Check Client Identity

            NFT nft = NFT.read(stub, id);
            nft.setApprovee(stub, approvee);

            return newSuccessResponse("SUCCESS");
        } catch (Throwable throwable) {
            return newErrorResponse("FAILURE");
        }
    }

    @Override
    public Response getApprovee(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                throw new Throwable("FAILURE");
            }

            String id = args.get(0);

            NFT nft = NFT.read(stub, id);
            String approvee = nft.getApprovee();

            return newSuccessResponse(approvee);
        } catch (Throwable throwable) {
            return newErrorResponse("FAILURE");
        }
    }
}