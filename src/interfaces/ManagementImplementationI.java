package interfaces;

public interface ManagementImplementationI {
	/**
	 * création d'un nouveau sujet
	 * @param topic sujet qui sera crée
	 * @throws Exception
	 */
	public void createTopic(String topic) throws Exception;
	/**
	 * création de nouveaux sujets
	 * @param topics sujets qui seront crées
	 * @throws Exception
	 */
	public void createTopics(String [] topics)throws Exception;
	/**
	 * Distruction d'un sujet
	 * @param topic sujet qui sera ditruit
	 * @throws Exception
	 */
	public void destroyTopics(String topic) throws Exception;
	/**
	 * Test si un sujet topic est un sujet
	 * @param topic sujet
	 * @return true si topic est un sujet, false si non
	 * @throws Exception
	 */
	public boolean isTopic(String topic) throws Exception;
	/**
	 * 
	 * @return tableau contenant tout les sujets existants 
	 * @throws Exception
	 */
	public String [] getTopics() throws Exception;
	/**
	 * 
	 * @return uri du port entrant de publication
	 * @throws Exception
	 */
	public String getPublicationPortURI() throws Exception;

}
