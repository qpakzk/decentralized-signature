package kr.ac.postech.sslab.standard;

import kr.ac.postech.sslab.msg_type.MsgEditNFTMetadata;
import kr.ac.postech.sslab.msg_type.MsgTransferNFT;
import kr.ac.postech.sslab.structure.Token;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.json.simple.JSONObject;

import java.util.Iterator;
import java.util.List;

public class ERC721 extends ChaincodeBase implements IERC721 {
    private static Log _logger = LogFactory.getLog(ERC721.class);
	private FabNFTManager nftManager;

	public ERC721() {
		this.nftManager = new FabNFTManager();
	}

	protected FabNFTManager getNFTManager() {
		return this.nftManager;
	}

	@Override
	public Response init(ChaincodeStub stub) {
        try {
            _logger.info("Init java ERC721 chaincode");
            String func = stub.getFunction();

            if (!func.equals("init")) {
                return newErrorResponse("Method other than init is not supported");
            }

            List<String> args = stub.getParameters();
            if (args.size() != 0) {
                newErrorResponse("Incorrect number of arguments. Expecting 0");
            }

            return newSuccessResponse();
        } catch (Throwable e) {
            return newErrorResponse(e);
        }
	}
	
    @Override
    public Response invoke(ChaincodeStub stub) {
	    try {
            _logger.info("Invoke java ERC721 chaincode");
            String func = stub.getFunction();
            List<String> args = stub.getParameters();

            if(func.equals("balanceOf")) {
                return this.balanceOf(stub, args);
            }
            else if(func.equals("ownerOf")) {
                return this.ownerOf(stub, args);
            }
            else if(func.equals("transferFrom")) {
                return this.transferFrom(stub, args);
            }
            else if(func.equals("approve")) {
                return this.approve(stub, args);
            }
            else if(func.equals("setApprovalForAll")) {
                return this.setApprovalForAll(stub, args);
            }
            else if(func.equals("getApproved")) {
                return this.getApproved(stub, args);
            }
            else if(func.equals("isApprovedForAll")) {
                return this.isApprovedForAll(stub, args);
            }

            return newErrorResponse("Invalid invoke method name. Expecting one of: "
                    + "[\"balanceOf\", \"ownerOf\", \"transferFrom\", \"approve\", \"setApprovalForAll\", \"getApproved\", \"isApprovedForAll\"]");
        } catch (Throwable e) {
            return newErrorResponse(e);
        }
	}

	@Override
	public Response balanceOf(ChaincodeStub stub, List<String> args) {
		if(args.size() != 1)
			return newErrorResponse("Incorrect number of arguments, expecting 1");
		
		String _owner = args.get(0);

		long balance = nftManager.queryNumberOfNFTs(stub, _owner);
		
		return newSuccessResponse(String.valueOf(balance));
	}

	@Override
	public Response ownerOf(ChaincodeStub stub, List<String> args) {
		if(args.size() != 1) {
            return newErrorResponse("Incorrect number of arguments, expecting 1");
        }

		String _tokenId = args.get(0);
		
		String owner = nftManager.queryOwner(stub, _tokenId);
		if(owner == null)
			return newErrorResponse(String.format("No such a token that has id %s", _tokenId));
		return newSuccessResponse(owner);
	}

	@Override
	public Response transferFrom(ChaincodeStub stub, List<String> args) {
		if(args.size() != 3) {
            return newErrorResponse("Incorrect number of arguments. Expecting 3");
        }

		String _from = args.get(0);
		String _to = args.get(1);
		String _tokenId = args.get(2);

        MsgTransferNFT msg = new MsgTransferNFT(_from, _to, _tokenId);

        if(nftManager.transfer(stub, msg)) {
            return newSuccessResponse("Succeeded transfer method");
        }
        else {
            return newErrorResponse("Failed transfer method");
        }
	}

	@Override
	public Response approve(ChaincodeStub stub, List<String> args) {
		if(args.size() != 2) {
            return newErrorResponse("Incorrect number of arguments. Expecting 2");
        }

		String _approved = args.get(0);
		String _tokenId = args.get(1);

        MsgEditNFTMetadata msg = new MsgEditNFTMetadata(_approved, _tokenId);
        nftManager.edit(stub, msg);

        return newSuccessResponse("Succeeded approve method");
	}
	
	@Override
	public Response setApprovalForAll(ChaincodeStub stub, List<String> args) {
		if(args.size() != 2)
			return newErrorResponse("Incorrect number of arguments. Expecting 2");
		
		String _operator = args.get(0);
		boolean _approved = Boolean.valueOf(args.get(1));

		byte[] _creator = stub.getCreator();
		String creator = String.valueOf(_creator);

		try {
			return newSuccessResponse();
		} catch (Throwable e) {
			return newErrorResponse();
		}
	}

	@Override
    public Response getApproved(ChaincodeStub stub, List<String> args) {
		if(args.size() != 1)
			return newErrorResponse("Incorrect number of arguments. Expecting 1");
		
		String _tokenId = args.get(0);
		String operator = nftManager.queryOperator(stub, _tokenId);

		if(operator == null) {
		    return newErrorResponse(String.format("Invalid NFT %s", _tokenId));
        }

		return newSuccessResponse(operator);
	}

	@Override
	public Response isApprovedForAll(ChaincodeStub stub, List<String> args) {
		if(args.size() != 2)
			return newErrorResponse("Incorrect number of arguments. Expecting 2");
		
		String _owner = args.get(0);
		String _operator = args.get(1);

		List<String> nftIds = nftManager.queryIDsByOwner(stub, _owner);

		if(nftIds == null) {
		    return newSuccessResponse("false");
        }

        for(Iterator<String> it = nftIds.iterator(); it.hasNext(); ) {
            if(it.next().equals(_operator)) {
                return newSuccessResponse("true");
            }
        }

		return newSuccessResponse("false");
	}

	public static void main(String[] args) {
        new ERC721().start(args);
    }
}