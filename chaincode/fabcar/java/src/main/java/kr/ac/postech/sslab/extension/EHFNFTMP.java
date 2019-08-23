package kr.ac.postech.sslab.extension;

import kr.ac.postech.sslab.adapter.XAtt;
import kr.ac.postech.sslab.nft.BaseNFT;
import kr.ac.postech.sslab.standard.HFNFTMP;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;

import java.util.List;

public class EHFNFTMP extends HFNFTMP {

    @Override
    public Response init(ChaincodeStub stub) {
        return super.init(stub);
    }

    @Override
    public Response invoke(ChaincodeStub stub) {
        try {
            String func = stub.getFunction();
            List<String> args = stub.getParameters();

            switch (func) {
                case "mint":
                    return super.mint(stub, args);

                case "transfer":
                    return super.transfer(stub, args);

                case "approve":
                    return super.approve(stub, args);

                case "burn":
                    return super.burn(stub, args);

                case "query":
                    return super.queryToken(stub, args);

                default:
                    throw new Throwable("Invalid invoke method name. Expecting one of: "
                            + "[\"mint\", \"transfer\", \"transferFrom\", \"approve\", \"setApprovalForAll\", \"getApproved\", \"isApprovedForAll\"]");
            }

        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    public Response duplicate(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                throw new Throwable("Incorrect number of arguments. Expecting 1");
            }

            String _id = args.get(0);

            BaseNFT nft = BaseNFT.read(stub, _id);

            BaseNFT dup = new BaseNFT();
            dup.mint(stub, _id, nft.getType(), nft.getOwner(), nft.getXAtt().toJSONString(), nft.getUri().toJSONString());
            dup.setOperator(stub, nft.getOperator());
            dup.setApproved(stub, nft.getApproved());

            return ResponseUtils.newSuccessResponse();
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    public Response deactivate(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                throw new Throwable("Incorrect number of arguments. Expecting 1");
            }

            String _id = args.get(0);

            BaseNFT nft = BaseNFT.read(stub, _id);

            XAtt xatt = nft.getXAtt();
            xatt.deactivate();
            nft.setXAtt(stub, xatt);

            return ResponseUtils.newSuccessResponse();
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    public Response updateXAtt(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 2) {
                throw new Throwable("Incorrect number of arguments. Expecting 2");
            }

            String _xatt = args.get(0);
            String _id = args.get(1);

            BaseNFT nft = BaseNFT.read(stub, _id);
            nft.setXAtt(stub, new XAtt(_xatt, nft.getType()));

            return ResponseUtils.newSuccessResponse();
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    public Response updateUri(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 2) {
                throw new Throwable("Incorrect number of arguments. Expecting 2");
            }

            String _uri = args.get(0);
            String _id = args.get(1);

            BaseNFT nft = BaseNFT.read(stub, _id);
            nft.setXAtt(stub, new XAtt(_uri, nft.getType()));

            return ResponseUtils.newSuccessResponse();
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }
}
