package kr.ac.postech.sslab.extension;

import kr.ac.postech.sslab.msg_type.MsgEditNFTMetadata;
import kr.ac.postech.sslab.msg_type.MsgMintNFT;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.shim.ChaincodeStub;
import java.util.List;

import kr.ac.postech.sslab.standard.*;

public class EERC721 extends ERC721 implements IEERC721 {
	private static Log _logger = LogFactory.getLog(EERC721.class);
	private HFNFTMP hfnftmp;

	public EERC721() {
		super();
		this.hfnftmp = super.getNFTManager();
	}

	@Override
	public Response init(ChaincodeStub stub) {
		try {
			_logger.info("Init java EERC721 chaincode");
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
			_logger.info("Invoke java EERC721 chaincode");
			String func = stub.getFunction();
			List<String> args = stub.getParameters();

			if(func.equals("balanceOf")) {
				return super.balanceOf(stub, args);
			}
			else if(func.equals("ownerOf")) {
				return super.ownerOf(stub, args);
			}
			else if(func.equals("transferFrom")) {
				return super.transferFrom(stub, args);
			}
			else if(func.equals("approve")) {
				return super.approve(stub, args);
			}
			else if(func.equals("setApprovalForAll")) {
				return super.setApprovalForAll(stub, args);
			}
			else if(func.equals("getApproved")) {
				return super.getApproved(stub, args);
			}
			else if(func.equals("isApprovedForAll")) {
				return super.isApprovedForAll(stub, args);
			}
			else if (func.equals("mint")) {
				return this.mint(stub, args);
			}
			else if (func.equals("divide")) {
				return this.divide(stub, args);
			}
			else if (func.equals("delete")) {
				return this.delete(stub, args);
			}
			else if (func.equals("update")) {
				return this.update(stub, args);
			}
			else if (func.equals("query")) {
				return this.query(stub, args);
			}
			else if (func.equals("queryTokenHistory")) {
				return this.queryTokenHistory(stub, args);
			}

			return newErrorResponse("Invalid invoke method name. Expecting one of: "
					+ "[\"balanceOf\", \"ownerOf\", \"transferFrom\", \"approve\", \"setApprovalForAll\", \"getApproved\", \"isApprovedForAll\", "
					+ "\"mint\", \"divide\", \"delete\", \"update\", \"query\", \"queryTokenHistory\"]");
		} catch (Throwable e) {
			return newErrorResponse(e);
		}
	}

	@Override
	public Response mint(ChaincodeStub stub, List<String> args) {
		if(args.size() != 2) {
			return newErrorResponse("Incorrect number of arguments. Expecting 2");
		}

		String _hash = args.get(0);
		String _uri = args.get(1);

		MsgMintNFT msg = new MsgMintNFT(_hash, _uri);

		String nftString = this.hfnftmp.mint(stub, msg);

		if(nftString == null) {
			return newErrorResponse("NFT not minted.");
		}

		return newSuccessResponse(nftString);
	}

	@Override
	public Response divide(ChaincodeStub stub, List<String> args) {
		if(args.size() != 1) {
			return newErrorResponse("Incorrect number of arguments. Expecting 1");
		}

		String _tokenId = args.get(0);
		return newSuccessResponse();
	}

	@Override
    public Response delete(ChaincodeStub stub, List<String> args) {
		if(args.size() != 1) {
			return newErrorResponse("Incorrect number of arguments. Expecting 1");
		}

		String _tokenId = args.get(0);

		hfnftmp.deactivate(stub, _tokenId);
		return newSuccessResponse();
	}

	@Override
    public Response update(ChaincodeStub stub, List<String> args) {
		if(args.size() != 3) {
			return newErrorResponse("Incorrect number of arguments. Expecting 3");
		}

		String _hash = args.get(0);
		String _uri = args.get(1);
		String _tokenId = args.get(2);

		MsgEditNFTMetadata msg = new MsgEditNFTMetadata(_tokenId, _hash, _uri);
		hfnftmp.edit(stub, msg);
		return newSuccessResponse();
	}

	@Override
    public Response query(ChaincodeStub stub, List<String> args) {
		if(args.size() != 1) {
			return newErrorResponse("Incorrect number of arguments. Expecting 1");
		}

		String _tokenId = args.get(0);

		String nftString = hfnftmp.query(stub, _tokenId);

		return newSuccessResponse(nftString);
	}

	@Override
    public Response queryTokenHistory(ChaincodeStub stub, List<String> args) {
		if(args.size() != 1) {
			return newErrorResponse("Incorrect number of arguments. Expecting 1");
		}

		String _tokenId = args.get(0);

		String histories = hfnftmp.queryHistory(stub, _tokenId);

		return newSuccessResponse(histories);
	}

	public static void main(String[] args) {
        new EERC721().start(args);
    }
}