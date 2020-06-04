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
	
	public Boolean getBooleanProp (String name) throws Exception;
	public Byte getByteProp (String name) throws Exception;
	public Character getCharProp (String name) throws Exception;
	public Double getDoubleProp (String name) throws Exception;
	public Float getFloatProp (String name) throws Exception;
	public Integer getIntProp (String name) throws Exception;
	public Long getLongProp (String name) throws Exception;
	public Short getShortProp (String name) throws Exception;
	public String getStringProp (String name) throws Exception;

}
