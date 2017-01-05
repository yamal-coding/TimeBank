package timebank.model.files.local;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import rice.p2p.commonapi.Id;

/**
 * User of the application private profile
 * @author yamal
 *
 */
public class PrivateProfile {
	//User UUID
	private UUID self_UUID;
	
	//DHT Hash to the self public profile
	private Id self_publicProfile_DHTHash;
	
	//DHT Hash list of the current transactions of the user
	private List<Id> transactionsDHTHashes;
	
	/**
	 * Class constructor
	 * @param self_UUID
	 */
	public PrivateProfile(UUID self_UUID){
		this.self_UUID = self_UUID;
		this.transactionsDHTHashes = new ArrayList<Id>();
	}
	
	/**
	 * 
	 * @return self_UUID
	 */
	public UUID getUUID(){
		return self_UUID;
	}
	
	/**
	 * This method sets the user public profile DHTHash value
	 * @param self_publicProfile_DHTHash
	 */
	public void setSelf_publicProfile_DHTHash(Id self_publicProfile_DHTHash){
		this.self_publicProfile_DHTHash = self_publicProfile_DHTHash;
	}
	
	/**
	 * 
	 * @return self_publicProfile_DHTHash
	 */
	public Id getSelf_publicProfile_DHTHash(){
		return self_publicProfile_DHTHash;
	}
	
	/**
	 * 
	 * @return transactionsDHTHashes
	 */
	public List<Id> getTransactionsDHTHashes(){
		return transactionsDHTHashes;
	}
	
	/**
	 * This method add a transaction to the transactions list
	 * @param bill_DHTHash
	 */
	public void addTransaction(Id bill_DHTHash){
		transactionsDHTHashes.add(bill_DHTHash);
	}
}
