package ports;

import components.Broker;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.MessageI;
import interfaces.PublicationCI;
import interfaces.PublicationsImplementationI;

public class PublicationCIBrokerInboundPort
extends AbstractInboundPort implements
PublicationCI {

    protected String uri;
    protected final int executorIndex;

    //protected ComponentI owner;
    
    public PublicationCIBrokerInboundPort(String uri,
            ComponentI owner,
            int executorIndex)
            throws Exception {
        super(uri, PublicationCI.class, owner);
        
		assert	uri != null && owner instanceof Broker ;

        this.uri= uri;
        this.executorIndex = executorIndex;

        //this.owner= owner;
    }
    
    public PublicationCIBrokerInboundPort(
            ComponentI owner,
            int executorIndex)
            throws Exception {
        super(PublicationCI.class, owner);
        
		assert owner instanceof Broker ;
		
        this.executorIndex = executorIndex;
        //this.owner= owner;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // Appel Asynchrone
    @Override
    public void publish(MessageI m, String topic) throws Exception {
        this.getOwner().handleRequestSync(
        		executorIndex,
      
        new AbstractComponent.AbstractService<Integer>() {
			@Override
			public Integer call() throws Exception {
				((PublicationsImplementationI)owner).publish(m,topic) ;
				return null;
			}
		}) ;
    }

    
    // Appel Asynchrone
    @Override
    public void publish(MessageI m, String[] topics) throws Exception {
        this.getOwner().handleRequestSync(
        		executorIndex,
     
        new AbstractComponent.AbstractService<Integer>() {
			@Override
			public Integer call() throws Exception {
				((PublicationsImplementationI)owner).publish(m,topics); return null;
			}
		}) ;
    
    }

    // Appel Asynchrone
    @Override
    public void publish(MessageI[] ms, String topic) throws Exception {
        this.getOwner().handleRequestSync(
        		executorIndex,
	        new AbstractComponent.AbstractService<Integer>() {
				@Override
				public Integer call() throws Exception {
					((PublicationsImplementationI)owner).publish(ms,topic); return null;				}
			}) ;
    }

    // Appel Asynchrone
    @Override
    public void publish(MessageI[] ms, String[] topics) throws Exception {
        this.getOwner().handleRequestSync(
        		executorIndex,
        new AbstractComponent.AbstractService<Integer>() {
			@Override
			public Integer call() throws Exception {
				((PublicationsImplementationI)owner).publish(ms,topics); return null;				}
		}) ;
    
    }

}


