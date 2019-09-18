package kr.ac.postech.sslab.extension;

import kr.ac.postech.sslab.adapter.XAttr;
import kr.ac.postech.sslab.nft.NFT;
import kr.ac.postech.sslab.standard.BaseNFT;
import kr.ac.postech.sslab.type.URI;
import org.hyperledger.fabric.shim.Chaincode.Response;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SigNFT extends BaseNFT implements IXNFT {
    @Override
    public Response mint(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 6) {
                throw new Throwable("Incorrect number of arguments. Expecting 6");
            }

            String id = args.get(0).toLowerCase();
            String type = args.get(1).toLowerCase();
            String owner = args.get(2).toLowerCase();
            String hash = args.get(3).toLowerCase();

            XAttr xattr = new XAttr();
            List<String> list = new ArrayList<>();
            list.add(hash);

            xattr.assign(type, list);

            URI uri = new URI(args.get(4).toLowerCase(), args.get(5).toLowerCase());

            NFT nft = new NFT();
            nft.mint(stub, id, type, owner, xattr, uri);

            return ResponseUtils.newSuccessResponse("Mint a token " + nft.getId());
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    @Override
    public Response setXAttr(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 3) {
                throw new Throwable("Incorrect number of arguments. Expecting 3");
            }

            String id = args.get(0).toLowerCase();
            int index = Integer.parseInt(args.get(1));
            String attr  = args.get(2).toLowerCase();

            NFT nft = NFT.read(stub, id);
            nft.setXAttr(stub, index, attr);

            return ResponseUtils.newSuccessResponse("Set a token attribute");
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }

    @Override
    public Response getXAttr(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 2) {
                throw new Throwable("Incorrect number of arguments. Expecting 2");
            }

            String id = args.get(0).toLowerCase();
            int index = Integer.parseInt(args.get(1));

            NFT nft = NFT.read(stub, id);

            return ResponseUtils.newSuccessResponse(nft.getXAttr(index));
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
    }
}
