package kr.ac.postech.sslab.extension;

import kr.ac.postech.sslab.adapter.XAttr;
import kr.ac.postech.sslab.nft.NFT;
import kr.ac.postech.sslab.type.URI;
import org.hyperledger.fabric.shim.ChaincodeStub;
import java.util.ArrayList;
import java.util.List;

public class DocNFT extends XNFT {
    @Override
    public Response mint(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 8) {
                throw new Throwable("FAILURE");
            }

            String id = args.get(0).toLowerCase();
            String type = args.get(1).toLowerCase();
            String owner = args.get(2).toLowerCase();
            String pages = args.get(3).toLowerCase();
            String hash = args.get(4).toLowerCase();
            String signers = args.get(5).toLowerCase();
            String path = args.get(6).toLowerCase();
            String merkleroot = args.get(7).toLowerCase();

            XAttr xattr = new XAttr();
            ArrayList<String> list = new ArrayList<>();
            list.add(pages);
            list.add(hash);
            list.add(signers);

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
