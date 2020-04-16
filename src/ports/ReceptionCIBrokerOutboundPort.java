package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.MessageI;
import interfaces.ReceptionCI;

public class ReceptionCIBrokerOutboundPort extends AbstractOutboundPort implements ReceptionCI {

    protected String uri;
    protected ComponentI owner;
    
    public ReceptionCIBrokerOutboundPort(String uri,
            ComponentI owner) throws Exception {
        super(uri, ReceptionCI.class, owner);
        
		assert	uri != null && owner != null ;

        this.uri= uri;
        this.owner= owner;
    }
    
    public ReceptionCIBrokerOutboundPort(
            ComponentI owner) throws Exception {
        super(ReceptionCI.class, owner);
        
		assert	owner != null ;

        this.owner= owner;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void acceptMessage(MessageI m) throws Exception {
    	((ReceptionCI)this.connector).acceptMessage(m);
    }

    @Override
    public void acceptMessage(MessageI[] ms) throws Exception {
		((ReceptionCI)this.connector).acceptMessage(ms);        
    }

}


