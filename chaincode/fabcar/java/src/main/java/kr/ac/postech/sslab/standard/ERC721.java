package kr.ac.postech.sslab.standard;

import kr.ac.postech.sslab.nft.BaseNFT;
import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.util.List;

public class ERC721 extends ChaincodeBase implements IERC721 {
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
				throw new Throwable("Incorrect number of arguments. Expecting 1");
			}

			String _owner = args.get(0);

			long ownedTokensCount = 0;
			String query = "{\"owner\":\"" + _owner + "\"}";

			QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(query);
			while(resultsIterator.iterator().hasNext()) {
				resultsIterator.iterator().next();
				ownedTokensCount++;
			}

			return ResponseUtils.newSuccessResponse(Long.toString(ownedTokensCount));
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

			String _id = args.get(0);

			BaseNFT nft = BaseNFT.read(stub, _id);

			String owner = nft.getOwner();
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

			String _from = args.get(0).toLowerCase();
			String _to = args.get(1).toLowerCase();
			String _id = args.get(2).toLowerCase();

			BaseNFT nft = BaseNFT.read(stub, _id);

			if (!_from.equals(nft.getOwner())) {
				throw new Throwable("HFNFTMP transfer error");
			}

			nft.setOwner(stub, _to);

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
			String _id = args.get(1);

			BaseNFT nft = BaseNFT.read(stub, _id);
			nft.setApproved(stub, _approved);

			return ResponseUtils.newSuccessResponse();
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse("HFNFTMP approve error");
		}
	}
	
	@Override
	public Response setApprovalForAll(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 3) {
				throw new Throwable("Incorrect number of arguments. Expecting 3");
			}

			String _owner = args.get(0);
			String _operator = args.get(1);
			boolean _approved = Boolean.parseBoolean(args.get(2));

			String query = "{\"owner\":\"" + _owner + "\"}";
			QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(query);
			if (_approved) {
				while(resultsIterator.iterator().hasNext()) {
					String id = resultsIterator.iterator().next().getKey();
					BaseNFT nft = BaseNFT.read(stub, id);
					nft.setOperator(stub, nft.getOperator().add(_operator));
				}
			}
			else {
				while(resultsIterator.iterator().hasNext()) {
					String id = resultsIterator.iterator().next().getKey();
					BaseNFT nft = BaseNFT.read(stub, id);
					nft.setOperator(stub, nft.getOperator().remove(_operator));
				}
			}

			return ResponseUtils.newSuccessResponse();
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse("HFNFTMP approve error");
		}
	}

	@Override
    public Response getApproved(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("Incorrect number of arguments. Expecting 1");
			}

			String _id = args.get(0);

			BaseNFT nft = BaseNFT.read(stub, _id);

			String approved = nft.getApproved();

			return ResponseUtils.newSuccessResponse(approved);
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

			String query = "{\"owner\":\"" + _owner + "\"}";
			QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(query);

			while(resultsIterator.iterator().hasNext()) {
				String id = resultsIterator.iterator().next().getKey();
				BaseNFT nft = BaseNFT.read(stub, id);
				if(!_operator.equals(nft.getOperator())) {
					return ResponseUtils.newSuccessResponse("false");
				}
			}

			return ResponseUtils.newSuccessResponse("true");
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse("HFNFTMP approve error");
		}
	}

	public static void main(String[] args) {
        new ERC721().start(args);
    }
}