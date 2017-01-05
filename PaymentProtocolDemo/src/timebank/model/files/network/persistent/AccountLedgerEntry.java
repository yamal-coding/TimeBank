package timebank.model.files.network.persistent;

import java.sql.Timestamp;

import rice.p2p.commonapi.Id;
import rice.p2p.past.ContentHashPastContent;

/**
 * This class represents an AccountLedger DHT file
 * @author yamal
 *
 */
public class AccountLedgerEntry extends ContentHashPastContent {
	//private String fileType;
	private int ledgerEntryNum;
	private Id billsEntry_DHTHash;
	private Id self_profile_DHTHash;
	private Id other_profile_DHTHash;
	private double actualServiceHours;
	//private double multiplier;
	private double pre_balance;
	private double balance;
	private Id self_previous_ledgerEntry_DHTHash;
	private Id self_next_ledgerEntry_DHTHash;
	private Id self_FBMEntry_DHTHash;
	private Id other_FBMEntry_DHTHash;
	private Id other_ledgerEntry_DHTHash;
	private Timestamp timeStamp_otherPartySignature;
	private String other_digitalSignature; //cambiar tipo
	private Timestamp timestamp_creation;
	private String self_digitalSignature; //cambiar tipo
	
	/**
	 * Constructor to create a first partial entry of an AccountLedgerEntry (ledgerEntryPE1)
	 * @param myId
	 * @param ledgerEntryNum
	 * @param billsEntry_DHTHash
	 * @param self_profile_DHTHash
	 * @param other_profile_DHTHash
	 * @param actualServiceHours
	 * @param multiplier
	 * @param pre_balance
	 * @param balance
	 * @param self_previous_ledgerEntry_DHTHash
	 * @param self_next_ledgerEntry_DHTHash
	 * @param other_FBMEntry_DHTHash
	 * @param self_FBMEntry_DHTHash
	 */
	public AccountLedgerEntry(Id myId/*, String fileType*/, int ledgerEntryNum, Id billsEntry_DHTHash,
			Id self_profile_DHTHash, Id other_profile_DHTHash, double actualServiceHours/*, double multiplier*/,
			double pre_balance, double balance, Id self_previous_ledgerEntry_DHTHash, 
			Id self_next_ledgerEntry_DHTHash, Id self_FBMEntry_DHTHash) {
		super(myId);
		//this.fileType = fileType;
		this.ledgerEntryNum = ledgerEntryNum;
		this.billsEntry_DHTHash = billsEntry_DHTHash;
		this.self_profile_DHTHash = self_profile_DHTHash;
		this.other_profile_DHTHash = other_profile_DHTHash;
		this.actualServiceHours = actualServiceHours;
		//this.multiplier = multiplier;
		this.pre_balance = pre_balance;
		this.balance = balance;
		this.self_previous_ledgerEntry_DHTHash = self_previous_ledgerEntry_DHTHash;
		this.self_next_ledgerEntry_DHTHash = self_next_ledgerEntry_DHTHash;
		this.self_FBMEntry_DHTHash = self_FBMEntry_DHTHash;
	}
	
	/**
	 * Constructor to create a second partial entry of an AccountLedgerEntry (ledgerEntryPE2)
	 * @param myId
	 * @param ledgerPE1
	 * @param other_FBMEntry_DHTHash
	 * @param other_ledgerEntry_DHTHash
	 * @param timeStamp_otherPartySignature
	 * @param other_digitalSignature
	 */
	public AccountLedgerEntry(Id myId, AccountLedgerEntry ledgerPE1, Id other_FBMEntry_DHTHash,
			Id other_ledgerEntry_DHTHash, Timestamp timeStamp_otherPartySignature, String other_digitalSignature) {
		super(myId);
		//this.fileType = ledgerPE1.getFileType();
		this.ledgerEntryNum = ledgerPE1.getLedgerEntryNum();
		this.billsEntry_DHTHash = ledgerPE1.getBillsEntry_DHTHash();
		this.self_profile_DHTHash = ledgerPE1.getSelf_profile_DHTHash();
		this.other_profile_DHTHash = ledgerPE1.getOther_profile_DHTHash();
		this.actualServiceHours = ledgerPE1.getActualServiceHours();
		//this.multiplier = ledgerPE1.getMultiplier();
		this.pre_balance = ledgerPE1.getPre_balance();
		this.balance = ledgerPE1.getBalance();
		this.self_previous_ledgerEntry_DHTHash = ledgerPE1.getSelf_previous_ledgerEntry_DHTHash();
		this.self_next_ledgerEntry_DHTHash = ledgerPE1.getSelf_next_ledgerEntry_DHTHash();
		this.self_FBMEntry_DHTHash = ledgerPE1.getSelf_FBMEntry_DHTHash();
		this.other_FBMEntry_DHTHash = other_FBMEntry_DHTHash;
		this.other_ledgerEntry_DHTHash = other_ledgerEntry_DHTHash;
		this.timeStamp_otherPartySignature = timeStamp_otherPartySignature;
		this.other_digitalSignature = other_digitalSignature;
	}
	
