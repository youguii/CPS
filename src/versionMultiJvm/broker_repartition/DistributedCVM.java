//CPSDistrib/src/versionMultiJvm/broker_repartition/jars/CPSDistrib.jar


package versionMultiJvm.broker_repartition;


import components.Publisher;
import components.Subscriber;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;
import versionMultiJvm.broker_repartition.components.Broker;



public class			DistributedCVM
extends		AbstractDistributedCVM
{
	
	protected static final String	JVM1_URI = "jvm1" ;
	protected static final String	JVM2_URI = "jvm2" ;
	protected static final String	JVM3_URI = "jvm3" ;


    protected static final String managementBIPURI1 = "managementBIPURI1";
    protected static final String managementBIPURI2 = "managementBIPURI2";
    protected static final String managementBIPURI3 = "managementBIPURI3";

    public static final String[] BROKER_INBOUND_PORT_URIS =
			new String[]{
				"brokerC_IP_URI1",
				"brokerC_IP_URI2",
				"brokerC_IP_URI3"
	} ;

    
	public				DistributedCVM(String[] args, int xLayout, int yLayout)
	throws Exception
	{
		super(args, xLayout, yLayout);
	}




	@Override
	public void			instantiateAndPublish() throws Exception
	{
		System.out.println(thisJVMURI);
		if (thisJVMURI.equals(JVM1_URI)) {
					
			AbstractComponent.createComponent(
	                Broker.class.getCanonicalName(),
	                new Object[] { 
	                		managementBIPURI1, 
	                		BROKER_INBOUND_PORT_URIS , 
	                		BROKER_INBOUND_PORT_URIS[0], 
	                		"B_JVM1",
	                		false});


			AbstractComponent.createComponent(
					Subscriber.class.getCanonicalName(),
					new Object[] {managementBIPURI1, Integer.toString(0), false});

			AbstractComponent.createComponent(
					Publisher.class.getCanonicalName(),
					new Object[] {managementBIPURI1, Integer.toString(0)});


		} else if (thisJVMURI.equals(JVM2_URI)) {
			
			AbstractComponent.createComponent(
	                Broker.class.getCanonicalName(),
	                new Object[] { 
	                		managementBIPURI2, 
	                		BROKER_INBOUND_PORT_URIS, 
	                		BROKER_INBOUND_PORT_URIS[1],
	                		"B_JVM2",
	                		false});
	    	
			AbstractComponent.createComponent(
					Publisher.class.getCanonicalName(),
					new Object[] {managementBIPURI2, Integer.toString(1) });
			
			AbstractComponent.createComponent(
					Subscriber.class.getCanonicalName(),
					new Object[] {managementBIPURI2, Integer.toString(1), false});


		} else if (thisJVMURI.equals(JVM3_URI)) {
						
			AbstractComponent.createComponent(
	                Broker.class.getCanonicalName(),
	                new Object[] { 
	                		managementBIPURI3, 
	                		BROKER_INBOUND_PORT_URIS, 
	                		BROKER_INBOUND_PORT_URIS[2],
	                		"B_JVM3",
	                		false});
	    	
		
			AbstractComponent.createComponent(
					Publisher.class.getCanonicalName(),
					new Object[] {managementBIPURI3, Integer.toString(2) });

				
			AbstractComponent.createComponent(
					Subscriber.class.getCanonicalName(),
					new Object[] {managementBIPURI3, Integer.toString(2), false});
			
		
		
		} else {

			System.out.println("Unknown JVM URI... " + thisJVMURI) ;

		}

		super.instantiateAndPublish();
	}

	
	
	
	public static void	main(String[] args)
	{
		try {
			DistributedCVM da  = new DistributedCVM(args, 2, 5) ;
			da.startStandardLifeCycle(15000L) ;
			Thread.sleep(10000L) ;
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
}
//-----------------------------------------------------------------------------
