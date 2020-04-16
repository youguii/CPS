
package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import utiles.Message;
import utiles.MessageFilter;
import utiles.Properties;
import utiles.TimeStamp;

public class MessageTests {
	
	Properties prop ;


	Message m;
	
	Properties propf ;	
	MessageFilter msgf ;
	
	Properties propf2 ;
	MessageFilter msgf2 ;
	
	
	@Before
	public void init() throws Exception {
		prop = new Properties();
		initialiseProp();
		
		m = new Message("Message-Uri",
								(new TimeStamp(2, 
								"192.168.0.1")),
								prop,
								"UPMC");
		
		propf = new Properties();
		propf.putProp("Master", "STL");
		
		msgf = new MessageFilter(propf);
		
		propf2 = new Properties();
		propf2.putProp("Master", "DAC");
		
		msgf2 = new MessageFilter(propf2);

	
	}
		
	public void initialiseProp() throws Exception {
		prop.putProp("Master", "STL");
		prop.putProp("Admis", true);
	}
	
	@Test
	public void testMsg() throws Exception {
		assertEquals(m.getURI(), "Message-Uri");
		assertEquals(m.getTimeStamp().getTime(), 2);
		assertEquals(m.getTimeStamp().getTimestamper(), "192.168.0.1");
		assertEquals(m.getPayload(), "UPMC");
	}
	
	@Test
	public void testProp() throws Exception {
		assertEquals( m.getProperties().getStringProp("Master"), "STL");
		assertEquals( m.getProperties().getBooleanProp("Admis"), true);		
	}
	
	@Test
	public void testFilter() throws Exception {
		assertEquals(msgf.filter(m),true);
		assertEquals(msgf2.filter(m),false);
	}
		
	
}
