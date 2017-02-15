package timebank.factory;

import java.sql.Timestamp;
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

/**
 * This class has static methods to create the multiple types of partial entries
 * @author yamal
 *
 */
public class PastEntryFactory {
	/**
	 * This method creates a first partial entry of an FBMEntry 
	 * @param entryNum
	 * @param lastFBM
	 * @param self_ledgerEntry_DHTHash
	 * @param comment
	 * @param degreeOfSatisfaction
	 * @param idFactory
	 * @param uuid
	 * @return fbmEntryPE1
	 */
	public static FBMEntry createFBMEntryPE1(int entryNum, Id lastFBM, Id self_ledgerEntry_DHTHash, String comment, 
			int degreeOfSatisfaction, PastryIdFactory idFactory, UUID uuid) {
		int FBMEntryNum = entryNum + 1;
		boolean isCreditorFBM = false;
		String fbmComment = comment;
		int numericalDegreeOfSatisfactionWithService = degreeOfSatisfaction;
		Id self_previous_FBMEntry_DHTHash = lastFBM;
		
		//This hash is directly calculated at this point because files in FreePastry are not mutable
		Id self_next_FBMEntry_DHTHash = Util.makeDHTHash(idFactory, uuid, FBMEntryNum + 1, null, FileType.FBM_ENTRY, EntryType.FINAL_ENTRY);
		
		Id fbmEntryPE1_DHTHash = Util.makeDHTHash(idFactory, uuid, FBMEntryNum, null, FileType.FBM_ENTRY, EntryType.PARTIAL_ENTRY_1);;
		
		return new FBMEntry(fbmEntryPE1_DHTHash, FBMEntryNum, self_ledgerEntry_DHTHash, 
				isCreditorFBM, fbmComment, numericalDegreeOfSatisfactionWithService, 
				self_previous_FBMEntry_DHTHash, self_next_FBMEntry_DHTHash);
	}

	/**
	 * This method returns a partial entry of a FAMEntry
	 * @param entryNum
	 * @param lastFAM
	 * @param self_ledgerEntry_DHTHash
	 * @param idFactory
	 * @param uuid
	 * @return famEntryPE1
	 */
	public static FAMEntry createFAMEntryPE1(int entryNum, Id lastFAM, Id self_ledgerEntry_DHTHash, PastryIdFactory idFactory, UUID uuid) {
		int FAMEntryNum = entryNum + 1;
		boolean isCreditorFAM = true;
		Id self_previous_FAMEntry_DHTHash = lastFAM;
		//This hash is directly calculated at this point because files in FreePastry are not mutable
		Id self_next_FAMEntry_DHTHash = Util.makeDHTHash(idFactory, uuid, FAMEntryNum + 1, null, FileType.FAM_ENTRY, EntryType.FINAL_ENTRY);
		
		Id famEntryPE1_DHTHash = Util.makeDHTHash(idFactory, uuid, FAMEntryNum, null, FileType.FAM_ENTRY, EntryType.PARTIAL_ENTRY_1);
		
		return new FAMEntry(famEntryPE1_DHTHash, 
				FAMEntryNum, self_ledgerEntry_DHTHash, isCreditorFAM, 
				self_previous_FAMEntry_DHTHash, self_next_FAMEntry_DHTHash);
	}

	/**
	 * This method returns a partial entry of an AccountLedgerEntry
	 * @param bill
	 * @param entryNum
	 * @param prebalance
	 * @param lastLedger
	 * @param lastFBMNumEntry
	 * @param isCreditor
	 * @param idFactory
	 * @param uuid
	 * @return accountLedgerEntryPE1
	 */
	public static AccountLedgerEntry createAccountLedgerEntryPE1(Bill bill, int entryNum, double prebalance, Id lastLedger, 
			int lastFBMNumEntry, boolean isCreditor, PastryIdFactory idFactory, UUID uuid) {
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
		Id self_next_ledgerEntry_DHTHash = Util.makeDHTHash(idFactory, uuid, ledgerEntryNum + 1, null, 
				FileType.ACCOUNT_LEDGER_ENTRY, EntryType.FINAL_ENTRY);
		//The final FBMENtry hash is directly calculated at this point
		Id self_FBMEntry_DHTHash = Util.makeDHTHash(idFactory, uuid, lastFBMNumEntry + 1, null, FileType.FBM_ENTRY, EntryType.FINAL_ENTRY);

		Id accountLedgerPE1_DHTHash = Util.makeDHTHash(idFactory, uuid, ledgerEntryNum, null, 
				FileType.ACCOUNT_LEDGER_ENTRY, EntryType.PARTIAL_ENTRY_1);
		return new AccountLedgerEntry(accountLedgerPE1_DHTHash, 
				ledgerEntryNum, billsEntry_DHTHash, self_profile_DHTHash, 
				other_profile_DHTHash, actualServiceHours, pre_balance, balance, 
				self_previous_ledgerEntry_DHTHash, self_next_ledgerEntry_DHTHash, self_FBMEntry_DHTHash);
	}
	
