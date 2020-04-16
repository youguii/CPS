package interfaces;

import java.io.Serializable;

import utiles.TimeStamp;

public interface MessageI extends Serializable {
	
	public String getURI() throws Exception;
	public TimeStamp getTimeStamp() throws Exception;
	public PropertiesI getProperties() throws Exception;
	public Serializable getPayload() throws Exception;

}
