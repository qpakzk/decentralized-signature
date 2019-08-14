package kr.ac.postech.sslab.adapter;

import org.hyperledger.fabric.shim.ChaincodeStub;
import org.json.simple.parser.ParseException;

public class NFT implements INFT {
    private NFTAdapter adapter;

    @Override
    public void mint(ChaincodeStub stub, String id, String owner, String uri) throws Throwable {
        adapter = new NFTAdapter(id.substring(0, 2));
        adapter.mint(stub, id, owner, uri);
    }

    @Override
    public void burn(ChaincodeStub stub, String id) {
        adapter.burn(stub, id);
    }

    @Override
    public String read(ChaincodeStub stub, String id) throws ParseException {
        return adapter.read(stub, id);
    }

    @Override
    public void setOwner(ChaincodeStub stub, String owner) {
        adapter.setOwner(stub, owner);
    }

    @Override
    public String getOwner() {
        return adapter.getOwner();
    }

    @Override
    public void setOperator(ChaincodeStub stub, String operator) {
        adapter.setOperator(stub, operator);
    }

    @Override
    public String getOperator() {
        return adapter.getOperator();
    }

    @Override
    public void setUri(ChaincodeStub stub, String uri) {
        adapter.setUri(stub, uri);
    }

    @Override
    public String getUri() {
        return adapter.getUri();
    }
 }