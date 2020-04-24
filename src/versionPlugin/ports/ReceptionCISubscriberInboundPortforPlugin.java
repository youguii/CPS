package versionPlugin.ports;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.forplugins.AbstractInboundPortForPlugin;
import interfaces.MessageI;
import interfaces.ReceptionCI;
import interfaces.ReceptionImplementationI;

public class ReceptionCISubscriberInboundPortforPlugin 
extends AbstractInboundPortForPlugin 
implements ReceptionCI {

    protected String uri;
    protected String pluginUri;
    
    public ReceptionCISubscriberInboundPortforPlugin(String pluginUri , ComponentI owner)
            throws Exception {
        super(ReceptionCI.class, pluginUri, owner);
        
        
		assert	pluginUri != null && owner instanceof ReceptionImplementationI ;
		
        this.pluginUri= pluginUri;

    }

    public ReceptionCISubscriberInboundPortforPlugin(String uri,String pluginUri,ComponentI owner)
            throws Exception {
        super(uri, ReceptionCI.class,pluginUri, owner);
        
        this.uri = uri;
        this.pluginUri = pluginUri;
        
		assert owner instanceof ReceptionImplementationI ;

    }



    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void acceptMessage(MessageI m) throws Exception {
    	System.out.println("reception du message 1"+ m.getURI());

    	this.owner.handleRequestSync(
    	    	new AbstractComponent.AbstractService<Void>(this.pluginURI) {
    				@Override
    				public Void call() throws Exception {
    					((ReceptionImplementationI)
    						this.getServiceProviderReference()).
    					acceptMessage(m) ;
    					return null;
    				}
    	    	});
    	
    }

    @Override
    public void acceptMessage(MessageI[] ms) throws Exception {
    	this.owner.handleRequestSync(
    	new AbstractComponent.AbstractService<Void>(this.pluginURI) {
			@Override
			public Void call() throws Exception {
				((ReceptionImplementationI)
					this.getServiceProviderReference()).
				acceptMessage(ms) ;
				return null;
			}
    	});

    }
}

