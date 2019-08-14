package kr.ac.postech.sslab.adapter;

import kr.ac.postech.sslab.nft.BaseNFT;
import kr.ac.postech.sslab.nft.DocNFT;
import kr.ac.postech.sslab.nft.OwnerNFT;
import kr.ac.postech.sslab.nft.SigNFT;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.json.simple.parser.ParseException;

public class NFTAdapter implements INFT {
    private ITypeNFT nft;
 
    public NFTAdapter(String type) throws Throwable {
        switch (type) {
            case "b_":
                nft = new BaseNFT();
                break;

            case "d_":
                nft = new DocNFT();
                break;

            case "s_":
                nft = new SigNFT();
                break;

            case "o_":
                nft = new OwnerNFT();
                break;

            default:
                throw new Throwable(String.format("No such a type %s", type));
        }
    }
 
    @Override
    public void mint(ChaincodeStub stub, String id, String owner, String uri) {
        nft.mint(stub, id, owner, uri);
    }

    @Override
    public void burn(ChaincodeStub stub, String id) {
        nft.burn(stub, id);
    }

    @Override
    public String read(ChaincodeStub stub, String id) throws ParseException {
        return nft.read(stub, id);
    }

    @Override
    public void setOwner(ChaincodeStub stub, String owner) {
        nft.setOwner(stub, owner);
    }

    @Override
    public String getOwner() {
        return nft.getOwner();
    }

    @Override
    public void setOperator(ChaincodeStub stub, String operator) {
        nft.setOperator(stub, operator);
    }

    @Override
    public String getOperator() {
        return nft.getOperator();
    }

    @Override
    public void setUri(ChaincodeStub stub, String uri) {
        nft.setUri(stub, uri);
    }

    @Override
    public String getUri() {
        return nft.getUri();
    }
 }