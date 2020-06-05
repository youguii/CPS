package testsPerformance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import components.Broker;
import components.Publisher;
import components.Subscriber;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.helpers.CVMDebugModes;

public class CVM 
extends AbstractCVM {
    

    protected static final String managementBIPURI = "managementBIPURI";
    protected int nbPublisher;
    
    public CVM(int nbPublisher) throws Exception {
        super();
        
        this.nbPublisher = nbPublisher;
        
        BufferedWriter bOut= null ;
		try{
            File inputFile = new File("/home/kiska/git/CPS/src/testsPerformance/test.txt");
            bOut = new BufferedWriter(new FileWriter(inputFile, true)) ;
            bOut.write("Calcul du temps d'acheminement d'un message pour un nombre de publishers = "+nbPublisher+"\n");

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
        }
        
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
		
	
		for(int i= 0; i < nbPublisher; i++) {
		
			String uriPublisher = AbstractComponent.createComponent(
	        Publisher.class.getCanonicalName(),
	        new Object[] {managementBIPURI, Integer.toString(i) });
	    	

		}

		for(int i= 0; i < 3 ; i++) {
			
			String uriSubscriber = AbstractComponent.createComponent(
	        Subscriber.class.getCanonicalName(),
	        new Object[] {managementBIPURI, Integer.toString(i)});
	        
			

		}

        super.deploy();
        
		assert	this.deploymentDone() ;
    }
    
    
    
    public static void    main(String[] args)
    {
    	

            try {
                
            	for(int nbCVM = 1 ; nbCVM <= 10 ; nbCVM++) {

                     System.out.println("Commencement cvm simple");
                     // Create an instance of the defined component virtual machine.
                     CVM a = new CVM(nbCVM*30) ;
                     // Execute the application.
                     a.startStandardLifeCycle(10000L) ;
                     // Give some time to see the traces (convenience).
                     Thread.sleep(10000L) ;
                     // Simplifies the termination (termination has yet to be treated
                     // properly in BCM).
            	}
            	               
            } catch (Exception e) {
                throw new RuntimeException(e);
                
            }
            
        System.exit(0) ;

        
    }

}


