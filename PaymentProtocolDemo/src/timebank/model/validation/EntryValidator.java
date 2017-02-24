package timebank.model.validation;

/**
 * This class is used to validate the partial entries of each payment phase
 * @author yamal
 *
 */
public class EntryValidator {
	/**
	 * This function is called by creditor when it is necessary to validate the
	 * three partial entries received from debtor
	 */
	public ValidationResultCode validatePaymentPhase1(){
		//TODO
		/*
		if (validateLedgerEntryPE1(debitorLedgerEntryPE1)){
			if (validateFAMEntryPE1(creditorFADebitorPE1)){
				if (validateFBMEntryPE1(debitorFBMEntryPE1))
					//Succesful validation
				else
					//Validation FBMEntryPE1 error
			}
			else
				//Validation FAMEntryPE1 error
		}
		else
			//Validation ledgerEntryPE1 error
		*/
		
		return null;
	}
	
	/**
	 * This function is called by debtor when it is necessary to validate the
	 * six partial entries received from creditor
	 */
	public ValidationResultCode validatePaymentPhase2(){
		//TODO
		
		/*
		if (validateLedgerEntryPE1(creditorLedgerEntryPE1)){
			if (validateFAMEntryPE1(debitorFACreditorPE1)){
				if (validateFBMEntryPE1(creditorFBMEntryPE1))
					if (validateLedgerEntryPE2(debitorLedgerEntryPE2))
						if (validateFAMEntryPE2(creditorFADebitorPE2)){
							if (validateFBMEntryPE2(debitorFBMEntryPE2)){
								//successful validation
							else
								//Validation FBMEntryPE2 error
						}
						else
							//Validation FAMEntryPE2 error
					}
					else
						//Validation ledgerEntryPE2 error
				}
				else
					//Validation FBMEntryPE1 error
			}else
				//Validation FAMEntryPE1 error
		}else
			//Validation ledgerEntryPE1 error
		*/
		
		return null;
	}
	
	/**
	 * This function is called by creditor when it is necessary to validate the
	 * three last partial entries received from debtor
	 */
	public ValidationResultCode validatePaymentPhase3(){
		//TODO
		
		/*
		if (validateLedgerEntryPE1(creditorLedgerEntryPE2)){
			if (validateFAMEntryPE1(debitorFACreditorPE2)){
				if (validateFBMEntryPE1(creditorFBMEntryPE2))
					//Succesful validation
				else
					//Validation FBMEntryPE2 error
			}
			else
				//Validation FAMEntryPE2 error
		}
		else
			//Validation ledgerEntryPE2
		*/
	
		return null;
	}
}
