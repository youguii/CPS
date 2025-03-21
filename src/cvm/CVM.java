package cvm;

import components.Broker;
import components.Publisher;
import components.Subscriber;
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
		
		// --------------------------------------------------------------------
		// Configuration phase
		// --------------------------------------------------------------------

		// debugging mode configuration; comment and uncomment the line to see
		// the difference
//		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.PUBLIHSING) ;
//		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CONNECTING) ;
//		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.COMPONENT_DEPLOYMENT) ;

		// --------------------------------------------------------------------
		// Creation phase
		// --------------------------------------------------------------------

        
    	String uriBroker = AbstractComponent.createComponent(
                Broker.class.getCanonicalName(),
                new Object[] { managementBIPURI});
    	
		assert	this.isDeployedComponent(uriBroker) ;
		
	
		for(int i= 0; i < 3; i++) {
		
			AbstractComponent.createComponent(
	        Publisher.class.getCanonicalName(),
	        new Object[] {managementBIPURI, Integer.toString(i) });
	    	

		}

		for(int i= 0; i < 3 ; i++) {
			
			AbstractComponent.createComponent(
	        Subscriber.class.getCanonicalName(),
	        new Object[] {managementBIPURI, Integer.toString(i), false});
	        
		}

        super.deploy();
        
		assert	this.deploymentDone() ;
    }
    
    
    
    public static void    main(String[] args)
    {
        
        try {
            
            System.out.println("Commencement cvm simple");
            // Create an instance of the defined component virtual machine.
            CVM a = new CVM() ;
            // Execute the application.
            a.startStandardLifeCycle(20000L) ;
            // Give some time to see the traces (convenience).
            Thread.sleep(20000L) ;
            // Simplifies the termination (termination has yet to be treated
            // properly in BCM).
            System.exit(0) ;
           
        } catch (Exception e) {
            throw new RuntimeException(e);
            
        }
        
    }

}


