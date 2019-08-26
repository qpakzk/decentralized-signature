package kr.ac.postech.sslab.extension;

import kr.ac.postech.sslab.adapter.XAtt;
import kr.ac.postech.sslab.nft.BaseNFT;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;

import java.util.*;

import kr.ac.postech.sslab.standard.*;
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.simple.JSONObject;

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

				case "mint":
					return this.mint(stub, args);

				case "divide":
					return this.divide(stub, args);

				case "delete":
					return this.delete(stub, args);

				case "updateXAtt":
					return this.updateXAtt(stub, args);

				case "updateUri":
					return this.updateUri(stub, args);

				case "queryToken":
					return this.queryToken(stub, args);

				case "queryHistory":
					return this.queryHistory(stub, args);

				case "burn":
					return this.burn(stub, args);

				default:
					throw new Throwable("Invalid invoke method name. Expecting one of: "
							+ "[\"balanceOf\", \"ownerOf\", \"transferFrom\", \"approve\", \"setApprovalForAll\", \"getApproved\", \"isApprovedForAll\", "
							+ "\"mint\", \"divide\", \"delete\", \"update\", \"query\", \"queryHistory\"]");
			}

		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
	public Response mint(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 5) {
				throw new Throwable("Incorrect number of arguments. Expecting 5");
			}

			String _id = args.get(0).toLowerCase();
			String _type = args.get(1).toLowerCase();
			String _owner = args.get(2).toLowerCase();
			String _xatt = args.get(3).toLowerCase();
			String _uri = args.get(4).toLowerCase();

			BaseNFT nft = new BaseNFT();
			nft.mint(stub, _id, _type, _owner, _xatt, _uri);

			return ResponseUtils.newSuccessResponse();
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

			String _id = args.get(0);

			BaseNFT nft = BaseNFT.read(stub, _id);

			BaseNFT dup = new BaseNFT();
			dup.mint(stub, _id, nft.getType(), nft.getOwner(), nft.getXAtt().toJSONString(), nft.getUri().toJSONString());
			dup.setOperator(stub, nft.getOperator());
			dup.setApproved(stub, nft.getApproved());

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

			String _id = args.get(0);

			BaseNFT nft = BaseNFT.read(stub, _id);

			XAtt xatt = nft.getXAtt();
			xatt.deactivate();
			nft.setXAtt(stub, xatt);

			return ResponseUtils.newSuccessResponse();
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
    public Response updateXAtt(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 2) {
				throw new Throwable("Incorrect number of arguments. Expecting 2");
			}

			String _xatt = args.get(0);
			String _id = args.get(1);

			BaseNFT nft = BaseNFT.read(stub, _id);
			nft.setXAtt(stub, new XAtt(_xatt, nft.getType()));

			return ResponseUtils.newSuccessResponse();
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
	public Response updateUri(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 2) {
				throw new Throwable("Incorrect number of arguments. Expecting 2");
			}

			String _uri = args.get(0);
			String _id = args.get(1);

			BaseNFT nft = BaseNFT.read(stub, _id);
			nft.setXAtt(stub, new XAtt(_uri, nft.getType()));

			return ResponseUtils.newSuccessResponse();
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
    public Response queryToken(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("Incorrect number of arguments. Expecting 1");
			}

			String _id = args.get(0);

			BaseNFT nft = BaseNFT.read(stub, _id);
			Map<String, String> map = new HashMap<>();
			map.put("id", nft.getId());
			map.put("owner", nft.getOwner());
			map.put("operator", nft.getOperator().toString());
			map.put("approved", nft.getApproved());
			map.put("xatt", nft.getXAtt().toJSONString());
			map.put("uri", nft.getUri().toJSONString());

			String _query = new JSONObject(map).toJSONString();
			return ResponseUtils.newSuccessResponse(_query);
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
    public Response queryHistory(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("Incorrect number of arguments. Expecting 1");
			}

			String _id = args.get(0);

			List<String> history = new LinkedList<>();
			QueryResultsIterator<KeyModification> resultsIterator = stub.getHistoryForKey(_id);
			while (resultsIterator.iterator().hasNext()) {
				history.add(resultsIterator.iterator().next().getStringValue());
			}

			Iterator<String> it = history.iterator();
			String result = "";
			while (it.hasNext()) {
				result += it.next() + ", ";
			}

			result = result.substring(0, result.length() - 1);
			return ResponseUtils.newSuccessResponse(result);
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}


	public Response burn(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("Incorrect number of arguments. Expecting 1");
			}

			String _id = args.get(0).toLowerCase();

			BaseNFT nft = BaseNFT.read(stub, _id);
			nft.burn(stub, _id);

			return ResponseUtils.newSuccessResponse();
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	public static void main(String[] args) {
        new EERC721().start(args);
    }
}