
package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import interfaces.MessageFilterI;
import interfaces.MessageI;
import utiles.Message;
import utiles.MessageFilter;
import utiles.Properties;
import utiles.TimeStamp;

public class MessageTests {
	
	Properties prop ;

	Message m;
	
	MessageFilter msgf ;
	
	MessageFilterI msgf2 ;
	
	
	@Before
	public void init() throws Exception {
		prop = new Properties();
		initialiseProp();
		
		
		m = new Message("Message-Uri",
								(new TimeStamp(2, 
								"192.168.0.1")),
								prop,
								"UPMC");
		
		
		msgf = new MessageFilter(1, 100, "STL", null);
		
		msgf2 = new MessageFilter(10, null, null, null);

	
	}
		
	public void initialiseProp() throws Exception {
		prop.putProp("lenM", 5);
		prop.putProp("topic", "STL");
		prop.putProp("new", true);
		prop.putProp("Pi", 3.14 );
		
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
		assertEquals( m.getProperties().getStringProp("topic"), "STL" );
		assertEquals( m.getProperties().getBooleanProp("new"), true);
		assertEquals( m.getProperties().getIntProp("lenM"), Integer.valueOf(5) );
	}
	
	@Test
	public void testFilter() throws Exception {
		assertEquals(msgf.filter(m),true);
		assertEquals(msgf2.filter(m),false);
		
		MessageFilterI filter = new MessageFilterI() {
			@Override
			public boolean filter(MessageI m) throws Exception {	
				if(m.getProperties().getIntProp("lenM") == 5) {
					return true;
				}
				return false;
			}
		};
		
		assertEquals(filter.filter(m),true);
		
		
	}
		
	
}
