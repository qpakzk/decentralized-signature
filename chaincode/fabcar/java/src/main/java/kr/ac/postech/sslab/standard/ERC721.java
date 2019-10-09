package kr.ac.postech.sslab.standard;

import kr.ac.postech.sslab.main.ConcreteChaincodeBase;
import kr.ac.postech.sslab.nft.NFT;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ERC721 extends ConcreteChaincodeBase implements IERC721 {
	private BaseNFT baseNFT = new BaseNFT();

	@Override
	public Response balanceOf(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("Incorrect number of arguments. Expecting 1.");
			}

			String owner = args.get(0).toLowerCase();
			long ownedTokensCount = this.getBalance(stub, owner);

			return newSuccessResponse(Long.toString(ownedTokensCount));
		} catch (Throwable throwable) {
			return newErrorResponse(throwable.getMessage());
		}
	}

	private long getBalance(ChaincodeStub stub, String owner) {
		String query = "{\"selector\":{\"owner\":\"" + owner + "\"}}";

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
		return this.baseNFT.getOwner(stub, args);
	}

	@Override
	public Response transferFrom(ChaincodeStub stub, List<String> args) {
		return this.baseNFT.setOwner(stub, args);
	}

	@Override
	public Response approve(ChaincodeStub stub, List<String> args) {
		return this.baseNFT.setApprovee(stub, args);
	}
	
	@Override
	public Response setApprovalForAll(ChaincodeStub stub, List<String> args) {
		return this.baseNFT.setOperator(stub, args);
	}

	@Override
    public Response getApproved(ChaincodeStub stub, List<String> args) {
		return this.baseNFT.getApprovee(stub, args);
	}

	@Override
	public Response isApprovedForAll(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 2) {
				throw new Throwable("Incorrect number of arguments. Expecting 2");
			}

			String owner = args.get(0).toLowerCase();
			String operator = args.get(1).toLowerCase();

			boolean result = this.isOperator(stub, owner, operator);

			return newSuccessResponse(Boolean.toString(result));
		} catch (Throwable throwable) {
			return newErrorResponse(throwable.getMessage());
		}
	}

	private boolean isOperator(ChaincodeStub stub, String owner, String operator) throws IOException {
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

	public Response mint(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 2) {
				throw new Throwable("Incorrect number of arguments. Expecting 2");
			}

			String id = args.get(0).toLowerCase();
			String type = "erc721";
			String owner = args.get(1).toLowerCase();

			List<String> newArgs = new ArrayList<>();
			newArgs.add(id);
			newArgs.add(type);
			newArgs.add(owner);

			return this.baseNFT.mint(stub, newArgs);
		} catch (Throwable throwable) {
			return newErrorResponse(throwable.getMessage());
		}
	}
}