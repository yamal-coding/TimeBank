package timebank.factory;

import java.util.UUID;

import rice.p2p.commonapi.Id;
import rice.pastry.commonapi.PastryIdFactory;
import timebank.model.files.network.persistent.AccountLedgerEntry;
import timebank.model.files.network.persistent.Bill;
import timebank.model.files.network.persistent.EntryType;
import timebank.model.files.network.persistent.FAMEntry;
import timebank.model.files.network.persistent.FBMEntry;
import timebank.model.files.network.persistent.FileType;
import timebank.model.util.Util;

public class PastEntryFactory {
	public static FBMEntry createFBMEntryPE1(int entryNum, Id lastFBM, Id self_ledgerEntry_DHTHash, PastryIdFactory idFactory, UUID uuid) {
		int FBMEntryNum = entryNum + 1;
		boolean isCreditorFBM = false;
		String comment = "comentario";//pending
		int numericalDegreeOfSatisfactionWithService = 5; //pending
		Id self_previous_FBMEntry_DHTHash = lastFBM;
		//This hash is directly calculated at this point because files in FreePastry are not mutable
		Id self_next_FBMEntry_DHTHash = Util.makeDHTHash(idFactory, uuid, FBMEntryNum + 1, null, FileType.FBM_ENTRY, EntryType.FINAL_ENTRY);
		
		Id debitorFBMEntryPE1_DHTHash = Util.makeDHTHash(idFactory, uuid, FBMEntryNum, null, FileType.FBM_ENTRY, EntryType.PARTIAL_ENTRY_1);;
		
		return new FBMEntry(debitorFBMEntryPE1_DHTHash, FBMEntryNum, self_ledgerEntry_DHTHash, 
				isCreditorFBM, comment, numericalDegreeOfSatisfactionWithService, 
				self_previous_FBMEntry_DHTHash, self_next_FBMEntry_DHTHash);
	}

	public static FAMEntry createFAMEntryPE1(int entryNum, Id lastFAM, Id self_ledgerEntry_DHTHash, PastryIdFactory idFactory, UUID uuid) {
		int FAMEntryNum = entryNum + 1;
		boolean isCreditorFAM = true;
		Id self_previous_FAMEntry_DHTHash = lastFAM;
		//This hash is directly calculated at this point because files in FreePastry are not mutable
		Id self_next_FAMEntry_DHTHash = Util.makeDHTHash(idFactory, uuid, FAMEntryNum + 1, null, FileType.FAM_ENTRY, EntryType.FINAL_ENTRY);
		
		Id creditorFADebitorPE1_DHTHash = Util.makeDHTHash(idFactory, uuid, FAMEntryNum, null, FileType.FAM_ENTRY, EntryType.PARTIAL_ENTRY_1);
		
		return new FAMEntry(creditorFADebitorPE1_DHTHash, 
				FAMEntryNum, self_ledgerEntry_DHTHash, isCreditorFAM, 
				self_previous_FAMEntry_DHTHash, self_next_FAMEntry_DHTHash);
	}

	public static AccountLedgerEntry createAccountLedgerEntryPE1(Bill bill, int entryNum, double prebalance, Id lastLedger, int lastFBMNumEntry, boolean isCreditor, PastryIdFactory idFactory, UUID uuid) {
		int ledgerEntryNum = entryNum + 1;
		Id billsEntry_DHTHash = bill.getId();
		
		Id self_profile_DHTHash;
		Id other_profile_DHTHash;
		
		double actualServiceHours = bill.getActualServiceHours();
		double pre_balance = prebalance;
		double balance;
		
		if (isCreditor){//the bill owner is creditor
			self_profile_DHTHash = bill.getOther_profile_DHTHash();
			other_profile_DHTHash = bill.getSelf_profile_DHTHash();
			balance = pre_balance + actualServiceHours;
		}
		else{
			other_profile_DHTHash = bill.getOther_profile_DHTHash();
			self_profile_DHTHash = bill.getSelf_profile_DHTHash();

			balance = pre_balance - actualServiceHours;
		}
		
		
		Id self_previous_ledgerEntry_DHTHash = lastLedger;
		//This hash is directly calculated at this point because files in FreePastry are not mutable
		Id self_next_ledgerEntry_DHTHash = Util.makeDHTHash(idFactory, uuid, ledgerEntryNum + 1, null, FileType.ACCOUNT_LEDGER_ENTRY, EntryType.FINAL_ENTRY);
		//The final FBMENtry hash is directly calculated at this point
		Id self_FBMEntry_DHTHash = Util.makeDHTHash(idFactory, uuid, lastFBMNumEntry + 1, null, FileType.FBM_ENTRY, EntryType.FINAL_ENTRY);

		Id debitorLedgerPE1_DHTHash = Util.makeDHTHash(idFactory, uuid, ledgerEntryNum, null, FileType.ACCOUNT_LEDGER_ENTRY, EntryType.PARTIAL_ENTRY_1);
		return new AccountLedgerEntry(debitorLedgerPE1_DHTHash, 
				ledgerEntryNum, billsEntry_DHTHash, self_profile_DHTHash, 
				other_profile_DHTHash, actualServiceHours, pre_balance, balance, 
				self_previous_ledgerEntry_DHTHash, self_next_ledgerEntry_DHTHash, self_FBMEntry_DHTHash);
	}
	
	public static FBMEntry createFBMEntryPE2(){
		return null;
	}
	
	public static FAMEntry createFAMEntryPE2(){
		return null;
	}
	
	public static AccountLedgerEntry createAccountLedgerEntryPE2(){
		return null;
	}
	
	public static FAMEntry createFinalFBMEntry(){
		return null;
	}
	
	public static FBMEntry createFinalFAMEntry(){
		return null;
	}
	
	public static AccountLedgerEntry createFinalAccountLedgerEntry(){
		return null;
	}
}
