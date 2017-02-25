package timebank.model.files.network.persistent;

import java.sql.Timestamp;

import rice.p2p.commonapi.Id;

/**
 * This class represents a FBMENtry DHT file
 * @author yamal
 *
 */
public class FBMEntry extends DHTEntry {
	private int FBMEntryNum;
	private Id self_ledgerEntry_DHTHash;
	private boolean isCreditor;
	private String comment;
	private int numericalDegreeOfSatisfactionWithService;
	private Id other_FAMEntry_DHTHash;
	private Id self_previous_FBMEntry_DHTHash;
	private Id self_next_FBMEntry_DHTHash;
	private Timestamp timestamp_creation;
	private String self_digitalSignature;
	
	/**
	 * Constructor to create a first partial entry of a FBMEntry (FBMPE1)
	 * @param myId
	 * @param fileType
	 * @param fBMEntryNum
	 * @param self_ledgerEntry_DHTHash
	 * @param isCreditor
	 * @param comment
	 * @param numericalDegreeOfSatisfactionWithService
	 * @param self_previous_FAMEntry_DHTHash
	 * @param self_next_FAMEntry_DHTHash
	 */
	public FBMEntry(Id myId, int FBMEntryNum, Id self_ledgerEntry_DHTHash, boolean isCreditor,
			String comment, int numericalDegreeOfSatisfactionWithService, Id self_previous_FAMEntry_DHTHash,
			Id self_next_FAMEntry_DHTHash) {
		super(myId);
		this.FBMEntryNum = FBMEntryNum;
		this.self_ledgerEntry_DHTHash = self_ledgerEntry_DHTHash;
		this.isCreditor = isCreditor;
		this.comment = comment;
		this.numericalDegreeOfSatisfactionWithService = numericalDegreeOfSatisfactionWithService;
		this.self_previous_FBMEntry_DHTHash = self_previous_FAMEntry_DHTHash;
		this.self_next_FBMEntry_DHTHash = self_next_FAMEntry_DHTHash;
	}
	
	/**
	 * Constructor to create a second partial entry of a FBMEntry (FBMPE2)
	 * @param id
	 * @param fbmPE1
	 * @param other_FAMEntry_DHTHash
	 */
	public FBMEntry(Id id, FBMEntry fbmPE1, Id other_FAMEntry_DHTHash){
		super(id);
		this.FBMEntryNum = fbmPE1.getFBMEntryNum();
		this.self_ledgerEntry_DHTHash = fbmPE1.getSelf_ledgerEntry_DHTHash();
		this.isCreditor = fbmPE1.isCreditor();
		this.comment = fbmPE1.getComment();
		this.numericalDegreeOfSatisfactionWithService = fbmPE1.getNumericalDegreeOfSatisfactionWithService();
		this.self_previous_FBMEntry_DHTHash = fbmPE1.getSelf_previous_FBMEntry_DHTHash();
		this.self_next_FBMEntry_DHTHash = fbmPE1.getSelf_next_FBMEntry_DHTHash();
		this.other_FAMEntry_DHTHash = other_FAMEntry_DHTHash;
	}
	
	/**
	 * Constructor to create a final and complete entry of a FBMEntry
	 * @param id
	 * @param fbmPE2
	 * @param timestamp_creation
	 * @param self_digitalSignature
	 */
	public FBMEntry(Id id, FBMEntry fbmPE2, Timestamp timestamp_creation, String self_digitalSignature){
		super(id);
		this.FBMEntryNum = fbmPE2.getFBMEntryNum();
		this.self_ledgerEntry_DHTHash = fbmPE2.getSelf_ledgerEntry_DHTHash();
		this.isCreditor = fbmPE2.isCreditor();
		this.comment = fbmPE2.getComment();
		this.numericalDegreeOfSatisfactionWithService = fbmPE2.getNumericalDegreeOfSatisfactionWithService();
		this.self_previous_FBMEntry_DHTHash = fbmPE2.getSelf_previous_FBMEntry_DHTHash();
		this.self_next_FBMEntry_DHTHash = fbmPE2.getSelf_next_FBMEntry_DHTHash();
		this.other_FAMEntry_DHTHash = fbmPE2.getOther_FAMEntry_DHTHash();
		this.timestamp_creation = timestamp_creation;
		this.self_digitalSignature = self_digitalSignature;
	}
	
	public int getFBMEntryNum() {
		return FBMEntryNum;
	}

	public Id getSelf_ledgerEntry_DHTHash() {
		return self_ledgerEntry_DHTHash;
	}

	public boolean isCreditor() {
		return isCreditor;
	}

	public String getComment() {
		return comment;
	}

	public int getNumericalDegreeOfSatisfactionWithService() {
		return numericalDegreeOfSatisfactionWithService;
	}

	public Id getOther_FAMEntry_DHTHash() {
		return other_FAMEntry_DHTHash;
	}

	public Id getSelf_previous_FBMEntry_DHTHash() {
		return self_previous_FBMEntry_DHTHash;
	}

	public Id getSelf_next_FBMEntry_DHTHash() {
		return self_next_FBMEntry_DHTHash;
	}

	public Timestamp getTimestamp_creation() {
		return timestamp_creation;
	}

	public String getSelf_digitalSignature() {
		return self_digitalSignature;
	}
}
