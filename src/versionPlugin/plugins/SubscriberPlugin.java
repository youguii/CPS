package versionPlugin.plugins;

import connectors.ManagementCIConnector;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import interfaces.ManagementCI;
import interfaces.MessageFilterI;
import interfaces.MessageI;
import interfaces.ReceptionCI;
import interfaces.ReceptionImplementationI;
import ports.ManagementCISubscriberOutboundPort;
import versionPlugin.ports.ReceptionCISubscriberInboundPortforPlugin;

public class SubscriberPlugin 
extends		AbstractPlugin
implements	ReceptionImplementationI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected MessageI message;

	protected String managementBIPURI;
    protected String subscriberUri;

	   
    protected ManagementCISubscriberOutboundPort managementSubscriberOP;
    protected ReceptionCISubscriberInboundPortforPlugin receptionSubscriberIP;

	public SubscriberPlugin(String managementBIPURI,
    		String uri) {
		this.managementBIPURI = managementBIPURI;
		this.subscriberUri = uri;

	}
	
    
    
	// -------------------------------------------------------------------------
	// Life cycle
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#installOn(fr.sorbonne_u.components.ComponentI)
	 */
	@Override
	public void			installOn(ComponentI owner)
	throws Exception
	{
		super.installOn(owner) ;

   // Ajout des interfaces, Cr�ation des ports, Publication des ports	
        
        this.addRequiredInterface(ManagementCI.class);
        this.managementSubscriberOP = new ManagementCISubscriberOutboundPort(this.owner);    
        this.managementSubscriberOP.publishPort();
        
        this.addOfferedInterface(ReceptionCI.class);
        this.receptionSubscriberIP = new ReceptionCISubscriberInboundPortforPlugin(this.getPluginURI(),this.owner);
        this.receptionSubscriberIP.publishPort();
  
	}

	
	/**
	 * assume that the plug-in on the server component has already been
	 * installed and initialised.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true				// no more preconditions.
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.components.AbstractPlugin#initialise()
	 */
	@Override
	public void			initialise() throws Exception
	{
		
		this.owner.doPortConnection(
                this.managementSubscriberOP.getPortURI(),
                this.managementBIPURI,
                ManagementCIConnector.class.getCanonicalName());
        
		super.initialise();
	}

	
	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#finalise()
	 */
	@Override
	public void			finalise() throws Exception
	{
		this.owner.doPortDisconnection(this.managementSubscriberOP.getPortURI()) ;
        
        super.finalise();
	}

	
	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#uninstall()
	 */
	@Override
	public void			uninstall() throws Exception
	{
		this.managementSubscriberOP.unpublishPort() ;
		this.managementSubscriberOP.destroyPort() ;
		this.removeRequiredInterface(ManagementCI.class) ;
		
		this.receptionSubscriberIP.unpublishPort() ;
		this.receptionSubscriberIP.destroyPort() ;
		this.removeOfferedInterface(ReceptionCI.class) ;
	
	}
	
	
	private ReceptionImplementationI getOwner()
	{
		return ((ReceptionImplementationI)this.owner) ;
	}

	
	
	
	@Override
	public void acceptMessage(MessageI m) throws Exception {
		this.getOwner().acceptMessage(m);
	}

	@Override
	public void acceptMessage(MessageI[] ms) throws Exception {
		this.getOwner().acceptMessage(ms);		
	}

	
	
	
	
	public void createTopic(String topic) throws Exception {
		this.managementSubscriberOP.createTopic(topic);		
	}

	public void createTopics(String[] topics) throws Exception {
		this.managementSubscriberOP.createTopics(topics);
	}

	public void destroyTopics(String topic) throws Exception {
		this.managementSubscriberOP.destroyTopics(topic);	
	}

	public boolean isTopic(String topic) throws Exception {
		return 	this.managementSubscriberOP.isTopic(topic);
	}

	public String[] getTopics() throws Exception {
		return 	this.managementSubscriberOP.getTopics();
	}

	public String getPublicationPortURI() throws Exception {
		return 	this.managementSubscriberOP.getPublicationPortURI();
	}

	public void subscribe(String topic, String inboundPortURI) throws Exception {
		this.managementSubscriberOP.subscribe(topic, inboundPortURI);
	}

	public void subscribe(String[] topics, String inboundPortURI) throws Exception {
		this.managementSubscriberOP.subscribe(topics, inboundPortURI);
	}

	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception {
		this.managementSubscriberOP.subscribe(topic, filter, inboundPortURI);
	}

	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception {
		this.managementSubscriberOP.modifyFilter(topic, newFilter, inboundPortURI);	
	}

	public void unsubscribe(String topic, String inboundPortURI) throws Exception {
		this.managementSubscriberOP.unsubscribe(topic, inboundPortURI);
	}
	
	public String getReceptionPortURI() throws Exception {
		return this.receptionSubscriberIP.getPortURI();
	}

}
