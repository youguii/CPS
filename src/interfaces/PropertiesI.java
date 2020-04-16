package interfaces;

import java.io.Serializable;

public interface PropertiesI extends Serializable {
	public void putProp(String name, boolean v) throws Exception;
	public void putProp(String name, byte v) throws Exception;
	public void putProp(String name, char v) throws Exception;
	public void putProp(String name, double v) throws Exception;
	public void putProp(String name, float v) throws Exception;
	public void putProp(String name, int v) throws Exception;
	public void putProp(String name, long v) throws Exception;
	public void putProp(String name, short v) throws Exception;
	public void putProp(String name, String v) throws Exception;
	
	public boolean getBooleanProp (String name) throws Exception;
	public byte getByteProp (String name) throws Exception;
	public char getCharProp (String name) throws Exception;
	public double getDoubleProp (String name) throws Exception;
	public float getFloatProp (String name) throws Exception;
	public int getIntProp (String name) throws Exception;
	public long getLongProp (String name) throws Exception;
	public short getShortProp (String name) throws Exception;
	public String getStringProp (String name) throws Exception;

}
