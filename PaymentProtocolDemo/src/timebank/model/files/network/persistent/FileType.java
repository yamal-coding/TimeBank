package timebank.model.files.network.persistent;

/**
 * 
 * @author yamal
 *
 */
public enum FileType {
	ACCOUNT_LEDGER_ENTRY("AccountLedgerEntry"),
	FAM_ENTRY("FAMEntry"),
	FBM_ENTRY("FBMEntry"),
	PUBLIC_PROFILE_ENTRY("PublicProfileEntry"),
	BILL_ENTRY("BillEntry");
	
	private String string;
	
	private FileType(String string){
		this.string = string;
	}
	
	public String toString(){
		return this.string;
	}
}
