package paymentprotocol.model.files.network.persistent;

import java.sql.Timestamp;

import rice.p2p.commonapi.Id;
import rice.p2p.past.ContentHashPastContent;

public class PublicProfile extends ContentHashPastContent {
	//private String fileType;
	//private String self_publicKey; //cambiar tipo
	private String self_firstName;
	private String self_surnames;
	private int self_telephone;
	private String self_email;
	private String self_address;
	//private Id self_previous_profileDHTHash;
	//private String enabled;
	private Timestamp timestamp_creation;
	private String self_digitalSignature_creation; //cambiar tipo
	private Id self_last_LedgerEntryDHTHash;
	private Id self_last_FAMEntryDHTHash;
	private Id self_last_FBMEntryDHTHash;
	//private String timestamp_lastchange; //cambiar tipo
	//private String self_digitalSignatureLastChange; //cambiar tipo

	public PublicProfile(Id myId, /*String fileType, String self_publicKey,*/ String self_firstName, String self_surnames,
			int self_telephone, String self_email, String self_address/*, Id self_previous_profileDHTHash,
			String enabled*/, Timestamp timestamp_creation, String self_digitalSignature_creation,
			Id self_last_LedgerEntryDHTHash, Id self_last_FAMEntryDHTHash, Id self_last_FBMEntryDHTHash/*,
			String timestamp_lastchange, String self_digitalSignatureLastChange*/) {
		super(myId);
		//this.fileType = fileType;
		//this.self_publicKey = self_publicKey;
		this.self_firstName = self_firstName;
		this.self_surnames = self_surnames;
		this.self_telephone = self_telephone;
		this.self_email = self_email;
		this.self_address = self_address;
		//this.self_previous_profileDHTHash = self_previous_profileDHTHash;
		//this.enabled = enabled;
		this.timestamp_creation = timestamp_creation;
		this.self_digitalSignature_creation = self_digitalSignature_creation;
		this.self_last_LedgerEntryDHTHash = self_last_LedgerEntryDHTHash;
		this.self_last_FAMEntryDHTHash = self_last_FAMEntryDHTHash;
		this.self_last_FBMEntryDHTHash = self_last_FBMEntryDHTHash;
		//this.timestamp_lastchange = timestamp_lastchange;
		//this.self_digitalSignatureLastChange = self_digitalSignatureLastChange;
	}

	/*public String getFileType() {
		return fileType;
	}

	public String getSelf_publicKey() {
		return self_publicKey;
	}*/

	public String getSelf_firstName() {
		return self_firstName;
	}

	public String getSelf_surnames() {
		return self_surnames;
	}

	public int getSelf_telephone() {
		return self_telephone;
	}

	public String getSelf_email() {
		return self_email;
	}

	public String getSelf_address() {
		return self_address;
	}

	/*public Id getSelf_previous_profileDHTHash() {
		return self_previous_profileDHTHash;
	}

	public String getEnabled() {
		return enabled;
	}*/

	public Timestamp getTimestamp_creation() {
		return timestamp_creation;
	}

	public String getSelf_digitalSignature_creation() {
		return self_digitalSignature_creation;
	}

	public Id getSelf_last_LedgerEntryDHTHash() {
		return self_last_LedgerEntryDHTHash;
	}

	public Id getSelf_last_FAMEntryDHTHash() {
		return self_last_FAMEntryDHTHash;
	}

	public Id getSelf_last_FBMEntryDHTHash() {
		return self_last_FBMEntryDHTHash;
	}

	/*public String getTimestamp_lastchange() {
		return timestamp_lastchange;
	}

	public String getSelf_digitalSignatureLastChange() {
		return self_digitalSignatureLastChange;
	}*/
	
	
}
