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
                throw new Throwable("FAILURE");
            }

            String id = args.get(0);
            String type = args.get(1);
            String owner = args.get(2);
            String hash = args.get(3);
            String path = args.get(4);
            String merkleroot = args.get(5);

            XAttr xattr = new XAttr();
            ArrayList<String> list = new ArrayList<>();
            list.add(hash);

            xattr.assign(type, list);

            URI uri = new URI(path, merkleroot);

            NFT nft = new NFT();
            nft.mint(stub, id, type, owner, xattr, uri);

            return newSuccessResponse("SUCCESS");
        } catch (Throwable throwable) {
            return newErrorResponse("FAILURE");
        }
    }
}
