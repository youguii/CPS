package ports;

import components.Subscriber;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.MessageI;
import interfaces.ReceptionCI;
import interfaces.ReceptionImplementationI;

public class ReceptionCISubscriberInboundPort extends AbstractInboundPort implements ReceptionCI {

    protected String uri;
    protected ComponentI owner;
    
    public ReceptionCISubscriberInboundPort(String uri , ComponentI owner)
            throws Exception {
        super(uri, ReceptionCI.class, owner);
        
		assert	uri != null && owner instanceof Subscriber ;
        
        this.uri= uri;
        this.owner= owner;
    }

    public ReceptionCISubscriberInboundPort(ComponentI owner)
            throws Exception {
        super(ReceptionCI.class, owner);
        
		assert owner instanceof Subscriber ;
        this.owner= owner;

    }



    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void acceptMessage(MessageI m) throws Exception {
    	 this.getOwner().handleRequestSync(owner -> {((ReceptionImplementationI)owner).acceptMessage(m); return null;});
    }

    @Override
    public void acceptMessage(MessageI[] ms) throws Exception {
    	this.getOwner().handleRequestSync(owner -> {((ReceptionImplementationI)owner).acceptMessage(ms); return null;});
    }

}


