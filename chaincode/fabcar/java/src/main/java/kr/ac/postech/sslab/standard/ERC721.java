package kr.ac.postech.sslab.standard;

import kr.ac.postech.sslab.nft.NFT;
import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.simple.parser.ParseException;
import java.util.List;

public class ERC721 extends ChaincodeBase implements IERC721 {
	@Override
	public Response init(ChaincodeStub stub) {
		try {
			String func = stub.getFunction();

			if (!func.equals("init")) {
				throw new Throwable("Functions other than init are not supported");
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

            switch (func) {
				case "balanceOf":
					return this.balanceOf(stub, args);

				case "ownerOf":
					return this.ownerOf(stub, args);

				case "transferFrom":
					return this.transferFrom(stub, args);

				case "approve":
					return this.approve(stub, args);

				case "setApprovalForAll":
					return this.setApprovalForAll(stub, args);

				case "getApproved":
					return this.getApproved(stub, args);

				case "isApprovedForAll":
					return this.isApprovedForAll(stub, args);

				default:
					throw new Throwable("Invalid invoke method name. Expecting one of: "
							+ "[\"balanceOf\", \"ownerOf\", \"transferFrom\", \"approve\", \"setApprovalForAll\", \"getApproved\", \"isApprovedForAll\"]");
			}

		} catch (Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable.getMessage());
        }
	}

	@Override
	public Response balanceOf(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("Incorrect number of arguments. Expecting 1.");
			}

			String owner = args.get(0).toLowerCase();

			if (owner.length() == 0) {
				throw new Throwable("Incorrect owner");
			}

			long ownedTokensCount = this._balanceOf(stub, owner);

			return ResponseUtils.newSuccessResponse(Long.toString(ownedTokensCount));
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	private long _balanceOf(ChaincodeStub stub, String _owner) {
		String query = "{\"selector\":{\"owner\":\"" + _owner + "\"}}";

		long ownedTokensCount = 0;
		QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(query);
		while(resultsIterator.iterator().hasNext()) {
			resultsIterator.iterator().next();
			ownedTokensCount++;
		}

		return ownedTokensCount;
	}

	@Override
	public Response ownerOf(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("Incorrect number of arguments. Expecting 1");
			}

			String id = args.get(0).toLowerCase();

			if (id.length() == 0) {
				throw new Throwable("Incorrect token id");
			}

			String owner = this._ownerOf(stub, id);

			return ResponseUtils.newSuccessResponse(owner);
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	protected String _ownerOf(ChaincodeStub stub, String id) throws ParseException {
		NFT nft = NFT.read(stub, id);
		return nft.getOwner();
	}

	@Override
	public Response transferFrom(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 3) {
				throw new Throwable("Incorrect number of arguments. Expecting 3");
			}

			String from = args.get(0).toLowerCase();
			String to = args.get(1).toLowerCase();
			String id = args.get(2).toLowerCase();

			if (from.length() == 0) {
				throw new Throwable("Incorrect sender");
			}

			if (to.length() == 0) {
				throw new Throwable("Incorrect recipient");
			}

			if (id.length() == 0) {
				throw new Throwable("Incorrect token id");
			}

			NFT nft = NFT.read(stub, id);

			if (!nft.checker()) {
				throw new Throwable("Cannot transfer");
			}

			/*
			if (!this._isApprovedOrOwner(stub, msg.sender, id)) {
				throw new Throwable("You should be owner or approved");
			}
			*/

			if (!from.equals(nft.getOwner())) {
				throw new Throwable("Transfer of token that is not own");
			}

			nft.setOwner(stub, to);

			return ResponseUtils.newSuccessResponse("Succeeded transferFrom");
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	protected boolean _isApprovedOrOwner(ChaincodeStub stub, String spender, String id) throws ParseException {
		String owner = this._ownerOf(stub, id);
		String approved = this._getApproved(stub, id);

		if (spender.equals(owner) || spender.equals(approved) || this._isApprovedForAll(stub, owner, spender)) {
			return true;
		}

		return false;
	}

	@Override
	public Response approve(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 2) {
				throw new Throwable("Incorrect number of arguments. Expecting 2");
			}

			String approved = args.get(0).toLowerCase();
			String id = args.get(1).toLowerCase();

			NFT nft = NFT.read(stub, id);

			if (!nft.checker()) {
				throw new Throwable("Cannot approve");
			}

			/*
			String owner = this._ownerOf(stub, id);
			if (!owner.equals(msg.sender) && !this._isApprovedForAll(stub, owner, msg.sender)) {
				throw new Throwable("Approve caller is not owner nor approved for all");
			}
			*/

			nft.setApproved(stub, approved);

			return ResponseUtils.newSuccessResponse("Succeeded approve");
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}
	
	@Override
	public Response setApprovalForAll(ChaincodeStub stub, List<String> args) {
		try {
			//should be modified as 2 arguments
			if (args.size() != 3) {
				throw new Throwable("Incorrect number of arguments. Expecting 3");
			}

			//user should be deleted
			String caller = args.get(0).toLowerCase();
			String operator = args.get(1).toLowerCase();
			boolean approved = Boolean.parseBoolean(args.get(2));

			if (operator.equals(caller)) {
				throw new Throwable("Approve to caller");
			}

			String query = "{\"selector\":{\"owner\":\"" + caller + "\"}}";
			QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(query);
			if (approved) {
				while(resultsIterator.iterator().hasNext()) {
					String id = resultsIterator.iterator().next().getKey();
					NFT nft = NFT.read(stub, id);

					if (!nft.checker()) {
						throw new Throwable("Cannot call setApprovalForAll");
					}

					nft.setOperator(stub, nft.getOperator().add(operator));
				}
			}
			else {
				while(resultsIterator.iterator().hasNext()) {
					String id = resultsIterator.iterator().next().getKey();
					NFT nft = NFT.read(stub, id);

					if (!nft.checker()) {
						throw new Throwable("Cannot call setApprovalForAll");
					}

					nft.setOperator(stub, nft.getOperator().remove(operator));
				}
			}

			return ResponseUtils.newSuccessResponse("Succeeded setApprovalForAll");
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

			String id = args.get(0).toLowerCase();

			String approved = this._getApproved(stub, id);

			return ResponseUtils.newSuccessResponse(approved);
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	protected String _getApproved(ChaincodeStub stub, String id) throws ParseException {
		NFT nft = NFT.read(stub, id);

		return nft.getApproved();
	}

	@Override
	public Response isApprovedForAll(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 2) {
				throw new Throwable("Incorrect number of arguments. Expecting 2");
			}

			String owner = args.get(0).toLowerCase();
			String operator = args.get(1).toLowerCase();

			boolean result = this._isApprovedForAll(stub, owner, operator);

			return ResponseUtils.newSuccessResponse(Boolean.toString(result));
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	protected boolean _isApprovedForAll(ChaincodeStub stub, String owner, String operator) throws ParseException {
		String query = "{\"selector\":{\"owner\":\"" + owner + "\"}}";
		QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(query);

		while(resultsIterator.iterator().hasNext()) {
			String id = resultsIterator.iterator().next().getKey();
			NFT nft = NFT.read(stub, id);
			if(!nft.getOperator().existOperator(operator)) {
				return false;
			}
		}

		return true;
	}

	public static void main(String[] args) {
        new ERC721().start(args);
    }
}