package kr.ac.postech.sslab.standard;

import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.hyperledger.fabric.shim.ledger.KeyValue;

import kr.ac.postech.sslab.msg_type.*;
import kr.ac.postech.sslab.type.NFT;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class HFNFTMP {
    private static long tokenCounts = 0;

    private static void increaseTokenCounts() {
        HFNFTMP.tokenCounts += 1;
    }

    public String mint(ChaincodeStub stub, MsgMintNFT msg) {
        NFT nft = new NFT(stub, String.valueOf(HFNFTMP.tokenCounts), msg.getHash(), msg.getUri());
        HFNFTMP.increaseTokenCounts();

        return nft.stringNFTJSON();
    }

    public boolean transfer(ChaincodeStub stub, MsgTransferNFT msg) {
        NFT nft = NFT.retrieve(stub, msg.getId());
        if(nft.getOwner().equals(msg.getSender()) || nft.getOwner().equals("")) {
            nft.setOwner(stub, msg.getRecipient());
            return true;
        }

        return false;
    }

    public void edit(ChaincodeStub stub, MsgEditNFTMetadata msg) {
        NFT nft = NFT.retrieve(stub, msg.getId());
        if(msg.getParams() == 2) {
            nft.setOperator(stub, msg.getOperator());
        }
        else if(msg.getParams() == 3) {
            nft.editMetadata(stub, msg.getHash(), msg.getUri());
        }
    }

    public void deactivate(ChaincodeStub stub, String id) {
        NFT nft = NFT.retrieve(stub, id);
        if(nft.getIsActivated())
            nft.setIsActivated(stub, false);
    }

    public void activate(ChaincodeStub stub, String id) {
        NFT nft = NFT.retrieve(stub, id);
        if(!nft.getIsActivated())
            nft.setIsActivated(stub, true);
    }

    public boolean burn(ChaincodeStub stub, MsgBurnNFT msg) {
        NFT nft = NFT.retrieve(stub, msg.getId());
        if(nft.getOwner().equals(msg.getSender())) {
            nft.burn(stub, msg.getId());
            return true;
        }

        return false;
    }

    public String query(ChaincodeStub stub, String id) {
        NFT nft = NFT.retrieve(stub, id);
        return nft.stringNFTJSON();
    }

    public long queryNumberOfNFTs(ChaincodeStub stub, String owner) {
        String queryString = "{\"owner\":\"" + owner + "\"}";
        long numberOfNFTs = 0;

        QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(queryString);
        while(resultsIterator.iterator().hasNext()) {
            resultsIterator.iterator().next().getStringValue();
            numberOfNFTs++;
        }

        return numberOfNFTs;
    }

    public List<String> queryIDsByOwner(ChaincodeStub stub, String owner) {
        List<String> ids = new LinkedList<>();
        String queryString = "{\"owner\":\"" + owner + "\"}";

        QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(queryString);
        while(resultsIterator.iterator().hasNext()) {
            ids.add(resultsIterator.iterator().next().getStringValue());
        }

        if(ids.size() == 0)
            return null;
        return ids;
    }

    public String queryOwner(ChaincodeStub stub, String id) {
        NFT nft = NFT.retrieve(stub, id);
        return nft.getOwner();
	}

	public  String queryOperator(ChaincodeStub stub, String id) {
        NFT nft = NFT.retrieve(stub, id);
        return nft.getOperator();
    }

    public String queryHistory(ChaincodeStub stub, String id) {
        List<String> history = new LinkedList<>();
        QueryResultsIterator<KeyModification> resultsIterator = stub.getHistoryForKey(id);
        while (resultsIterator.iterator().hasNext()) {
            history.add(resultsIterator.iterator().next().getStringValue());
        }

        Iterator<String> it = history.iterator();
        String results = "";
        while (it.hasNext()) {
            results += it.next() + "\n";
        }

        return results;
    }
}