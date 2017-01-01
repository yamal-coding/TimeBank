package paymentprotocol.model.util;

import java.util.UUID;

import paymentprotocol.model.files.network.persistent.FileType;
import rice.p2p.commonapi.Id;
import rice.pastry.commonapi.PastryIdFactory;

public class Util {
	public static Id makeDHTHash(PastryIdFactory idFactory, UUID uuid, int numEntry, String transRef, FileType fileType){
		
		if (fileType == FileType.ACCOUNT_LEDGER_ENTRY)
			return idFactory.buildId(uuid.toString() + FileType.ACCOUNT_LEDGER_ENTRY + numEntry);
		
		if (fileType == FileType.FAM_ENTRY)
			return idFactory.buildId(uuid.toString() + FileType.FAM_ENTRY + numEntry);
		
		if (fileType == FileType.FBM_ENTRY)
			return idFactory.buildId(uuid.toString() + FileType.FBM_ENTRY + numEntry);
		
		if (fileType == FileType.BILL_ENTRY)
			return idFactory.buildId(uuid.toString() + FileType.BILL_ENTRY + transRef);
			
		//Default case is fileType == FileType.PUBLIC_PROFILE
		return idFactory.buildId(uuid.toString() + FileType.PUBLIC_PROFILE_ENTRY);
	}
}
