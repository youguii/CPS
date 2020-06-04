package utiles;

import java.util.HashMap;

import interfaces.PropertiesI;

public class Properties implements PropertiesI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/* Propreties contient 9 maps de 9 types primitifs différents
	 * Ce qui nous permet d'avoir plusieurs propriétés de meme type de valeurs mais de clés différentes
	 */
	
	protected HashMap<String, Boolean> boolProperties;
	protected HashMap<String, String> strProperties;
	protected HashMap<String, Integer> intProperties;
	protected HashMap<String, Float> floatProperties;
	protected HashMap<String, Double> doubleProperties;
	protected HashMap<String, Long> longProperties;
	protected HashMap<String, Short> shortProperties;
	protected HashMap<String, Character> charProperties;
	protected HashMap<String, Byte> byteProperties;

	public Properties() {
		this.boolProperties = new HashMap<>();
		this.strProperties = new HashMap<>();
		this.intProperties = new HashMap<>();
		this.floatProperties = new HashMap<>();
		this.doubleProperties = new HashMap<>();
		this.longProperties = new HashMap<>();
		this.shortProperties = new HashMap<>();
		this.charProperties = new HashMap<>();
		this.byteProperties = new HashMap<>();
	}

	@Override
	public void putProp(String name, boolean v) throws Exception {

		boolProperties.put(name, v);
	}

	@Override
	public void putProp(String name, byte v) throws Exception {

		byteProperties.put(name, v);
	}

	@Override
	public void putProp(String name, char v) throws Exception {

		charProperties.put(name, v);
	}

	@Override
	public void putProp(String name, double v) throws Exception {

		doubleProperties.put(name, v);
	}

	@Override
	public void putProp(String name, float v) throws Exception {

		floatProperties.put(name, v);
	}

	@Override
	public void putProp(String name, int v) throws Exception {

		intProperties.put(name, v);
	}

	@Override
	public void putProp(String name, long v) throws Exception {

		longProperties.put(name, v);
	}

	@Override
	public void putProp(String name, short v) throws Exception {

		shortProperties.put(name, v);
	}

	@Override
	public void putProp(String name, String v) throws Exception {

		strProperties.put(name, v);
	}

	@Override
	public Boolean getBooleanProp(String name) throws Exception {

		return boolProperties.get(name);
	}

	@Override
	public Byte getByteProp(String name) throws Exception {

		return byteProperties.get(name);
	}

	@Override
	public Character getCharProp(String name) throws Exception {
		return charProperties.get(name);
	}

	@Override
	public Double getDoubleProp(String name) throws Exception {
		return doubleProperties.get(name);
	}

	@Override
	public Float getFloatProp(String name) throws Exception {
		return floatProperties.get(name);
	}

	@Override
	public Integer getIntProp(String name) throws Exception {
		return intProperties.get(name);
	}

	@Override
	public Long getLongProp(String name) throws Exception {
		return longProperties.get(name);
	}

	@Override
	public Short getShortProp(String name) throws Exception {
		return shortProperties.get(name);
	}

	@Override
	public String getStringProp(String name) throws Exception {
		return strProperties.get(name);
	}

}