	/**
	 * This method creates a second partial entry of an FBMEntry
	 * @param fbmEntryPE1
	 * @param other_FAM_DHTHash
	 * @param idFactory
	 * @param uuid
	 * @return fbmEntryPE2
	 */
	public static FBMEntry createFBMEntryPE2(FBMEntry fbmEntryPE1, Id other_FAM_DHTHash, PastryIdFactory idFactory, UUID uuid){
		Id fbmEntryPE2_DHTHash = Util.makeDHTHash(idFactory, uuid, fbmEntryPE1.getFBMEntryNum(), null, 
				FileType.FBM_ENTRY, EntryType.PARTIAL_ENTRY_2);
		
		return new FBMEntry(fbmEntryPE2_DHTHash, fbmEntryPE1, other_FAM_DHTHash);
	}
	
	/**
	 * This method creates a second partial entry of an FAMEntry
	 * @param famEntryPE1
	 * @param comment
	 * @param degreeOfSatisfaction
	 * @param other_FBM_DHTHash
	 * @param other_digitalSignature
	 * @param idFactory
	 * @param uuid
	 * @return famEntryPE2
	 */
	public static FAMEntry createFAMEntryPE2(FAMEntry famEntryPE1, String comment, int degreeOfSatisfaction, Id other_FBM_DHTHash,
			String other_digitalSignature, PastryIdFactory idFactory, UUID uuid){
		Id famEntryPE2_DHTHash = Util.makeDHTHash(idFactory, uuid, famEntryPE1.getFAMEntryNum(), null, 
				FileType.FAM_ENTRY, EntryType.PARTIAL_ENTRY_2);
		
		return new FAMEntry(famEntryPE2_DHTHash, famEntryPE1, comment, degreeOfSatisfaction, 
				other_FBM_DHTHash, new Timestamp(System.currentTimeMillis()), other_digitalSignature);
	}
	
	/**
	 * This method creates a second partial entry of an AccountLedgerEntry
	 * @param ledgerEntryPE1
	 * @param otherFBMEntry_DHTHash
	 * @param otherLedger_DHTHash
	 * @param other_digitalSignature
	 * @param idFactory
	 * @param uuid
	 * @return accountLedgerEntryPE2
	 */
	public static AccountLedgerEntry createAccountLedgerEntryPE2(AccountLedgerEntry ledgerEntryPE1, Id otherFBMEntry_DHTHash, 
			Id otherLedger_DHTHash, String other_digitalSignature, PastryIdFactory idFactory, UUID uuid){
		
		Id accountLedgerPE2_DHTHash = Util.makeDHTHash(idFactory, uuid, ledgerEntryPE1.getLedgerEntryNum(), null, 
				FileType.ACCOUNT_LEDGER_ENTRY, EntryType.PARTIAL_ENTRY_2);
		
		return new AccountLedgerEntry(accountLedgerPE2_DHTHash, ledgerEntryPE1, otherFBMEntry_DHTHash, 
				otherLedger_DHTHash, new Timestamp(System.currentTimeMillis()), other_digitalSignature);
	}
	
	/**
	 * This method creates a final entry of an FBMEntry
	 * @param fbmEntryPE2
	 * @param hash
	 * @param self_digitalSignature
	 * @return fbmEntry
	 */
	public static FBMEntry createFinalFBMEntry(FBMEntry fbmEntryPE2, Id hash, String self_digitalSignature){
		//The hash is not calculated here because it was calculated previously
		return new FBMEntry(hash, fbmEntryPE2, new Timestamp(System.currentTimeMillis()), self_digitalSignature);
	}
	
	/**
	 * This method creates a final entry of an FAMEntry
	 * @param famEntryPE2
	 * @param hash
	 * @param self_digitalSignature
	 * @return famEntry
	 */
	public static FAMEntry createFinalFAMEntry(FAMEntry famEntryPE2, Id hash, String self_digitalSignature){
		//The hash is not calculated here because it was calculated previously
		return new FAMEntry(hash, famEntryPE2, new Timestamp(System.currentTimeMillis()), self_digitalSignature);
	}
	
	/**
	 * This method creates a final entry of an AccountLedgerEntry
	 * @param ledgerEntryPE2
	 * @param DHTHash
	 * @param self_digitalSignature
	 * @return accountLedgerEntry
	 */
	public static AccountLedgerEntry createFinalAccountLedgerEntry(AccountLedgerEntry ledgerEntryPE2, Id hash, String self_digitalSignature){
		//The hash is not calculated here because it was calculated previously
		return new AccountLedgerEntry(hash, ledgerEntryPE2, new Timestamp(System.currentTimeMillis()), self_digitalSignature);
	}
}
