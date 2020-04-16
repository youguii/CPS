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
//		propStrBool.setName(name);
//		propStrBool.setValeur(v);

		boolProperties.put(name, v);
	}

	@Override
	public void putProp(String name, byte v) throws Exception {
//		propStrByte.setName(name);
//		propStrByte.setValeur(v);

		byteProperties.put(name, v);
	}

	@Override
	public void putProp(String name, char v) throws Exception {
//		propStrChar.setName(name);
//		propStrChar.setValeur(v);

		charProperties.put(name, v);
	}

	@Override
	public void putProp(String name, double v) throws Exception {
//		propStrDouble.setName(name);
//		propStrDouble.setValeur(v);

		doubleProperties.put(name, v);
	}

	@Override
	public void putProp(String name, float v) throws Exception {
//		propStrFloat.setName(name);
//		propStrFloat.setValeur(v);

		floatProperties.put(name, v);
	}

	@Override
	public void putProp(String name, int v) throws Exception {
//		propStrInt.setName(name);
//		propStrInt.setValeur(v);

		intProperties.put(name, v);
	}

	@Override
	public void putProp(String name, long v) throws Exception {
//		propStrLong.setName(name);
//		propStrLong.setValeur(v);

		longProperties.put(name, v);
	}

	@Override
	public void putProp(String name, short v) throws Exception {
//		propStrShort.setName(name);
//		propStrShort.setValeur(v);

		shortProperties.put(name, v);
	}

	@Override
	public void putProp(String name, String v) throws Exception {
//		propStrString.setName(name);
//		propStrString.setValeur(v);

		strProperties.put(name, v);
	}

	@Override
	public boolean getBooleanProp(String name) throws Exception {
//		return (boolean) propStrBool.getValeur();
		return boolProperties.get(name);
	}

	@Override
	public byte getByteProp(String name) throws Exception {
//		return (byte) propStrByte.getValeur();
		return byteProperties.get(name);
	}

	@Override
	public char getCharProp(String name) throws Exception {
//		return (char) propStrChar.getValeur();
		return charProperties.get(name);
	}

	@Override
	public double getDoubleProp(String name) throws Exception {
//		return (double) propStrDouble.getValeur();
		return doubleProperties.get(name);
	}

	@Override
	public float getFloatProp(String name) throws Exception {
//		return (float) propStrFloat.getValeur();
		return floatProperties.get(name);
	}

	@Override
	public int getIntProp(String name) throws Exception {
//		return (int) propStrInt.getValeur();
		return intProperties.get(name);
	}

	@Override
	public long getLongProp(String name) throws Exception {
//		return (long) propStrLong.getValeur();
		return longProperties.get(name);
	}

	@Override
	public short getShortProp(String name) throws Exception {
//		return (short) propStrShort.getValeur();
		return shortProperties.get(name);
	}

	@Override
	public String getStringProp(String name) throws Exception {
//		return (String) propStrString.getValeur();
		return strProperties.get(name);
	}

}
