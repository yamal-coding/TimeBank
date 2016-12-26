package paymentprotocol.model.files.persistent;

import rice.p2p.commonapi.Id;
import rice.p2p.past.ContentHashPastContent;

public class PublicProfile extends ContentHashPastContent {
	private String fileType;
	private String self_publicKey;
	private String self_firstName;
	private String self_surnames;
	private String self_telephone;
	private String self_email;
	private String self_address;
	private String self_previous_profileDHTHash;
	private String enabled;
	private String timestamp_creation;
	private String self_digitalSignature_creation;
	private String self_last_LedgerEntryDHTHash;
	private String self_last_FAMEntryDHTHash;
	private String self_last_FBMEntryDHTHash;
	private String timestamp_lastchange;
	private String self_digitalSignatureLastChange;
	
	public PublicProfile(Id myId, String fileType, String self_publicKey, String self_firstName, String self_surnames,
			String self_telephone, String self_email, String self_address, String self_previous_profileDHTHash,
			String enabled, String timestamp_creation, String self_digitalSignature_creation,
			String self_last_LedgerEntryDHTHash, String self_last_FAMEntryDHTHash, String self_last_FBMEntryDHTHash,
			String timestamp_lastchange, String self_digitalSignatureLastChange) {
		super(myId);
		this.fileType = fileType;
		this.self_publicKey = self_publicKey;
		this.self_firstName = self_firstName;
		this.self_surnames = self_surnames;
		this.self_telephone = self_telephone;
		this.self_email = self_email;
		this.self_address = self_address;
		this.self_previous_profileDHTHash = self_previous_profileDHTHash;
		this.enabled = enabled;
		this.timestamp_creation = timestamp_creation;
		this.self_digitalSignature_creation = self_digitalSignature_creation;
		this.self_last_LedgerEntryDHTHash = self_last_LedgerEntryDHTHash;
		this.self_last_FAMEntryDHTHash = self_last_FAMEntryDHTHash;
		this.self_last_FBMEntryDHTHash = self_last_FBMEntryDHTHash;
		this.timestamp_lastchange = timestamp_lastchange;
		this.self_digitalSignatureLastChange = self_digitalSignatureLastChange;
	}

	public String getFileType() {
		return fileType;
	}

	public String getSelf_publicKey() {
		return self_publicKey;
	}

	public String getSelf_firstName() {
		return self_firstName;
	}

	public String getSelf_surnames() {
		return self_surnames;
	}

	public String getSelf_telephone() {
		return self_telephone;
	}

	public String getSelf_email() {
		return self_email;
	}

	public String getSelf_address() {
		return self_address;
	}

	public String getSelf_previous_profileDHTHash() {
		return self_previous_profileDHTHash;
	}

	public String getEnabled() {
		return enabled;
	}

	public String getTimestamp_creation() {
		return timestamp_creation;
	}

	public String getSelf_digitalSignature_creation() {
		return self_digitalSignature_creation;
	}

	public String getSelf_last_LedgerEntryDHTHash() {
		return self_last_LedgerEntryDHTHash;
	}

	public String getSelf_last_FAMEntryDHTHash() {
		return self_last_FAMEntryDHTHash;
	}

	public String getSelf_last_FBMEntryDHTHash() {
		return self_last_FBMEntryDHTHash;
	}

	public String getTimestamp_lastchange() {
		return timestamp_lastchange;
	}

	public String getSelf_digitalSignatureLastChange() {
		return self_digitalSignatureLastChange;
	}
}
