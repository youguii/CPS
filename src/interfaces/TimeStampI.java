package interfaces;

public interface TimeStampI {
	public boolean isInitialised() throws Exception;
	public long getTime() throws Exception;
	public void setTime(long time) throws Exception;
	public String getTimestamper() throws Exception;
	public void setTimestamper(String timestamper) throws Exception;

}
