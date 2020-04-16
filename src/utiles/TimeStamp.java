package utiles;

import interfaces.TimeStampI;

public class TimeStamp implements TimeStampI {
	
	protected long time;	//le temps systeme Unix auquel le messge a été publié
	protected String timestamper; 	// identification du dateur (nom de l hote ou son adresse IP) 

	public TimeStamp(long time, String timestamper) {
		this.time = time;
		this.timestamper = timestamper;
	}

	@Override
	public boolean isInitialised() {
		return false;
	}

	@Override
	public long getTime() {
		return time;
	}

	@Override
	public void setTime(long time) {
		this.time= time;		
	}

	@Override
	public String getTimestamper() {
		return timestamper;
	}

	@Override
	public void setTimestamper(String timestamper) {
		this.timestamper = timestamper;		
	}

}
