package ports;

import components.Broker;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ManagementCI;
import interfaces.ManagementImplementationI;
import interfaces.MessageFilterI;
import interfaces.SubscriptionImplementationI;

public class ManagementCIBrokerInboundPort extends AbstractInboundPort implements ManagementCI {
    
    protected String uri;
    protected ComponentI owner;
    protected final int executorIndex;

    public ManagementCIBrokerInboundPort(String uri,
            ComponentI owner,
            int executorIndex) throws Exception {
        super(uri, ManagementCI.class, owner);
        this.uri= uri;
        this.owner= owner;
        
        this.executorIndex = executorIndex;
        
		assert	uri != null && owner instanceof Broker ;
    
    }
    
    public ManagementCIBrokerInboundPort(
            ComponentI owner,
            int executorIndex) throws Exception {
        super(ManagementCI.class, owner);
        
		assert owner instanceof Broker ;

        this.owner= owner;
        
        this.executorIndex = executorIndex;
    
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // Appel Asynchrone
    @Override
    public void createTopic(String topic) throws Exception {
		this.getOwner().handleRequestSync(
				executorIndex,
        new AbstractComponent.AbstractService<Integer>() {
			@Override
			public Integer call() throws Exception {
				((Broker)owner).createTopic(topic);; return null;			}
		}) ;
    }

    // Appel Asynchrone
    @Override
    public void createTopics(String[] topics) throws Exception {
		this.getOwner().handleRequestSync(
				executorIndex,
        new AbstractComponent.AbstractService<Integer>() {
			@Override
			public Integer call() throws Exception {
				((Broker)owner).createTopics(topics);; return null;		}
		}) ;
    }

    // Appel Asynchrone    
    @Override
    public void destroyTopics(String topic) throws Exception {
		this.getOwner().handleRequestSync(
				executorIndex,
        new AbstractComponent.AbstractService<Integer>() {
			@Override
			public Integer call() throws Exception {
				((Broker)owner).destroyTopics(topic); return null; }
		}) ;
    }

    @Override
    public boolean isTopic(String topic) throws Exception {
        return this.getOwner().handleRequestSync(
        		executorIndex,
        new AbstractComponent.AbstractService<Boolean>() {
 			@Override
 			public Boolean call() throws Exception {
 				return ((Broker)owner).isTopic(topic); }
 		}) ;
    
    }

    @Override
    public String[] getTopics() throws Exception {
        return this.getOwner().handleRequestSync(
        		executorIndex,
       
        new AbstractComponent.AbstractService<String[]>() {
 			@Override
 			public String[] call() throws Exception {
 				return ((Broker)owner).getTopics(); }
 		}) ;

    }

    // Appel Asynchrone
    @Override
    public void subscribe(String topic, String inboundPortURI) throws Exception {
		this.getOwner().handleRequestSync(
				executorIndex,
        new AbstractComponent.AbstractService<Integer>() {
			@Override
			public Integer call() throws Exception {
				((SubscriptionImplementationI)owner).subscribe(topic, inboundPortURI); return null; }
		}) ;

    }

    // Appel Asynchrone
    @Override
    public void subscribe(String[] topics, String inboundPortURI) throws Exception {
		this.getOwner().handleRequestSync(
				executorIndex,
        new AbstractComponent.AbstractService<Integer>() {
			@Override
			public Integer call() throws Exception {
				((SubscriptionImplementationI)owner).subscribe(topics, inboundPortURI); return null; }
		}) ;
    }
    
    // Appel Asynchrone
    @Override
    public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception {
		this.getOwner().handleRequestSync(
				executorIndex,
        new AbstractComponent.AbstractService<Integer>() {
			@Override
			public Integer call() throws Exception {
				((SubscriptionImplementationI)owner).subscribe(topic, filter, inboundPortURI); return null; }
		}) ;
    }

    // Appel Asynchrone
    @Override
    public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception {
		this.getOwner().handleRequestSync(
				executorIndex,
        new AbstractComponent.AbstractService<Integer>() {
			@Override
			public Integer call() throws Exception {
				((SubscriptionImplementationI)owner).modifyFilter(topic, newFilter, inboundPortURI); return null; }
		}) ;
    }

    // Appel Asynchrone
    @Override
    public void unsubscribe(String topic, String inboundPortURI) throws Exception {
		this.getOwner().handleRequestSync(
				executorIndex,
		        new AbstractComponent.AbstractService<Integer>() {
					@Override
					public Integer call() throws Exception {
						((SubscriptionImplementationI)owner).unsubscribe(topic, inboundPortURI); return null; }
				}) ;
				
    }

	@Override
	public String getPublicationPortURI() throws Exception {
		return this.getOwner().handleRequestSync(
				executorIndex,
        new AbstractComponent.AbstractService<String>() {
			@Override
			public String call() throws Exception {
				return ((ManagementImplementationI)owner).getPublicationPortURI(); }
		}) ;
	}

}


