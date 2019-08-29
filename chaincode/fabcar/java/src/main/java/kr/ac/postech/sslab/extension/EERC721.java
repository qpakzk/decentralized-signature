package kr.ac.postech.sslab.extension;

import kr.ac.postech.sslab.adapter.XAtt;
import kr.ac.postech.sslab.nft.NFT;
import kr.ac.postech.sslab.type.URI;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;

import java.util.*;

import kr.ac.postech.sslab.standard.*;
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.xml.ws.Response;

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

				case "deactivate":
					return this.deactivate(stub, args);

				case "setXAtt":
					return this.setXAtt(stub, args);

				case "getXAtt":
					return this.getXAtt(stub, args);

				case "setUri":
					return this.setUri(stub, args);

				case "getUri":
					return this.getUri(stub, args);

				case "query":
					return this.query(stub, args);

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
	public Response balanceOf(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() == 1) {
				return super.balanceOf(stub, args);
			}

			if (args.size() != 2) {
				throw new Throwable("Incorrect number of arguments. Expecting 1 or 2.");
			}

			String owner = args.get(0).toLowerCase();
			String type = args.get(1).toLowerCase();

			if (owner.length() == 0) {
				throw new Throwable("Incorrect owner");
			}

			if (type.length() == 0) {
				throw new Throwable("Incorrect token type");
			}

			long ownedTokensCount = this._balanceOf(stub, owner, type);

			return ResponseUtils.newSuccessResponse(Long.toString(ownedTokensCount));
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	private long _balanceOf(ChaincodeStub stub, String owner, String type) throws ParseException {
		String query = "{\"selector\":{\"owner\":\"" + owner + "\"}}";

		long ownedTokensCount = 0;
		QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(query);
		while(resultsIterator.iterator().hasNext()) {
			String id = resultsIterator.iterator().next().getKey();
			NFT nft = NFT.read(stub, id);

			if (nft.getType().equals(type)) {
				ownedTokensCount++;
			}
		}

		return ownedTokensCount;
	}

	@Override
	public Response mint(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 5) {
				throw new Throwable("Incorrect number of arguments. Expecting 5");
			}

			String id = args.get(0).toLowerCase();
			String type = args.get(1).toLowerCase();
			String owner = args.get(2).toLowerCase();
			String xatt = args.get(3).toLowerCase();
			String uri = args.get(4).toLowerCase();

			/*
			if (!owner.equals(msg.sender)) {
				throw new Throwable("Owner should be caller");
			}
			*/

			NFT nft = new NFT();
			nft.mint(stub, id, type, owner, xatt, uri);

			return ResponseUtils.newSuccessResponse("Succeeded mint");
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
	public Response divide(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 2) {
				throw new Throwable("Incorrect number of arguments. Expecting 2");
			}

			String id = args.get(0).toLowerCase();
			String newId = args.get(1).toLowerCase();

			/*
			String owner = this._ownerOf(stub, id);
			if (!owner.equals(msg.sender) && !this._isApprovedForAll(stub, owner, msg.sender)) {
				throw new Throwable("Caller is not owner nor approved for all");
			}
			*/

			NFT nft = NFT.read(stub, id);

			if (!nft.checker()) {
				throw new Throwable("Cannot transfer");
			}

			NFT dupNft = new NFT();
			dupNft.mint(stub, newId, nft.getType(), nft.getOwner(), nft.getXAtt().toJSONString(), nft.getUri().toJSONString());
			dupNft.setOperator(stub, nft.getOperator());
			dupNft.setApproved(stub, nft.getApproved());

			return ResponseUtils.newSuccessResponse();
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
    public Response deactivate(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("Incorrect number of arguments. Expecting 1");
			}

			String id = args.get(0).toLowerCase();

			/*
			String owner = this._ownerOf(stub, id);
			if (!owner.equals(msg.sender) && !this._isApprovedForAll(stub, owner, msg.sender)) {
				throw new Throwable("Caller is not owner nor approved for all");
			}
			*/

			NFT nft = NFT.read(stub, id);

			XAtt xatt = nft.getXAtt();
			xatt.deactivate();
			nft.setXAtt(stub, xatt);

			return ResponseUtils.newSuccessResponse();
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
	public Response getXAtt(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("Incorrect number of arguments. Expecting 1");
			}

			String id = args.get(0).toLowerCase();

			NFT nft = NFT.read(stub, id);

			XAtt xatt = nft.getXAtt();

			return ResponseUtils.newSuccessResponse(xatt.toJSONString());
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
	public Response setXAtt(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 2) {
				throw new Throwable("Incorrect number of arguments. Expecting 2");
			}

			String xatt = args.get(0).toLowerCase();
			String id = args.get(1).toLowerCase();

			/*
			String owner = this._ownerOf(stub, id);
			if (!owner.equals(msg.sender) && !this._isApprovedForAll(stub, owner, msg.sender)) {
				throw new Throwable("Caller is not owner nor approved for all");
			}
			*/

			NFT nft = NFT.read(stub, id);

			if (!nft.checker()) {
				throw new Throwable("Cannot setXAtt");
			}

			nft.setXAtt(stub, new XAtt(xatt, nft.getType()));

			return ResponseUtils.newSuccessResponse();
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
	public Response getUri(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("Incorrect number of arguments. Expecting 1");
			}

			String id = args.get(0).toLowerCase();

			NFT nft = NFT.read(stub, id);

			URI uri = nft.getUri();

			return ResponseUtils.newSuccessResponse(uri.toJSONString());
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	@Override
	public Response setUri(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 2) {
				throw new Throwable("Incorrect number of arguments. Expecting 2");
			}

			String uri = args.get(0).toLowerCase();
			String id = args.get(1).toLowerCase();

			/*
			String owner = this._ownerOf(stub, id);
			if (!owner.equals(msg.sender) && !this._isApprovedForAll(stub, owner, msg.sender)) {
				throw new Throwable("Caller is not owner nor approved for all");
			}
			*/

			NFT nft = NFT.read(stub, id);

			if (!nft.checker()) {
				throw new Throwable("Cannot setUri");
			}

			nft.setUri(stub, new URI(uri));

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

			String id = args.get(0).toLowerCase();

			NFT nft = NFT.read(stub, id);
			Map<String, String> map = new HashMap<>();
			map.put("id", nft.getId());
			map.put("type", nft.getType());
			map.put("owner", nft.getOwner());
			map.put("operator", nft.getOperator().toString());
			map.put("approved", nft.getApproved());
			map.put("xatt", nft.getXAtt().toJSONString());
			map.put("uri", nft.getUri().toJSONString());

			String query = new JSONObject(map).toJSONString();
			return ResponseUtils.newSuccessResponse(query);
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

			String _id = args.get(0).toLowerCase();

			List<String> history = new LinkedList<>();
			QueryResultsIterator<KeyModification> resultsIterator = stub.getHistoryForKey(_id);
			while (resultsIterator.iterator().hasNext()) {
				history.add(resultsIterator.iterator().next().getStringValue());
			}

			Iterator<String> it = history.iterator();
			String result = "";
			while (it.hasNext()) {
				result += (it.next() + "  ");
			}
			return ResponseUtils.newSuccessResponse(result);
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}


	@Override
	public Response burn(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("Incorrect number of arguments. Expecting 1");
			}

			String id = args.get(0).toLowerCase();

			/*
			String owner = this._ownerOf(stub, id);
			if (!owner.equals(msg.sender) && !this._isApprovedForAll(stub, owner, msg.sender)) {
				throw new Throwable("Caller is not owner nor approved for all");
			}
			*/

			NFT nft = NFT.read(stub, id);
			nft.burn(stub, id);

			return ResponseUtils.newSuccessResponse();
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	public static void main(String[] args) {
        new EERC721().start(args);
    }
}