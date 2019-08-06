package kr.ac.postech.sslab.extension;

import kr.ac.postech.sslab.msg_type.MsgEditNFTMetadata;
import kr.ac.postech.sslab.msg_type.MsgMintNFT;

import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;
import java.util.List;

import kr.ac.postech.sslab.standard.*;

public class EERC721 extends ERC721 implements IEERC721 {
	@Override
	public Response init(ChaincodeStub stub) {
		return super.init(stub);
	}
	
    @Override
    public Response invoke(ChaincodeStub stub) {
		try {
			String func = stub.getFunction();
			List<String> args = stub.getParameters();

			switch (func) {
				case "balanceOf":
					return super.balanceOf(stub, args);

				case "ownerOf":
					return super.ownerOf(stub, args);

				case "transferFrom":
					return super.transferFrom(stub, args);

				case "approve":
					return super.approve(stub, args);

				case "setApprovalForAll":
					return super.setApprovalForAll(stub, args);

				case "getApproved":
					return super.getApproved(stub, args);

				case "isApprovedForAll":
					return super.isApprovedForAll(stub, args);

				case "mint":
					return this.mint(stub, args);

				case "divide":
					return this.divide(stub, args);

				case "delete":
					return this.delete(stub, args);

				case "update":
					return this.update(stub, args);

				case "query":
					return this.query(stub, args);

				case "queryTokenHistory":
					return this.queryTokenHistory(stub, args);

				default:
					throw new Throwable("Invalid invoke method name. Expecting one of: "
							+ "[\"balanceOf\", \"ownerOf\", \"transferFrom\", \"approve\", \"setApprovalForAll\", \"getApproved\", \"isApprovedForAll\", "
							+ "\"mint\", \"divide\", \"delete\", \"update\", \"query\", \"queryTokenHistory\"]");
			}

		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
	public Response mint(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 2) {
				throw new Throwable("Incorrect number of arguments. Expecting 2");
			}

			String _hash = args.get(0);
			String _uri = args.get(1);

			MsgMintNFT msg = new MsgMintNFT(_hash, _uri);

			String nftString = HFNFTMP.mint(stub, msg);

			if (nftString == null) {
				throw new Throwable("NFT not minted.");
			}

			return ResponseUtils.newSuccessResponse(nftString);
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
	public Response divide(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("Incorrect number of arguments. Expecting 1");
			}

			String _tokenId = args.get(0);
			return ResponseUtils.newSuccessResponse();
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
    public Response delete(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("Incorrect number of arguments. Expecting 1");
			}

			String _tokenId = args.get(0);

			HFNFTMP.deactivate(stub, _tokenId);
			return ResponseUtils.newSuccessResponse();
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
    public Response update(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 3) {
				throw new Throwable("Incorrect number of arguments. Expecting 3");
			}

			String _hash = args.get(0);
			String _uri = args.get(1);
			String _tokenId = args.get(2);

			MsgEditNFTMetadata msg = new MsgEditNFTMetadata(_tokenId, _hash, _uri);
			HFNFTMP.edit(stub, msg);
			return ResponseUtils.newSuccessResponse();
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
    public Response query(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("Incorrect number of arguments. Expecting 1");
			}

			String _tokenId = args.get(0);

			String nftString = HFNFTMP.query(stub, _tokenId);

			return ResponseUtils.newSuccessResponse(nftString);
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
    public Response queryTokenHistory(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("Incorrect number of arguments. Expecting 1");
			}

			String _tokenId = args.get(0);

			String histories = HFNFTMP.queryHistory(stub, _tokenId);

			return ResponseUtils.newSuccessResponse(histories);
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	public static void main(String[] args) {
        new EERC721().start(args);
    }
}