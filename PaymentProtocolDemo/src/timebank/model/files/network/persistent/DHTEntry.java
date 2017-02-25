package timebank.model.files.network.persistent;

import rice.p2p.commonapi.Id;
import rice.p2p.past.ContentHashPastContent;

/**
 * 
 * @author yamal
 *
 */
public class DHTEntry extends ContentHashPastContent {

	public DHTEntry(Id myId) {
		super(myId);
	}

}
