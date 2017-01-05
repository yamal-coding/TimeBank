package timebank.model.files.network.persistent;

import java.sql.Timestamp;

import rice.p2p.commonapi.Id;
import rice.p2p.past.ContentHashPastContent;

public class Bill extends ContentHashPastContent {
	//private String fileType;
	private String self_transRef;
	private String other_transRef;
	//private Id transRefHash;
	private Id self_profile_DHTHash;
	private Id other_profile_DHTHash;
	//private String mutuallyAgreedMultiplier; //cambiar tipo
	//private Id self_invoicesEntryDHTHash;
	private double actualServiceHours;
	private String other_digitalSignature; //cambiar tipo
	private Timestamp timestamp_creation;
	private String self_digitalSignature_creation; //cambiar tipo
	//private Id other_ledgerEntryDHTHash;
	//private Id self_ledgerEntryDHTHash;
	//private String timestamp_lastChange;
	//private String self_digitalSignature_lastChange;
	
	public Bill(Id myId,/* String fileType,*/ String self_transRef, String other_transRef/*, Id transRefHash*/,
			Id self_profile_DHTHash, Id other_profile_DHTHash/*, String mutuallyAgreedMultiplier*/,
			/*Id self_invoicesEntryDHTHash,*/ double actualServiceHours, String other_digitalSignature,
			Timestamp timestamp_creation, String self_digitalSignature_creation/*, Id other_ledgerEntryDHTHash,
			Id self_ledgerEntryDHTHash, String timestamp_lastChange, String self_digitalSignature_lastChange*/) {
		super(myId);
		//this.fileType = fileType;
		this.self_transRef = self_transRef;
		this.other_transRef = other_transRef;
		//this.transRefHash = transRefHash;
		this.self_profile_DHTHash = self_profile_DHTHash;
		this.other_profile_DHTHash = other_profile_DHTHash;
		//this.mutuallyAgreedMultiplier = mutuallyAgreedMultiplier;
		//this.self_invoicesEntryDHTHash = self_invoicesEntryDHTHash;
		this.actualServiceHours = actualServiceHours;
		this.other_digitalSignature = other_digitalSignature;
		this.timestamp_creation = timestamp_creation;
		this.self_digitalSignature_creation = self_digitalSignature_creation;
		//this.other_ledgerEntryDHTHash = other_ledgerEntryDHTHash;
		//this.self_ledgerEntryDHTHash = self_ledgerEntryDHTHash;
		//this.timestamp_lastChange = timestamp_lastChange;
		//this.self_digitalSignature_lastChange = self_digitalSignature_lastChange;
	}
	
	/*public String getFileType() {
		return fileType;
	}*/
	public String getSelf_transRef() {
		return self_transRef;
	}
	public String getOther_transRef() {
		return other_transRef;
	}
	/*public Id getTransRefHash() {
		return transRefHash;
	}*/
	public Id getSelf_profile_DHTHash() {
		return self_profile_DHTHash;
	}
	public Id getOther_profile_DHTHash() {
		return other_profile_DHTHash;
	}
	/*public String getMutuallyAgreedMultiplier() {
		return mutuallyAgreedMultiplier;
	}*/
	/*public Id getSelf_invoicesEntryDHTHash() {
		return self_invoicesEntryDHTHash;
	}*/
	public double getActualServiceHours() {
		return actualServiceHours;
	}
	public String getOther_digitalSignature() {
		return other_digitalSignature;
	}
	public Timestamp getTimestamp_creation() {
		return timestamp_creation;
	}
	public String getSelf_digitalSignature_creation() {
		return self_digitalSignature_creation;
	}
	/*public Id getOther_ledgerEntryDHTHash() {
		return other_ledgerEntryDHTHash;
	}
	public Id getSelf_ledgerEntryDHTHash() {
		return self_ledgerEntryDHTHash;
	}
	public String getTimestamp_lastChange() {
		return timestamp_lastChange;
	}
	public String getSelf_digitalSignature_lastChange() {
		return self_digitalSignature_lastChange;
	}*/
}
