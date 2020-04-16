package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.MessageI;
import interfaces.PublicationCI;

public class PublicationCIPublisherOutboundPort extends AbstractOutboundPort implements PublicationCI{

    protected String uri;
    protected ComponentI owner;
    
    public PublicationCIPublisherOutboundPort(String uri, ComponentI owner)
            throws Exception {
        super(uri, PublicationCI.class, owner);
        
		assert	uri != null && owner != null ;

        this.uri= uri;
        this.owner= owner;
    }
    
    public PublicationCIPublisherOutboundPort(ComponentI owner)
            throws Exception {
        super(PublicationCI.class, owner);
        
		assert owner != null ;

        this.owner= owner;
    }


    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void publish(MessageI m, String topic) throws Exception {
        ((PublicationCI)this.connector).publish(m, topic);
        
    }

    @Override
    public void publish(MessageI m, String[] topics) throws Exception {
    	((PublicationCI)this.connector).publish(m, topics);       
    }

    @Override
    public void publish(MessageI[] ms, String topic) throws Exception {
    	((PublicationCI)this.connector).publish(ms, topic);        
    }

    @Override
    public void publish(MessageI[] ms, String[] topics) throws Exception {
    	((PublicationCI)this.connector).publish(ms, topics);
        
    }


}


