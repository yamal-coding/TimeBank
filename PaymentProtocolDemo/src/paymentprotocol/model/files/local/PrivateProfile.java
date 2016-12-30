package paymentprotocol.model.files.local;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import rice.p2p.commonapi.Id;

public class PrivateProfile {
	private UUID self_UUID;
	//DHT Hash to the self public profile
	private Id self_publicProfile_DHTHash;
	//DHT Hash list of the current transactions of the user
	private List<Id> transactionsDHTHashes;
	
	public PrivateProfile(UUID self_UUID){
		this.self_UUID = self_UUID;
		this.transactionsDHTHashes = new ArrayList<Id>();
	}

	public UUID getUUID(){
		return self_UUID;
	}
	
	public void setSelf_publicProfile_DHTHash(Id self_publicProfile_DHTHash){
		this.self_publicProfile_DHTHash = self_publicProfile_DHTHash;
	}

	public Id getSelf_publicProfile_DHTHash(){
		return self_publicProfile_DHTHash;
	}
	
	public void addTransaction(Id bill_DHTHash){
		this.transactionsDHTHashes.add(bill_DHTHash);
	}
}
