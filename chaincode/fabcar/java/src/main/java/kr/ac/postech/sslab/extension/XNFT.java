package kr.ac.postech.sslab.extension;

import kr.ac.postech.sslab.adapter.XAttr;
import kr.ac.postech.sslab.nft.NFT;
import kr.ac.postech.sslab.standard.BaseNFT;
import kr.ac.postech.sslab.type.URI;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.List;

public class XNFT extends BaseNFT implements IXNFT {
    @Override
    public Response setURI(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 3) {
                throw new Throwable("FAILURE");
            }

            String id = args.get(0);
            int index = Integer.parseInt(args.get(1));
            String attribute = args.get(2);

            //Check Client Identity

            NFT nft = NFT.read(stub, id);

            URI uri = nft.getURI();
            if (uri == null)
                throw new Throwable();

            switch (index) {
                case 0:
                    uri.setPath(attribute);
                    break;

                case 1:
                    uri.setHash(attribute);
                    break;
            }

            nft.setURI(stub);

            return newSuccessResponse("SUCCESS");
        } catch (Throwable throwable) {
            return newErrorResponse("FAILURE");
        }
    }

    @Override
    public Response getURI(ChaincodeStub stub, List<String> args) {
        try {
            String id = args.get(0);
            int index = Integer.parseInt(args.get(1));
            NFT nft = NFT.read(stub, id);

            URI uri = nft.getURI();
            if (uri == null)
                throw new Throwable();

            String value;
            switch (index) {
                case 0:
                    value = uri.getPath();
                    break;

                case 1:
                    value = uri.getHash();
                    break;

                default:
                    throw new Throwable();
            }

            return newSuccessResponse(value);
        } catch (Throwable throwable) {
            return newErrorResponse("FAILURE");
        }
    }

    @Override
    public Response setXAttr(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 3) {
                throw new Throwable("FAILURE");
            }

            String id = args.get(0);
            int index = Integer.parseInt(args.get(1));
            String attr  = args.get(2);

            NFT nft = NFT.read(stub, id);

            XAttr xattr = nft.getXAttr();
            if (xattr == null)
                throw new Throwable();

            nft.setXAttr(stub, index, attr);

            return newSuccessResponse("SUCCESS");
        } catch (Throwable throwable) {
            return newErrorResponse("FAILURE");
        }
    }

    @Override
    public Response getXAttr(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 2) {
                throw new Throwable("FAILURE");
            }

            String id = args.get(0);
            int index = Integer.parseInt(args.get(1));

            NFT nft = NFT.read(stub, id);

            XAttr xattr = nft.getXAttr();
            if (xattr == null)
                throw new Throwable();

            String value = nft.getXAttr(index);
            return newSuccessResponse(value);
        } catch (Throwable throwable) {
            return newErrorResponse("FAILURE");
        }
    }
}
