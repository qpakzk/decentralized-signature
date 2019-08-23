package kr.ac.postech.sslab.extension;

import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;
import java.util.List;

import kr.ac.postech.sslab.standard.*;

public class EERC721 extends ERC721 implements IEERC721 {
	private EHFNFTMP protocol;

	public EERC721() {
		super(new EHFNFTMP());
		this.protocol = new EHFNFTMP();
	}
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
					return super.balanceOf(stub, args);

				case "ownerOf":
					return super.ownerOf(stub, args);

				case "transferFrom":
					return super.transferFrom(stub, args);

				case "approve":
					return super.approve(stub, args);

				case "setApprovalForAll":
					return super.setApprovalForAll(stub, args);

				case "getApproved":
					return super.getApproved(stub, args);

				case "isApprovedForAll":
					return super.isApprovedForAll(stub, args);

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
		return this.protocol.mint(stub, args);
	}

	@Override
	public Response divide(ChaincodeStub stub, List<String> args) {
		return this.protocol.duplicate(stub, args);
	}

	@Override
    public Response delete(ChaincodeStub stub, List<String> args) {
		return this.protocol.deactivate(stub, args);
	}

	@Override
    public Response updateXAtt(ChaincodeStub stub, List<String> args) {
		return  this.protocol.updateXAtt(stub, args);
	}

	@Override
	public Response updateUri(ChaincodeStub stub, List<String> args) {
		return this.protocol.updateUri(stub, args);
	}

	@Override
    public Response queryToken(ChaincodeStub stub, List<String> args) {
		return this.protocol.queryToken(stub, args);
	}

	@Override
    public Response queryHistory(ChaincodeStub stub, List<String> args) {
		return this.protocol.queryHistory(stub, args);
	}


	public Response burn(ChaincodeStub stub, List<String> args) {
		return this.protocol.burn(stub, args);
	}

	public static void main(String[] args) {
        new EERC721().start(args);
    }
}