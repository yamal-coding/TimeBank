package paymentprotocol.model.files.network.persistent;

import rice.p2p.commonapi.Id;
import rice.p2p.past.ContentHashPastContent;

public class FAMEntry extends ContentHashPastContent {
	private String fileType;
	private int FAMEntryNum;
	private Id self_ledgerEntry_DHTHash;
	private boolean isCreditor;
	private Id self_previous_FAMEntry_DHTHash;
	private Id self_next_FAMEntry_DHTHash;
	private String comment;
	private int numericalDegreeOfSatisfactionWithService;
	private Id other_FBMEntry_DHTHash;
	private String timeStamp_otherPartySignature; //cambiar tipo
	private String other_digitalSignature; //cambiar tipo
	private String timestamp_creation; //cambiar tipo
	private String self_digitalSignature; //cambiar tipo
	
	public String getFileType() {
		return fileType;
	}

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

	public FAMEntry(Id myId) {
		super(myId);
		
	}
	
	
}
