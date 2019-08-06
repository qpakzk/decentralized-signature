package kr.ac.postech.sslab.standard;

import kr.ac.postech.sslab.msg_type.MsgEditNFTMetadata;
import kr.ac.postech.sslab.msg_type.MsgTransferNFT;

import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;

import java.util.Iterator;
import java.util.List;

public class ERC721 extends ChaincodeBase implements IERC721 {
	private HFNFTMP hfnftmp;

	public ERC721() {
		this.hfnftmp = new HFNFTMP();
	}

	protected HFNFTMP getNFTManager() {
		return this.hfnftmp;
	}

	@Override
	public Response init(ChaincodeStub stub) {
        try {
            String func = stub.getFunction();

            if (!func.equals("init")) {
            	throw new Throwable("Method other than init is not supported");
            }

            List<String> args = stub.getParameters();
            if (args.size() != 0) {
            	throw new Throwable("Incorrect number of arguments. Expecting 0");
            }

            return ResponseUtils.newSuccessResponse();
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
	}
	
    @Override
    public Response invoke(ChaincodeStub stub) {
	    try {
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

            throw new Throwable("Invalid invoke method name. Expecting one of: "
                    + "[\"balanceOf\", \"ownerOf\", \"transferFrom\", \"approve\", \"setApprovalForAll\", \"getApproved\", \"isApprovedForAll\"]");
        } catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
	}

	@Override
	public Response balanceOf(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("Incorrect number of arguments. Expecting 1");
			}

			String _owner = args.get(0);

			long balance = hfnftmp.queryNumberOfNFTs(stub, _owner);

			return ResponseUtils.newSuccessResponse(String.valueOf(balance));
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
	public Response ownerOf(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("Incorrect number of arguments. Expecting 1");
			}

			String _tokenId = args.get(0);

			String owner = hfnftmp.queryOwner(stub, _tokenId);
			if (owner == null) {
				throw new Throwable(String.format("No such a NFT that has id %s", _tokenId));
			}

			return ResponseUtils.newSuccessResponse(owner);
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
	public Response transferFrom(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 3) {
				throw new Throwable("Incorrect number of arguments. Expecting 3");
			}

			String _from = args.get(0);
			String _to = args.get(1);
			String _tokenId = args.get(2);

			MsgTransferNFT msg = new MsgTransferNFT(_from, _to, _tokenId);


			hfnftmp.transfer(stub, msg);
			return ResponseUtils.newSuccessResponse();
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
	public Response approve(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 2) {
				throw new Throwable("Incorrect number of arguments. Expecting 2");
			}

			String _approved = args.get(0);
			String _tokenId = args.get(1);

			MsgEditNFTMetadata msg = new MsgEditNFTMetadata(_approved, _tokenId);
			hfnftmp.edit(stub, msg);

			return ResponseUtils.newSuccessResponse();
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}
	
	@Override
	public Response setApprovalForAll(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 2) {
				throw new Throwable("Incorrect number of arguments. Expecting 2");
			}

			String _operator = args.get(0);
			boolean _approved = Boolean.valueOf(args.get(1));

			byte[] _creator = stub.getCreator();
			String creator = String.valueOf(_creator);

			return ResponseUtils.newSuccessResponse();
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
    public Response getApproved(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("Incorrect number of arguments. Expecting 1");
			}

			String _tokenId = args.get(0);
			String operator = hfnftmp.queryOperator(stub, _tokenId);

			if (operator == null) {
				throw new Throwable(String.format("Invalid NFT %s", _tokenId));
			}

			return ResponseUtils.newSuccessResponse(operator);
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
	public Response isApprovedForAll(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 2) {
				throw new Throwable("Incorrect number of arguments. Expecting 2");
			}

			String _owner = args.get(0);
			String _operator = args.get(1);

			List<String> nftIds = hfnftmp.queryIDsByOwner(stub, _owner);

			if (nftIds == null) {
				return ResponseUtils.newSuccessResponse("false");
			}

			for (Iterator<String> it = nftIds.iterator(); it.hasNext(); ) {
				if (it.next().equals(_operator)) {
					return ResponseUtils.newSuccessResponse("true");
				}
			}

			return ResponseUtils.newSuccessResponse("false");
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	public static void main(String[] args) {
        new ERC721().start(args);
    }
}