	/**
	 * Constructor to create a final and complete entry of an AccountLedgerEntry
	 * @param id
	 * @param ledgerPE2
	 * @param timestamp_creation
	 * @param self_digitalSignature
	 */
	public AccountLedgerEntry(Id id, AccountLedgerEntry ledgerPE2, Timestamp timestamp_creation, String self_digitalSignature){
		super(id);
		//this.fileType = ledgerPE1.getFileType();
		this.ledgerEntryNum = ledgerPE2.getLedgerEntryNum();
		this.billsEntry_DHTHash = ledgerPE2.getBillsEntry_DHTHash();
		this.self_profile_DHTHash = ledgerPE2.getSelf_profile_DHTHash();
		this.other_profile_DHTHash = ledgerPE2.getOther_profile_DHTHash();
		this.actualServiceHours = ledgerPE2.getActualServiceHours();
		//this.multiplier = ledgerPE2.getMultiplier();
		this.pre_balance = ledgerPE2.getPre_balance();
		this.balance = ledgerPE2.getBalance();
		this.self_previous_ledgerEntry_DHTHash = ledgerPE2.getSelf_previous_ledgerEntry_DHTHash();
		this.self_next_ledgerEntry_DHTHash = ledgerPE2.getSelf_next_ledgerEntry_DHTHash();
		this.self_FBMEntry_DHTHash = ledgerPE2.getSelf_FBMEntry_DHTHash();
		this.other_FBMEntry_DHTHash = ledgerPE2.getOther_FBMEntry_DHTHash();
		this.other_ledgerEntry_DHTHash = ledgerPE2.getOther_ledgerEntry_DHTHash();
		this.timeStamp_otherPartySignature = ledgerPE2.getTimeStamp_otherPartySignature();
		this.other_digitalSignature = ledgerPE2.getOther_digitalSignature();
		this.timestamp_creation = timestamp_creation;
		this.self_digitalSignature = self_digitalSignature;
	}
	
	/*public String getFileType() {
		return fileType;
	}*/

	public int getLedgerEntryNum() {
		return ledgerEntryNum;
	}

	public Id getBillsEntry_DHTHash() {
		return billsEntry_DHTHash;
	}

	public Id getSelf_profile_DHTHash() {
		return self_profile_DHTHash;
	}

	public Id getOther_profile_DHTHash() {
		return other_profile_DHTHash;
	}

	public double getActualServiceHours() {
		return actualServiceHours;
	}

	/*public double getMultiplier() {
		return multiplier;
	}*/

	public double getPre_balance() {
		return pre_balance;
	}

	public double getBalance() {
		return balance;
	}

	public Id getSelf_previous_ledgerEntry_DHTHash() {
		return self_previous_ledgerEntry_DHTHash;
	}

	public Id getSelf_next_ledgerEntry_DHTHash() {
		return self_next_ledgerEntry_DHTHash;
	}

	public Id getSelf_FBMEntry_DHTHash() {
		return self_FBMEntry_DHTHash;
	}

	public Id getOther_FBMEntry_DHTHash() {
		return other_FBMEntry_DHTHash;
	}
	
	public Id getOther_ledgerEntry_DHTHash(){
		return other_ledgerEntry_DHTHash;
	}
	
	public Timestamp getTimeStamp_otherPartySignature() {
		return timeStamp_otherPartySignature;
	}

	public String getOther_digitalSignature() {
		return other_digitalSignature;
	}

	public Timestamp getTimestamp_creation() {
		return timestamp_creation;
	}

	public String getSelf_digitalSignature() {
		return self_digitalSignature;
	}	

}
