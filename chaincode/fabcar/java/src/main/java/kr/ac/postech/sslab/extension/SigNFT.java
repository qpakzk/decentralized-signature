package kr.ac.postech.sslab.extension;

import kr.ac.postech.sslab.adapter.XAttr;
import kr.ac.postech.sslab.nft.NFT;
import kr.ac.postech.sslab.type.URI;
import org.hyperledger.fabric.shim.ChaincodeStub;
import java.util.ArrayList;
import java.util.List;

public class SigNFT extends XNFT {
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
            String path = args.get(4).toLowerCase();
            String offChainHash = args.get(5).toLowerCase();

            XAttr xattr = new XAttr();
            List<String> list = new ArrayList<>();
            list.add(hash);

            xattr.assign(type, list);

            URI uri = new URI(path, offChainHash);

            NFT nft = new NFT();
            nft.mint(stub, id, type, owner, xattr, uri);

            return newSuccessResponse("Mint a token " + nft.getId());
        } catch (Throwable throwable) {
            return newErrorResponse(throwable.getMessage());
        }
    }
}
