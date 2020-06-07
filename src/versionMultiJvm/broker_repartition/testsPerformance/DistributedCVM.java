//CPSDistrib/src/versionMultiJvm/broker_repartition/testsPerformance/jars/CPSDistrib.jar


package versionMultiJvm.broker_repartition.testsPerformance;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
    
    
    protected static boolean done;
    
    
    
    
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
		done = false;
	}



	@Override
	public void			instantiateAndPublish() throws Exception
	{
		System.out.println(thisJVMURI);
		if (thisJVMURI.equals(JVM1_URI)) {
		
			writeInFile();
			
			AbstractComponent.createComponent(
	                Broker.class.getCanonicalName(),
	                new Object[] { 
	                		managementBIPURI1, 
	                		BROKER_INBOUND_PORT_URIS , 
	                		BROKER_INBOUND_PORT_URIS[0], 
	                		"B_JVM1",
	                		true});


			AbstractComponent.createComponent(
					Subscriber.class.getCanonicalName(),
					new Object[] {managementBIPURI1, Integer.toString(0), true});

			for(int i= 0; i < Configuration.nb_publishers_DCVM; i++) {
				AbstractComponent.createComponent(
						Publisher.class.getCanonicalName(),
						new Object[] {managementBIPURI1, Integer.toString(i+1000) });
			}


		} else if (thisJVMURI.equals(JVM2_URI)) {
			
			AbstractComponent.createComponent(
	                Broker.class.getCanonicalName(),
	                new Object[] { 
	                		managementBIPURI2, 
	                		BROKER_INBOUND_PORT_URIS, 
	                		BROKER_INBOUND_PORT_URIS[1],
	                		"B_JVM2",
	                		true});
	    	
			for(int i= 0; i < Configuration.nb_publishers_DCVM ; i++) {

				AbstractComponent.createComponent(
						Publisher.class.getCanonicalName(),
						new Object[] {managementBIPURI2, Integer.toString(i+2000) });
			}
			
			AbstractComponent.createComponent(
					Subscriber.class.getCanonicalName(),
					new Object[] {managementBIPURI2, Integer.toString(1), true});


		} else if (thisJVMURI.equals(JVM3_URI)) {
						
			AbstractComponent.createComponent(
	                Broker.class.getCanonicalName(),
	                new Object[] { 
	                		managementBIPURI3, 
	                		BROKER_INBOUND_PORT_URIS, 
	                		BROKER_INBOUND_PORT_URIS[2],
	                		"B_JVM3",
	                		true});
	    	
		
			for(int i= 0; i < Configuration.nb_publishers_DCVM; i++) {

				AbstractComponent.createComponent(
						Publisher.class.getCanonicalName(),
						new Object[] {managementBIPURI3, Integer.toString(i+3000) });
			}

			AbstractComponent.createComponent(
					Subscriber.class.getCanonicalName(),
					new Object[] {managementBIPURI3, Integer.toString(2), true});
			
		
		
		} else {

			System.out.println("Unknown JVM URI... " + thisJVMURI) ;

		}

		super.instantiateAndPublish();
	}
	
	
	public void writeInFile() {
		if (!done) {
			done = true;
			BufferedWriter bOut= null ;
			BufferedWriter bOut2= null ;

			try{
	            File inputFile = new File(Configuration.path_t);
	            File inputFile2 = new File(Configuration.path_m);

	            bOut = new BufferedWriter(new FileWriter(inputFile, true)) ;
	            bOut2 = new BufferedWriter(new FileWriter(inputFile2, true)) ;

	            bOut.write("\nDCVM : Calcul du temps d'acheminement d'un message reçu par chaque subscriber et pour un nombre de publishers = "+Configuration.nb_publishers_DCVM * 3+" \n");
	            bOut2.write("\nDCVM : Calcul du nombre moyen de messages en attente de livraison pour chaque JVM avec un nombre total de publishers = "+Configuration.nb_publishers_DCVM * 3+" \n");

			}catch(IOException e) {
	            System.out.println(e) ;
	        }
		    finally{
		    	
			    if (bOut != null)
			        try {
			            bOut.close();
			        }catch(IOException ec) {
			            System.out.println(ec) ;
			        }
			    if (bOut2 != null)
			        try {
			            bOut2.close();
			        }catch(IOException ec) {
			            System.out.println(ec) ;
			        }
	        }

		}
	}

	
	
	
	public static void	main(String[] args)
	{
		try {

				DistributedCVM da  = new DistributedCVM(args, 2, 5) ;
				da.startStandardLifeCycle(10000L) ;
				Thread.sleep(2000L) ;
				System.exit(0) ;
		
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
}
//-----------------------------------------------------------------------------
