package paymentprotocol.model.files.persistent;

import rice.p2p.commonapi.Id;
import rice.p2p.past.ContentHashPastContent;

public class AccountLedgerEntry extends ContentHashPastContent {
	private String fileType;
	private int billsEntryNum;
	private String billsEntry_DHTHash; //cambiar tipo
	private String self_profile_DHTHash; //cambiar tipo
	private String other_profile_DHTHash; //cambiar tipo
	private double actualServiceHours;
	private double multiplier;
	private double pre_balance;
	private double balance;
	private String self_previous_ledgerEntry_DHTHash; //cambiar tipo
	private String self_next_ledgerEntry_DHTHash; //cambiar tipo
	private String other_FBMEntry_DHTHash; //cambiar tipo
	private String self_FBMEntry_DHTHash; //cambiar tipo
	private String timeStamp_otherPartySignature; //cambiar tipo
	private String other_digitalSignature; //cambiar tipo
	private String timestamp_creation; //cambiar tipo
	private String self_digitalSignature; //cambiar tipo
	
	public AccountLedgerEntry(Id myId) {
		super(myId);
		
	}	
	
	public String getFileType() {
		return fileType;
	}
	
	public int getBillsEntryNum() {
		return billsEntryNum;
	}
	
	public String getBillsEntry_DHTHash() {
		return billsEntry_DHTHash;
	}
	
	public String getSelf_profile_DHTHash() {
		return self_profile_DHTHash;
	}
	
	public String getOther_profile_DHTHash() {
		return other_profile_DHTHash;
	}
	
	public double getActualServiceHours() {
		return actualServiceHours;
	}
	
	public double getMultiplier() {
		return multiplier;
	}
	
	public double getPre_balance() {
		return pre_balance;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public String getSelf_previous_ledgerEntry_DHTHash() {
		return self_previous_ledgerEntry_DHTHash;
	}
	
	public String getSelf_next_ledgerEntry_DHTHash() {
		return self_next_ledgerEntry_DHTHash;
	}
	
	public String getOther_FBMEntry_DHTHash() {
		return other_FBMEntry_DHTHash;
	}
	
	public String getSelf_FBMEntry_DHTHash() {
		return self_FBMEntry_DHTHash;
	}
	
	public String getTimeStamp_otherPartySignature() {
		return timeStamp_otherPartySignature;
	}
	
	public String getOther_digitalSignature() {
		return other_digitalSignature;
	}
	
	public String getTimestamp_creation() {
		return timestamp_creation;
	}
	
	public String getSelf_digitalSignature() {
		return self_digitalSignature;
	}
}
