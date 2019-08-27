package kr.ac.postech.sslab.standard;

import kr.ac.postech.sslab.nft.Base;
import kr.ac.postech.sslab.nft.BaseNFT;
import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ERC721 extends ChaincodeBase implements IERC721 {
	@Override
	public Response init(ChaincodeStub stub) {
		try {
			String func = stub.getFunction();

			if (!func.equals("init")) {
				throw new Throwable("Method other than 'init' is not supported");
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

			String _owner = args.get(0).toLowerCase();

			if (_owner.length() == 0) {
				throw new Throwable("Incorrect owner.");
			}


			long ownedTokensCount = _balanceOf(stub, _owner);

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

			String _id = args.get(0).toLowerCase();

			if (_id.length() == 0) {
				throw new Throwable("Incorrect token id");
			}

			String owner = _ownerOf(stub, _id);

			return ResponseUtils.newSuccessResponse(owner);
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	private String _ownerOf(ChaincodeStub stub, String _id) throws ParseException {
		BaseNFT nft = BaseNFT.read(stub, _id);
		return nft.getOwner();
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

			if (_from.length() == 0) {
				throw new Throwable("Incorrect sender");
			}

			if (_to.length() == 0) {
				throw new Throwable("Incorrect recipient");
			}

			if (_id.length() == 0) {
				throw new Throwable("Incorrect token id");
			}

			_transferFrom(stub, _from, _to, _id);

			return ResponseUtils.newSuccessResponse();
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	private void _transferFrom(ChaincodeStub stub, String _from, String _to, String _id) throws Throwable {
		BaseNFT nft = BaseNFT.read(stub, _id);


		//isApprovedForAll(stub, msg.sender, _id);

		if (!_from.equals(nft.getOwner())) {
			throw new Throwable("transfer error");
		}

		nft.setOwner(stub, _to);
	}

	private boolean isApprovedOrOwner(ChaincodeStub stub, String _spender, String _id) throws ParseException {
		String _owner = _ownerOf(stub, _id);
		String _approved = _getApproved(stub, _id);

		if (_spender.equals(_owner) || _spender.equals(_approved) || _isApprovedForAll(stub, _owner, _spender)) {
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

			String _approved = args.get(0).toLowerCase();
			String _id = args.get(1).toLowerCase();

			BaseNFT nft = BaseNFT.read(stub, _id);

			//String _owner = _ownerOf(stub, _id);
			//msg.sender == _owner || _isApprovedForAll(stub, _owner, msg.sender);

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

			String _owner = args.get(0).toLowerCase();
			String _operator = args.get(1).toLowerCase();
			boolean _approved = Boolean.parseBoolean(args.get(2));

			if (_owner.equals(_operator)) {
				throw new Throwable("setApprovalForAll error");
			}

			String query = "{\"selector\":{\"owner\":\"" + _owner + "\"}}";
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

			String _id = args.get(0).toLowerCase();

			String approved = _getApproved(stub, _id);

			return ResponseUtils.newSuccessResponse(approved);
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	private String _getApproved(ChaincodeStub stub, String _id) throws ParseException {
		BaseNFT nft = BaseNFT.read(stub, _id);

		return nft.getApproved();
	}

	@Override
	public Response isApprovedForAll(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 2) {
				throw new Throwable("Incorrect number of arguments. Expecting 2");
			}

			String _owner = args.get(0).toLowerCase();
			String _operator = args.get(1).toLowerCase();

			boolean _result = _isApprovedForAll(stub, _owner, _operator);

			return ResponseUtils.newSuccessResponse(Boolean.toString(_result));
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse("isApprovedForAll error");
		}
	}

	private boolean _isApprovedForAll(ChaincodeStub stub, String _owner, String _operator) throws ParseException {
		String query = "{\"selector\":{\"owner\":\"" + _owner + "\"}}";
		QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(query);

		while(resultsIterator.iterator().hasNext()) {
			String id = resultsIterator.iterator().next().getKey();
			BaseNFT nft = BaseNFT.read(stub, id);
			if(!_operator.equals(nft.getOperator().toString())) {
				return false;
			}
		}

		return true;
	}

	public static void main(String[] args) {
        new ERC721().start(args);
    }
}