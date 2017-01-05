package timebank.model.util;

import java.util.UUID;

import rice.p2p.commonapi.Id;
import rice.pastry.commonapi.PastryIdFactory;
import timebank.model.files.network.persistent.EntryType;
import timebank.model.files.network.persistent.FileType;

public class Util {
	public static Id makeDHTHash(PastryIdFactory idFactory, UUID uuid, int numEntry, String transRef, FileType fileType, EntryType entryType){
		Id ret;
		switch(fileType){
			case ACCOUNT_LEDGER_ENTRY:{
				if (entryType == EntryType.FINAL_ENTRY)
					ret = idFactory.buildId(uuid.toString() + FileType.ACCOUNT_LEDGER_ENTRY + numEntry);
				else if (entryType == EntryType.PARTIAL_ENTRY_1)
					ret = idFactory.buildId(uuid.toString() + FileType.ACCOUNT_LEDGER_ENTRY + numEntry + "PE1");
				else
					ret = idFactory.buildId(uuid.toString() + FileType.ACCOUNT_LEDGER_ENTRY + numEntry + "PE2");
			} break;
			case FAM_ENTRY:{
				if (entryType == EntryType.FINAL_ENTRY)
					ret = idFactory.buildId(uuid.toString() + FileType.FAM_ENTRY + numEntry);
				else if (entryType == EntryType.PARTIAL_ENTRY_1)
					ret = idFactory.buildId(uuid.toString() + FileType.FAM_ENTRY + numEntry + "PE1");
				else
					ret = idFactory.buildId(uuid.toString() + FileType.FAM_ENTRY + numEntry + "PE2");
			} break;
			case FBM_ENTRY:{
				if (entryType == EntryType.FINAL_ENTRY)
					ret = idFactory.buildId(uuid.toString() + FileType.FBM_ENTRY + numEntry);
				else if (entryType == EntryType.PARTIAL_ENTRY_1)
					ret = idFactory.buildId(uuid.toString() + FileType.FBM_ENTRY + numEntry + "PE1");
				else
					ret = idFactory.buildId(uuid.toString() + FileType.FBM_ENTRY + numEntry + "PE2");
			} break;
			case BILL_ENTRY:{
				ret = idFactory.buildId(uuid.toString() + FileType.BILL_ENTRY + transRef);
			}break;
			default:{
				ret = idFactory.buildId(uuid.toString() + FileType.PUBLIC_PROFILE_ENTRY);
			}
		}
		
		return ret;
	}
}
