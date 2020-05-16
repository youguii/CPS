package interfaces;

import java.io.Serializable;

public interface TimeStampI extends Serializable{
	public boolean isInitialised() throws Exception;
	public long getTime() throws Exception;
	public void setTime(long time) throws Exception;
	public String getTimestamper() throws Exception;
	public void setTimestamper(String timestamper) throws Exception;

}
