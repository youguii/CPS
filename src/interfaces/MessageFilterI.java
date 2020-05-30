package interfaces;

public interface MessageFilterI{
	/**
	 * prend un message et lui applique le filtre
	 * @param m message
	 * @return true si le message satisfait le filtre, false si non
	 * @throws Exception
	 */
	public boolean filter(MessageI m) throws Exception;
	

}
