package kr.ac.postech.sslab.standard;

import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;

import java.util.List;

public class ERC721 extends ChaincodeBase implements IERC721 {
	private HFNFTMP protocol;

	public ERC721() {
		this.protocol = new HFNFTMP();
	}

	public ERC721(HFNFTMP protocol) {
		this.protocol = protocol;
	}

	@Override
	public Response init(ChaincodeStub stub) {
		return this.protocol.init(stub);
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
		return this.protocol.queryOwnedTokensCount(stub, args);
	}

	@Override
	public Response ownerOf(ChaincodeStub stub, List<String> args) {
		return this.protocol.queryOwner(stub, args);
	}

	@Override
	public Response transferFrom(ChaincodeStub stub, List<String> args) {
		return this.protocol.transfer(stub, args);
	}

	@Override
	public Response approve(ChaincodeStub stub, List<String> args) {
		return this.protocol.approve(stub, args);
	}
	
	@Override
	public Response setApprovalForAll(ChaincodeStub stub, List<String> args) {
		return this.protocol.setOperator(stub, args);
	}

	@Override
    public Response getApproved(ChaincodeStub stub, List<String> args) {
		return this.protocol.queryApproved(stub, args);
	}

	@Override
	public Response isApprovedForAll(ChaincodeStub stub, List<String> args) {
		return this.protocol.isOperatorForOwner(stub, args);
	}

	public static void main(String[] args) {
        new ERC721().start(args);
    }
}