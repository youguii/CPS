package utiles;

import interfaces.MessageFilterI;
import interfaces.MessageI;

public class MessageFilter implements MessageFilterI {
	
	/**longueur minimal souhaité pour les messages à recevoir */
	protected Integer minLen; 
	/**longueur maximal souhaité pour les messages à recevoir */
	protected Integer maxLen;
	/**topic souhaité pour les messages à recevoir */
	protected String topic;
	/**laisser que les nouveau messages ou non*/
	protected Boolean newMessage;
	
	public MessageFilter(Integer minLen, Integer maxLen,String topic, Boolean newMessage ) {
		super();
		this.minLen = minLen;
		this.maxLen = maxLen;
		this.topic = topic;
		this.newMessage = newMessage;
		
		
	}

	@Override
	public synchronized boolean  filter(MessageI m) throws Exception {
		if(minLen != null) {
			//on fixe une longueur minimal pour les messages 
			Integer lenM = m.getProperties().getIntProp("lenM");
			if(lenM == null || lenM < minLen) {
				return false;
			}
		}

		if(maxLen != null) {
			//on fixe une longueur maximal pour les messages 
			Integer lenM = m.getProperties().getIntProp("lenM");
			if(lenM == null || lenM > maxLen) {
				return false;
			}
		}

		if(newMessage != null) {
			//on garde que les nouveau messages qu'on suppose qu'ils ont une propriété "new" avec comme valeur true/false
			Boolean newM = m.getProperties().getBooleanProp("new");
			if(newM == null || newM) {
				return false;
			}
		}
			
		if(topic != null) {
			//on filtre sur les sujets qui contienent le mot clé Literature
			String topicM = m.getProperties().getStringProp("topic");
			if(topicM == null || !(topicM.contains(topic))) {
				
				return false;
			}
		}
			
		return true;
	}
	

}
