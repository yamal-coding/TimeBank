package paymentprotocol.model.files.ephemeral;

import rice.p2p.commonapi.Id;
import rice.p2p.past.ContentHashPastContent;

public class FBMEntry extends ContentHashPastContent {//debe implementar PastContent
	private String fileType;
	private int FBMEntryNum;
	private String self_ledgerEntry_DHTHash; //cambiar tipo
	private boolean isCreditor;
	private String comment;
	private int numericalDegreeOfSatisfactionWithService;
	private String other_FAMEntry_DHTHash; //cambiar tipo
	private String self_previous_FAMEntry_DHTHash; //cambiar tipo
	private String self_next_FAMEntry_DHTHash; //cambiar tipo
	private String timestamp_creation; //cambiar tipo
	private String self_digitalSignature; //cambiar tipo
	
	public FBMEntry(Id myId) {
		super(myId);
		
	}
	
	public String getFileType() {
		return fileType;
	}
	
	public int getFBMEntryNum() {
		return FBMEntryNum;
	}
	
	public String getSelf_ledgerEntry_DHTHash() {
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
	
	public String getOther_FAMEntry_DHTHash() {
		return other_FAMEntry_DHTHash;
	}
	
	public String getSelf_previous_FAMEntry_DHTHash() {
		return self_previous_FAMEntry_DHTHash;
	}
	
	public String getSelf_next_FAMEntry_DHTHash() {
		return self_next_FAMEntry_DHTHash;
	}
	
	public String getTimestamp_creation() {
		return timestamp_creation;
	}
	
	public String getSelf_digitalSignature() {
		return self_digitalSignature;
	}
}
