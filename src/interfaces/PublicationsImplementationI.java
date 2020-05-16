package interfaces;

import java.rmi.Remote;

public interface PublicationsImplementationI extends Remote {

	/**
	 * publication d'un message associé à un topic
	 * 
	 * @param m     message
	 * @param topic sujet de publication
	 * @throws Exception
	 */
	public void publish(MessageI m, String topic) throws Exception;

	/**
	 * publication d'un message associé à plusieurs topics
	 * 
	 * @param m      message
	 * @param topics sujets de publication
	 * @throws Exception
	 */
	public void publish(MessageI m, String[] topics) throws Exception;

	/**
	 * publication de plusieurs messages associé à un meme topic
	 * 
	 * @param ms    messages
	 * @param topic sujet de publication
	 * @throws Exception
	 */
	public void publish(MessageI[] ms, String topic) throws Exception;

	/**
	 * publication de plusieurs messages associés à plusieurs topics
	 * 
	 * @param ms     messages
	 * @param topics sujets de publication
	 * @throws Exception
	 */
	public void publish(MessageI[] ms, String[] topics) throws Exception;

}
