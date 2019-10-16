package kr.ac.postech.sslab.extension;

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

            String id = args.get(0).toLowerCase();
            int index = Integer.parseInt(args.get(1));
            String attribute = args.get(2).toLowerCase();

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

            return newSuccessResponse("SUCCESS");
        } catch (Throwable throwable) {
            return newErrorResponse("FAILURE");
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
                    return newSuccessResponse(uri.toJSONString());
                }

                case 2: {
                    String id = args.get(0).toLowerCase();
                    int index = Integer.parseInt(args.get(1));
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
                            throw new Throwable("FAILURE");
                    }

                    return newSuccessResponse(attribute);
                }

                default:
                    throw new Throwable("FAILURE");
            }
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

            String id = args.get(0).toLowerCase();
            int index = Integer.parseInt(args.get(1));
            String attr  = args.get(2).toLowerCase();

            NFT nft = NFT.read(stub, id);
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

            String id = args.get(0).toLowerCase();
            int index = Integer.parseInt(args.get(1));

            NFT nft = NFT.read(stub, id);

            return newSuccessResponse(nft.getXAttr(index));
        } catch (Throwable throwable) {
            return newErrorResponse("FAILURE");
        }
    }
}
