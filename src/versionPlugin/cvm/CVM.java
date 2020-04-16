package versionPlugin.cvm;

import components.Broker;
import versionPlugin.components.Publisher;
import versionPlugin.components.Subscriber;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

public class CVM 
extends AbstractCVM {
    

    protected static final String managementBIPURI = "managementBIPURI";


    
    
    public CVM() throws Exception {
        super();
    }
    
    @Override
    public void    deploy() throws Exception
    {
		assert	!this.deploymentDone() ;
        
    	String uriBroker = AbstractComponent.createComponent(
                Broker.class.getCanonicalName(),
                new Object[] { managementBIPURI});
    	
		assert	this.isDeployedComponent(uriBroker) ;
		
	
		for(int i= 0; i < 3; i++) {
		
			String uriPublisher = AbstractComponent.createComponent(
	        Publisher.class.getCanonicalName(),
	        new Object[] {managementBIPURI, Integer.toString(i) });
	    	
	    	assert	this.isDeployedComponent(uriPublisher) ;

		}

		for(int i= 0; i < 3 ; i++) {
			
			String uriSubscriber = AbstractComponent.createComponent(
	        Subscriber.class.getCanonicalName(),
	        new Object[] {managementBIPURI, Integer.toString(i)});
	        
			assert	this.isDeployedComponent(uriSubscriber) ;
			

		}

        super.deploy();
        
		assert	this.deploymentDone() ;
    }
    
    
    
    public static void    main(String[] args)
    {
        
        try {
            
            System.out.println("Commencement");
            // Create an instance of the defined component virtual machine.
            CVM a = new CVM() ;
            // Execute the application.
            a.startStandardLifeCycle(10000L) ;
            // Give some time to see the traces (convenience).
            Thread.sleep(10000L) ;
            // Simplifies the termination (termination has yet to be treated
            // properly in BCM).
            System.exit(0) ;
           
        } catch (Exception e) {
            throw new RuntimeException(e);
            
        }
        
    }

}


