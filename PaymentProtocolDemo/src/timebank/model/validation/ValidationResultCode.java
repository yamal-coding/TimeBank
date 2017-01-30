package timebank.model.validation;

/**
 * Enumerate used to return the specific result code when the validation
 * methods are called during the payment phases
 * @author yamal
 *
 */
public enum ValidationResultCode {
	//TODO
	PHASE1_VALIDATION_OK, //The partial DHT entries created in the first stage of the payment has been validated successfully
	PHASE2_VALIDATION_OK, //The partial DHT entries created in the second stage of the payment has been validated successfully
	PHASE3_VALIDATION_OK  //The partial DHT entries created in the third stage of the payment has been validated successfully
}
