package paymentprotocol.model.files.network.persistent;

import java.sql.Timestamp;

import rice.p2p.commonapi.Id;
import rice.p2p.past.ContentHashPastContent;

public class FAMEntry extends ContentHashPastContent {
	//private String fileType;
	private int FAMEntryNum;
	private Id self_ledgerEntry_DHTHash;
	private boolean isCreditor;
	private Id self_previous_FAMEntry_DHTHash;
	private Id self_next_FAMEntry_DHTHash;
	private String comment;
	private int numericalDegreeOfSatisfactionWithService;
	private Id other_FBMEntry_DHTHash;
	private Timestamp timestamp_otherPartySignature; //cambiar tipo
	private String other_digitalSignature; //cambiar tipo
	private Timestamp timestamp_creation; //cambiar tipo
	private String self_digitalSignature; //cambiar tipo

	/**
	 * Constructor to create a first partial entry of a FAMEntry (FAMPE1)
	 * @param myId
	 * @param fileType
	 * @param fAMEntryNum
	 * @param self_ledgerEntry_DHTHash
	 * @param isCreditor
	 * @param self_previous_FAMEntry_DHTHash
	 * @param self_next_FAMEntry_DHTHash
	 */
	public FAMEntry(Id myId/*, String fileType*/, int fAMEntryNum, Id self_ledgerEntry_DHTHash, boolean isCreditor,
			Id self_previous_FAMEntry_DHTHash, Id self_next_FAMEntry_DHTHash) {
		super(myId);
		//this.fileType = fileType;
		FAMEntryNum = fAMEntryNum;
		this.self_ledgerEntry_DHTHash = self_ledgerEntry_DHTHash;
		this.isCreditor = isCreditor;
		this.self_previous_FAMEntry_DHTHash = self_previous_FAMEntry_DHTHash;
		this.self_next_FAMEntry_DHTHash = self_next_FAMEntry_DHTHash;
	}
	
	/**
	 * Constructor to create a second partial entry of a FAMEntry (FAMPE2)
	 * @param id
	 * @param famPE1
	 * @param comment
	 * @param numericalDegreeOfSatisfactionWithService
	 * @param other_FBMEntry_DHTHash
	 * @param timestamp_otherPartySignature
	 * @param other_digitalSignature
	 */
	public FAMEntry(Id id, FAMEntry famPE1, String comment, int numericalDegreeOfSatisfactionWithService,
			Id other_FBMEntry_DHTHash, Timestamp timestamp_otherPartySignature, String other_digitalSignature){
		super(id);
		//this.fileType = famPE1.getFileType();
		FAMEntryNum = famPE1.getFAMEntryNum();
		this.self_ledgerEntry_DHTHash = famPE1.getSelf_ledgerEntry_DHTHash();
		this.isCreditor = isCreditor();
		this.self_previous_FAMEntry_DHTHash = famPE1.getSelf_previous_FAMEntry_DHTHash();
		this.self_next_FAMEntry_DHTHash = famPE1.getSelf_next_FAMEntry_DHTHash();
		this.comment = comment;
		this.numericalDegreeOfSatisfactionWithService = numericalDegreeOfSatisfactionWithService;
		this.other_FBMEntry_DHTHash = other_FBMEntry_DHTHash;
		this.timestamp_otherPartySignature = timestamp_otherPartySignature;
		this.other_digitalSignature = other_digitalSignature;
	}
	
	/**
	 * Constructor to create a final and complete entry of a FAMEntry
	 * @param id
	 * @param famPE2
	 * @param timestamp_creation
	 * @param self_digitalSignature
	 */
	public FAMEntry(Id id, FAMEntry famPE2, Timestamp timestamp_creation, String self_digitalSignature){
		super(id);
		//this.fileType = famPE1.getFileType();
		FAMEntryNum = famPE2.getFAMEntryNum();
		this.self_ledgerEntry_DHTHash = famPE2.getSelf_ledgerEntry_DHTHash();
		this.isCreditor = isCreditor();
		this.self_previous_FAMEntry_DHTHash = famPE2.getSelf_previous_FAMEntry_DHTHash();
		this.self_next_FAMEntry_DHTHash = famPE2.getSelf_next_FAMEntry_DHTHash();
		this.comment = famPE2.getComment();
		this.numericalDegreeOfSatisfactionWithService = famPE2.getNumericalDegreeOfSatisfactionWithService();
		this.other_FBMEntry_DHTHash = famPE2.getOther_FBMEntry_DHTHash();
		this.timestamp_otherPartySignature = famPE2.getTimestamp_otherPartySignature();
		this.other_digitalSignature = famPE2.getOther_digitalSignature();
		this.timestamp_creation = timestamp_creation;
		this.self_digitalSignature = self_digitalSignature;
		
	}

	/*public String getFileType() {
		return fileType;
	}*/

	public int getFAMEntryNum() {
		return FAMEntryNum;
	}

	public Id getSelf_ledgerEntry_DHTHash() {
		return self_ledgerEntry_DHTHash;
	}

	public boolean isCreditor() {
		return isCreditor;
	}

	public Id getSelf_previous_FAMEntry_DHTHash() {
		return self_previous_FAMEntry_DHTHash;
	}

	public Id getSelf_next_FAMEntry_DHTHash() {
		return self_next_FAMEntry_DHTHash;
	}

	public String getComment() {
		return comment;
	}

	public int getNumericalDegreeOfSatisfactionWithService() {
		return numericalDegreeOfSatisfactionWithService;
	}

	public Id getOther_FBMEntry_DHTHash() {
		return other_FBMEntry_DHTHash;
	}

	public Timestamp getTimestamp_otherPartySignature() {
		return timestamp_otherPartySignature;
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
