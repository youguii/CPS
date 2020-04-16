package utiles;

import java.util.Map;

import interfaces.MessageFilterI;
import interfaces.MessageI;

public class MessageFilter implements MessageFilterI {
	
	protected Properties properties;

	public MessageFilter(Properties properties) {
		super();
		this.properties = properties;
	}

	@Override
	public boolean filter(MessageI m) throws Exception {
		//Cette fonction vérifie que le message contient tous les critères du filtre
		
		Properties p= (Properties) m.getProperties();
		
		/* Pour chaque type de propriétés nous testons: 
		 * Que le nom de la propriétés du filtre existe bien dans les propriétés du message
		 * Et que la valeur de la propriétés du message est la meme que celle du filtre
		 */
		
		//Propriétés booléennes
		if(p.boolProperties.size() >= properties.boolProperties.size()){
			/* Ici les propriétés du message sont supérieures ou égales à celles du filtre 
			 * Donc les propriétés du filtre sont potentiellement contenue dans le msg 
			 */
	        for(Map.Entry<String, Boolean> entry :properties.boolProperties.entrySet()) {
				if(!(p.boolProperties.containsKey(entry.getKey()) 
						&& p.boolProperties.get(entry.getKey())== entry.getValue())) {
					/* Si le nom de propriété du filtre n'est pas contenu dans les propriétés du message
					 * Ou que la valeur n'est pas la meme, on retourne false
					 */
					return false;
				}
			}
		}else {
			/* Ici le message a moins de propriétés que le filtre
			 * Donc le message ne contient pas toute les propriétés du filtre
			 */
			return false;
		}
		
		//Propriétés entières
		if(p.intProperties.size() >= properties.intProperties.size()){
			/* Ici les propriétés du message sont supérieures ou égales à celles du filtre 
			 * Donc les propriétés du filtre sont potentiellement contenue dans le msg 
			 */
	        for(Map.Entry<String, Integer> entry :properties.intProperties.entrySet()) {
				if(!(p.intProperties.containsKey(entry.getKey()) 
						&& p.intProperties.get(entry.getKey())== entry.getValue())) {
					return false;
				}
			}
		}else {
			/* Ici le message a moins de propriétés que le filtre
			 * Donc le message ne contient pas toute les propriétés du filtre
			 */
			return false;
		}

		//Propriétés floatantes
		if(p.floatProperties.size() >= properties.floatProperties.size()){
			/* Ici les propriétés du message sont supérieures ou égales à celles du filtre 
			 * Donc les propriétés du filtre sont potentiellement contenue dans le msg 
			 */
	        for(Map.Entry<String, Float> entry :properties.floatProperties.entrySet()) {
				if(!(p.floatProperties.containsKey(entry.getKey()) 
						&& p.floatProperties.get(entry.getKey())== entry.getValue())) {
					return false;
				}
			}
		}else {
			/* Ici le message a moins de propriétés que le filtre
			 * Donc le message ne contient pas toute les propriétés du filtre
			 */
			return false;
		}

		//Propriétés doubles
		if(p.doubleProperties.size() >= properties.doubleProperties.size()){
			/* Ici les propriétés du message sont supérieures ou égales à celles du filtre 
			 * Donc les propriétés du filtre sont potentiellement contenue dans le msg 
			 */
	        for(Map.Entry<String, Double> entry :properties.doubleProperties.entrySet()) {
				if(!(p.doubleProperties.containsKey(entry.getKey()) 
						&& p.doubleProperties.get(entry.getKey())== entry.getValue())) {
					return false;
				}
			}
		}else {
			/* Ici le message a moins de propriétés que le filtre
			 * Donc le message ne contient pas toute les propriétés du filtre
			 */
			return false;
		}
	
		//Propriétés long
		if(p.longProperties.size() >= properties.longProperties.size()){
			/* Ici les propriétés du message sont supérieures ou égales à celles du filtre 
			 * Donc les propriétés du filtre sont potentiellement contenue dans le msg 
			 */
	        for(Map.Entry<String, Long> entry :properties.longProperties.entrySet()) {
				if(!(p.longProperties.containsKey(entry.getKey()) 
						&& p.longProperties.get(entry.getKey())== entry.getValue())) {
					return false;
				}
			}
		}else {
			/* Ici le message a moins de propriétés que le filtre
			 * Donc le message ne contient pas toute les propriétés du filtre
			 */
			return false;
		}

		//Propriétés short
		if(p.shortProperties.size() >= properties.shortProperties.size()){
			/* Ici les propriétés du message sont supérieures ou égales à celles du filtre 
			 * Donc les propriétés du filtre sont potentiellement contenue dans le msg 
			 */
	        for(Map.Entry<String, Short> entry :properties.shortProperties.entrySet()) {
				if(!(p.shortProperties.containsKey(entry.getKey()) 
						&& p.shortProperties.get(entry.getKey())== entry.getValue())) {
					return false;
				}
			}
		}else {
			/* Ici le message a moins de propriétés que le filtre
			 * Donc le message ne contient pas toute les propriétés du filtre
			 */
			return false;
		}

		//Propriétés char
		if(p.charProperties.size() >= properties.charProperties.size()){
			/* Ici les propriétés du message sont supérieures ou égales à celles du filtre 
			 * Donc les propriétés du filtre sont potentiellement contenue dans le msg 
			 */
	        for(Map.Entry<String, Character> entry :properties.charProperties.entrySet()) {
				if(!(p.charProperties.containsKey(entry.getKey()) 
						&& p.charProperties.get(entry.getKey())== entry.getValue())) {
					return false;
				}
			}
		}else {
			/* Ici le message a moins de propriétés que le filtre
			 * Donc le message ne contient pas toute les propriétés du filtre
			 */
			return false;
		}
		
		//Propriétés String
		if(p.strProperties.size() >= properties.strProperties.size()){
			/* Ici les propriétés du message sont supérieures ou égales à celles du filtre 
			 * Donc les propriétés du filtre sont potentiellement contenue dans le msg 
			 */
	        for(Map.Entry<String, String> entry :properties.strProperties.entrySet()) {
				if(!(p.strProperties.containsKey(entry.getKey()) 
						&& p.strProperties.get(entry.getKey())== entry.getValue())) {
					return false;
				}
			}
		}else {
			/* Ici le message a moins de propriétés que le filtre
			 * Donc le message ne contient pas toute les propriétés du filtre
			 */
			return false;
		}

		//Propriétés bytes
		if(p.byteProperties.size() >= properties.byteProperties.size()){
			/* Ici les propriétés du message sont supérieures ou égales à celles du filtre 
			 * Donc les propriétés du filtre sont potentiellement contenue dans le msg 
			 */
	        for(Map.Entry<String, Byte> entry :properties.byteProperties.entrySet()) {
	        	if(!(p.byteProperties.containsKey(entry.getKey()) 
						&& p.byteProperties.get(entry.getKey())== entry.getValue())) {
					return false;
				}
			}
		}else {
			/* Ici le message a moins de propriétés que le filtre
			 * Donc le message ne contient pas toute les propriétés du filtre
			 */
			return false;
		}
		
		return true;
	}
	
	public Properties getProperties() {
		return properties;
	}

}
