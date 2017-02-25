package timebank.model.files.network.persistent;

import java.sql.Timestamp;

import rice.p2p.commonapi.Id;

/**
 * Class that represents the Bill of a payment
 * @author yamal
 *
 */
public class Bill extends DHTEntry {
	private String self_transRef;
	private String other_transRef;
	private Id self_profile_DHTHash;
	private Id other_profile_DHTHash;
	private double actualServiceHours;
	private String other_digitalSignature;
	private Timestamp timestamp_creation;
	private String self_digitalSignature_creation;
	
	public Bill(Id myId, String self_transRef, String other_transRef,
			Id self_profile_DHTHash, Id other_profile_DHTHash, double actualServiceHours, String other_digitalSignature,
			Timestamp timestamp_creation, String self_digitalSignature_creation) {
		super(myId);
		this.self_transRef = self_transRef;
		this.other_transRef = other_transRef;
		this.self_profile_DHTHash = self_profile_DHTHash;
		this.other_profile_DHTHash = other_profile_DHTHash;
		this.actualServiceHours = actualServiceHours;
		this.other_digitalSignature = other_digitalSignature;
		this.timestamp_creation = timestamp_creation;
	}
	
	public String getSelf_transRef() {
		return self_transRef;
	}
	public String getOther_transRef() {
		return other_transRef;
	}
	
	public Id getSelf_profile_DHTHash() {
		return self_profile_DHTHash;
	}
	public Id getOther_profile_DHTHash() {
		return other_profile_DHTHash;
	}
	
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
}
