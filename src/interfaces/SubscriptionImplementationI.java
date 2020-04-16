package interfaces;

public interface SubscriptionImplementationI {
	/**
	 * souscription à un topic
	 * 
	 * @param topic          sujet
	 * @param inboundPortURI uri du port entrant de reception
	 * @throws Exception
	 */
	public void subscribe(String topic, String inboundPortURI) throws Exception;

	/**
	 * souscription à plusieurs topics
	 * 
	 * @param topics         sujets
	 * @param inboundPortURI uri du port entrant de reception
	 * @throws Exception
	 */
	public void subscribe(String[] topics, String inboundPortURI) throws Exception;

	/**
	 * souscription à un topic avec un filtre
	 * 
	 * @param topic          sujet
	 * @param filter         filtre pour les messages
	 * @param inboundPortURI uri du port entrant de reception
	 * @throws Exception
	 */
	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception;

	/**
	 * modification du filtre
	 * 
	 * @param topic
	 * @param newFilter
	 * @param inboundPortURI
	 * @throws Exception
	 */
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception;

	/**
	 * desinscription d'un topic
	 * 
	 * @param topic          sujet
	 * @param inboundPortURI
	 * @throws Exception
	 */
	public void unsubscribe(String topic, String inboundPortURI) throws Exception;

}
