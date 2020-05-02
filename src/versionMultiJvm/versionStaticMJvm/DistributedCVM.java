package versionMultiJvm.versionStaticMJvm;


import components.Broker;
import components.Publisher;
import components.Subscriber;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;



public class			DistributedCVM
extends		AbstractDistributedCVM
{
	protected static final String	JVM1_URI = "jvm1" ;
	protected static final String	JVM2_URI = "jvm2" ;

    protected static final String managementBIPURI = "managementBIPURI";
	
	
	
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
	                new Object[] { managementBIPURI});
	    	
		
		
			AbstractComponent.createComponent(
					Publisher.class.getCanonicalName(),
					new Object[] {managementBIPURI, Integer.toString(1) });
	    	

			AbstractComponent.createComponent(
					Subscriber.class.getCanonicalName(),
					new Object[] {managementBIPURI, Integer.toString(1)});


		} else if (thisJVMURI.equals(JVM2_URI)) {

			AbstractComponent.createComponent(
	                Broker.class.getCanonicalName(),
	                new Object[] { managementBIPURI});
	    	
		
			AbstractComponent.createComponent(
					Publisher.class.getCanonicalName(),
					new Object[] {managementBIPURI, Integer.toString(1) });
	    	

			AbstractComponent.createComponent(
					Subscriber.class.getCanonicalName(),
					new Object[] {managementBIPURI, Integer.toString(1)});


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
