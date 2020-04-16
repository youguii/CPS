package versionPlugin.ports;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.forplugins.AbstractInboundPortForPlugin;
import interfaces.MessageI;
import interfaces.ReceptionCI;
import interfaces.ReceptionImplementationI;

public class ReceptionCISubscriberInboundPortforPlugin 
extends AbstractInboundPortForPlugin 
implements ReceptionImplementationI {

    protected String uri;
    protected String pluginUri;
    
    public ReceptionCISubscriberInboundPortforPlugin(String pluginUri , ComponentI owner)
            throws Exception {
        super(ReceptionCI.class, pluginUri, owner);
        
		assert	pluginUri != null && owner instanceof ReceptionCI ;

    }

    public ReceptionCISubscriberInboundPortforPlugin(String uri,String pluginUri,ComponentI owner)
            throws Exception {
        super(uri, ReceptionCI.class,pluginUri, owner);
        
		assert owner instanceof ReceptionCI ;

    }



    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void acceptMessage(MessageI m) throws Exception {
    	System.out.println("reception du message 1"+ m.getURI());

    	new AbstractComponent.AbstractService<Void>(this.pluginURI) {
			@Override
			public Void call() throws Exception {
				((ReceptionImplementationI)
					this.getServiceProviderReference()).
				acceptMessage(m) ;
				return null;
			}
    	};
		
    	
    }

    @Override
    public void acceptMessage(MessageI[] ms) throws Exception {
    	new AbstractComponent.AbstractService<Void>(this.pluginURI) {
			@Override
			public Void call() throws Exception {
				((ReceptionImplementationI)
					this.getServiceProviderReference()).
				acceptMessage(ms) ;
				return null;
			}
    	};

    }
}

