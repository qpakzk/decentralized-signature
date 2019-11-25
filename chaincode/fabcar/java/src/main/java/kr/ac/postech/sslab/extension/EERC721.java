package kr.ac.postech.sslab.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.postech.sslab.nft.NFT;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.io.IOException;
import java.util.*;
import kr.ac.postech.sslab.standard.*;
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

public class EERC721 extends ERC721 implements IEERC721 {
	@Override
	public Response balanceOf(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() == 1) {
				return super.balanceOf(stub, args);
			}

			if (args.size() != 2) {
				throw new Throwable("FAILURE");
			}

			String owner = args.get(0).toLowerCase();
			String type = args.get(1).toLowerCase();

			long ownedTokensCount = this.getBalance(stub, owner, type);

			return newSuccessResponse(Long.toString(ownedTokensCount));
		} catch (Throwable throwable) {
			return newErrorResponse("FAILURE");
		}
	}

	private long getBalance(ChaincodeStub stub, String owner, String type) throws IOException {
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
			if (args.size() != 4) {
				throw new Throwable("FAILURE");
			}

			String id = args.get(0).toLowerCase();
			String[] newIds = args.get(1)
					.replace("[", "").replace("]", "").split(", ");
			String[] values = args.get(2)
					.replace("[", "").replace("]", "").split(", ");
			int index = Integer.parseInt(args.get(3));

			if (newIds.length != 2 || values.length != 2) {
				throw new Throwable();
			}

			NFT nft = NFT.read(stub, id);

			NFT[] child = new NFT[2];

			for (int i = 0; i < 2; i++) {
				child[i] = new NFT();
				child[i].mint(stub, newIds[i], nft.getType(), nft.getOwner(), nft.getXAttr(), nft.getURI());
				child[i].setXAttr(stub, index, values[i]); // division point
				child[i].setXAttr(stub, 1, nft.getId()); // parent
			}

			nft.setXAttr(stub, 0, null); //deactivate
			nft.setXAttr(stub, 2, newIds[0] + "," + newIds[1]); // children

			return newSuccessResponse("SUCCESS");
		} catch (Throwable throwable) {
			return newErrorResponse("FAILURE");
		}
	}

	@Override
    public Response deactivate(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("FAILURE");
			}

			String id = args.get(0).toLowerCase();

			NFT nft = NFT.read(stub, id);
			nft.setXAttr(stub, 3, null);

			return newSuccessResponse("SUCCESS");
		} catch (Throwable throwable) {
			return newErrorResponse("FAILURE");
		}
	}

	@Override
    public Response query(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("FAILURE");
			}

			String id = args.get(0).toLowerCase();

			NFT nft = NFT.read(stub, id);

			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> map = new HashMap<>();
			map.put("id", nft.getId());
			map.put("type", nft.getType());
			map.put("owner", nft.getOwner());
			map.put("approvee", nft.getApprovee());
			map.put("xattr", nft.getXAttr().toJSONString());
			map.put("uri", nft.getURI().toJSONString());

			String query = mapper.writeValueAsString(map);
			return newSuccessResponse(query);
		} catch (Throwable throwable) {
			return newErrorResponse("FAILURE");
		}
	}

	@Override
    public Response queryHistory(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("FAILURE");
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
			return newSuccessResponse(result);
		} catch (Throwable throwable) {
			return newErrorResponse("FAILURE");
		}
	}
}