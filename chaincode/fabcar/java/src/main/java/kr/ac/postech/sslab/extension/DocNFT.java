package kr.ac.postech.sslab.extension;

import kr.ac.postech.sslab.adapter.XAttr;
import kr.ac.postech.sslab.nft.NFT;
import kr.ac.postech.sslab.standard.BaseNFT;
import kr.ac.postech.sslab.type.URI;
import org.hyperledger.fabric.shim.ChaincodeStub;
import java.util.ArrayList;
import java.util.List;

public class DocNFT extends BaseNFT implements IXNFT {
    //write
    @Override
    public Response mint(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 7) {
                throw new Throwable("Incorrect number of arguments. Expecting 7");
            }

            String id = args.get(0).toLowerCase();
            String type = args.get(1).toLowerCase();
            String owner = args.get(2).toLowerCase();
            String hash = args.get(3).toLowerCase();
            String signers = args.get(4).toLowerCase();

            XAttr xattr = new XAttr();
            List<String> list = new ArrayList<>();
            list.add(hash);
            list.add(signers);

            xattr.assign(type, list);

            URI uri = new URI(args.get(5).toLowerCase(), args.get(6).toLowerCase());

            NFT nft = new NFT();
            nft.mint(stub, id, type, owner, xattr, uri);

            return newSuccessResponse("Mint a token " + nft.getId());
        } catch (Throwable throwable) {
            return newErrorResponse(throwable.getMessage());
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

            return newSuccessResponse("Set a token attribute");
        } catch (Throwable throwable) {
            return newErrorResponse(throwable.getMessage());
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

            return newSuccessResponse(nft.getXAttr(index));
        } catch (Throwable throwable) {
            return newErrorResponse(throwable.getMessage());
        }
    }
}
