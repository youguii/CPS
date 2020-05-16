package interfaces;

import java.rmi.Remote;

public interface ReceptionImplementationI extends Remote{

	/**
	 * reception d'un message depuis le broker
	 * 
	 * @param m message
	 * @throws Exception
	 */
	public void acceptMessage(MessageI m) throws Exception;

	/**
	 * reception de plusieurs messages depuis le broker
	 * 
	 * @param ms messages
	 * @throws Exception
	 */
	public void acceptMessage(MessageI[] ms) throws Exception;
}
