package kr.ac.postech.sslab;

import kr.ac.postech.sslab.standard.BaseNFT;
import kr.ac.postech.sslab.standard.ERC721;
import kr.ac.postech.sslab.standard.IBaseNFT;
import kr.ac.postech.sslab.standard.IERC721;
import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;

import java.util.*;

public class Main extends ChaincodeBase implements IERC721, IBaseNFT {
    private ERC721 erc721 = new ERC721();
    private BaseNFT nft = new BaseNFT();

    @Override
    public Response init(ChaincodeStub stub) {
        try {
            String func = stub.getFunction();

            if (!func.equals("init")) {
                throw new Throwable("Functions other than init are not supported");
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
                case "balanceOf":
                    return this.balanceOf(stub, args);

                case "ownerOf":
                    return this.ownerOf(stub, args);

                case "transferFrom":
                    return this.transferFrom(stub, args);

                case "approve":
                    return this.approve(stub, args);

                case "setApprovalForAll":
                    return this.setApprovalForAll(stub, args);

                case "getApproved":
                    return this.getApproved(stub, args);

                case "isApprovedForAll":
                    return this.isApprovedForAll(stub, args);

                default:
                    throw new Throwable("Invalid invoke method name. Expecting one of: "
                            + "[\"balanceOf\", \"ownerOf\", \"transferFrom\", \"approve\", \"setApprovalForAll\", \"getApproved\", \"isApprovedForAll\"]");
            }

        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    @Override
    public Response balanceOf(ChaincodeStub stub, List<String> args) {
        return this.erc721.balanceOf(stub, args);
    }

    @Override
    public Response ownerOf(ChaincodeStub stub, List<String> args) {
       return this.erc721.ownerOf(stub, args);
    }

    @Override
    public Response transferFrom(ChaincodeStub stub, List<String> args) {
        return this.erc721.transferFrom(stub, args);
    }

    @Override
    public Response approve(ChaincodeStub stub, List<String> args) {
        return this.erc721.approve(stub, args);
    }

    @Override
    public Response setApprovalForAll(ChaincodeStub stub, List<String> args) {
        return this.erc721.setApprovalForAll(stub, args);
    }

    @Override
    public Response getApproved(ChaincodeStub stub, List<String> args) {
        return this.erc721.getApproved(stub, args);
    }

    @Override
    public Response isApprovedForAll(ChaincodeStub stub, List<String> args) {
        return this.erc721.isApprovedForAll(stub, args);
    }

    @Override
    public Response mint(ChaincodeStub stub, List<String> args) {
        return this.nft.mint(stub, args);
    }

    @Override
    public Response burn(ChaincodeStub stub, List<String> args) {
        return this.nft.burn(stub, args);
    }

    public Response getId(ChaincodeStub stub, List<String> args) {
        return this.nft.getId(stub, args);
    }

    public Response getType(ChaincodeStub stub, List<String> args) {
       return this.nft.getType(stub, args);
    }

    public Response setOwner(ChaincodeStub stub, List<String> args) {
        return this.nft.setOwner(stub, args);
    }

    public Response getOwner(ChaincodeStub stub, List<String> args) {
        return this.nft.getOwner(stub, args);
    }

    public Response setOperator(ChaincodeStub stub, List<String> args) {
        return this.nft.setOperator(stub, args);
    }

    public Response getOperator(ChaincodeStub stub, List<String> args) {
        return this.nft.getOperator(stub, args);
    }

    public Response setApprovee(ChaincodeStub stub, List<String> args) {
        return this.nft.setApprovee(stub, args);
    }

    @Override
    public Response getApprovee(ChaincodeStub stub, List<String> args) {
        return this.nft.getApprovee(stub, args);
    }

    @Override
    public Response getXAtt(ChaincodeStub stub, List<String> args) {
        return this.nft.getXAtt(stub, args);
    }

    @Override
    public Response setXAtt(ChaincodeStub stub, List<String> args) {
        return this.nft.setXAtt(stub, args);
    }


    @Override
    public Response getURI(ChaincodeStub stub, List<String> args) {
        return this.nft.getURI(stub, args);
    }

    @Override
    public Response setURI(ChaincodeStub stub, List<String> args) {
        return this.nft.setURI(stub, args);
    }

    public static void main(String[] args) {
        new Main().start(args);
    }

}
