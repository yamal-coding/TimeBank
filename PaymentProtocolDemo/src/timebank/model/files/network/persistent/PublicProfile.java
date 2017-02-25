package timebank.model.files.network.persistent;

import java.sql.Timestamp;

import rice.p2p.commonapi.Id;
import rice.p2p.past.ContentHashPastContent;

/**
 * This class represents the PublicProfile DHT entry of an user
 * @author yamal
 *
 */
public class PublicProfile extends ContentHashPastContent {
	private String self_firstName;
	private String self_surnames;
	private int self_telephone;
	private String self_email;
	private String self_address;
	private Timestamp timestamp_creation;
	private String self_digitalSignature_creation;
	private Id self_first_LedgerEntryDHTHash;
	private Id self_first_FAMEntryDHTHash;
	private Id self_first_FBMEntryDHTHash;

	public PublicProfile(Id myId, String self_firstName, String self_surnames,
			int self_telephone, String self_email, String self_address, Timestamp timestamp_creation, 
			String self_digitalSignature_creation, Id self_last_LedgerEntryDHTHash, Id self_last_FAMEntryDHTHash, 
			Id self_last_FBMEntryDHTHash) {
		super(myId);
		this.self_firstName = self_firstName;
		this.self_surnames = self_surnames;
		this.self_telephone = self_telephone;
		this.self_email = self_email;
		this.self_address = self_address;
		this.timestamp_creation = timestamp_creation;
		this.self_digitalSignature_creation = self_digitalSignature_creation;
		this.self_first_LedgerEntryDHTHash = self_last_LedgerEntryDHTHash;
		this.self_first_FAMEntryDHTHash = self_last_FAMEntryDHTHash;
		this.self_first_FBMEntryDHTHash = self_last_FBMEntryDHTHash;
	}

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

	public Timestamp getTimestamp_creation() {
		return timestamp_creation;
	}

	public String getSelf_digitalSignature_creation() {
		return self_digitalSignature_creation;
	}

	public Id getSelf_first_LedgerEntryDHTHash() {
		return self_first_LedgerEntryDHTHash;
	}

	public Id getSelf_first_FAMEntryDHTHash() {
		return self_first_FAMEntryDHTHash;
	}

	public Id getSelf_first_FBMEntryDHTHash() {
		return self_first_FBMEntryDHTHash;
	}
}
