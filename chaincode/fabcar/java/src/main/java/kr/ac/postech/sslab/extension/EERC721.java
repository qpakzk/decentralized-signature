package kr.ac.postech.sslab.extension;

import kr.ac.postech.sslab.adapter.XAttr;
import kr.ac.postech.sslab.nft.NFT;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;
import org.hyperledger.fabric.shim.Chaincode.Response;
import java.util.*;

import kr.ac.postech.sslab.standard.*;
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class EERC721 extends ERC721 implements IEERC721 {
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

			long ownedTokensCount = this.getBalance(stub, owner, type);

			return ResponseUtils.newSuccessResponse(Long.toString(ownedTokensCount));
		} catch (Throwable throwable) {
			return ResponseUtils.newErrorResponse(throwable.getMessage());
		}
	}

	private long getBalance(ChaincodeStub stub, String owner, String type) throws ParseException {
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
	public Response divide(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 2) {
				throw new Throwable("Incorrect number of arguments. Expecting 2");
			}

			String id = args.get(0).toLowerCase();
			String newId = args.get(1).toLowerCase();

			NFT nft = NFT.read(stub, id);

			NFT dup = new NFT();
			dup.mint(stub, newId, nft.getType(), nft.getOwner(), nft.getXAttr(), nft.getURI());
			dup.setOperator(stub, nft.getOperator());
			dup.setApprovee(stub, nft.getApprovee());

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

			NFT nft = NFT.read(stub, id);
			nft.setXAttr(stub, 3, null);

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
			map.put("approvee", nft.getApprovee());
			map.put("xattr", nft.getXAttr().toJSONString());
			map.put("uri", nft.getURI().toJSONString());

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

			String id = args.get(0).toLowerCase();

			List<String> history = new LinkedList<>();
			QueryResultsIterator<KeyModification> resultsIterator = stub.getHistoryForKey(id);
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
}