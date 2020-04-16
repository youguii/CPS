package versionPlugin.plugins;

import connectors.ManagementCIConnector;
import connectors.PublicationCIConnector;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import interfaces.ManagementCI;
import interfaces.MessageI;
import interfaces.PublicationCI;
import ports.ManagementCIPublisherOutboundPort;
import ports.PublicationCIPublisherOutboundPort;

public class PublisherPlugin 
extends		AbstractPlugin
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	protected PublicationCIPublisherOutboundPort publicationPOP;
    protected ManagementCIPublisherOutboundPort managementPOP;
  
    protected String managementBIPURI;
    protected String uri;

    
    public PublisherPlugin(String managementBIPURI,
            String uri) {
        
        this.managementBIPURI = managementBIPURI;
        this.uri = uri;
        
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
        this.managementPOP= new ManagementCIPublisherOutboundPort(this.owner);
        this.managementPOP.publishPort();
  
        
        
        this.addRequiredInterface(PublicationCI.class);
        this.publicationPOP= new PublicationCIPublisherOutboundPort(this.owner);
        this.publicationPOP.publishPort();
  
      

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
                this.managementPOP.getPortURI(),
                this.managementBIPURI,
                ManagementCIConnector.class.getCanonicalName());
        
        String uripublicationPort = this.managementPOP.getPublicationPortURI();
				
        
        
        this.owner.doPortConnection(
                this.publicationPOP.getPortURI(),
                uripublicationPort,
                PublicationCIConnector.class.getCanonicalName());
   
    
		super.initialise();
	}

	
	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#finalise()
	 */
	@Override
	public void			finalise() throws Exception
	{
		this.owner.doPortDisconnection(this.managementPOP.getPortURI()) ;
		this.owner.doPortDisconnection(this.publicationPOP.getPortURI()) ;

	
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#uninstall()
	 */
	@Override
	public void			uninstall() throws Exception
	{
		this.managementPOP.unpublishPort() ;
		this.managementPOP.destroyPort() ;
		this.removeRequiredInterface(ManagementCI.class) ;
	
		this.publicationPOP.unpublishPort() ;
		this.publicationPOP.destroyPort() ;
		this.removeRequiredInterface(PublicationCI.class) ;
	
	}

	
    /**
     * publication d un message à un topic
     * @param topic
     * @param msg
     * @throws Exception
     */
	public void publishMessage(MessageI m, String topic) throws Exception {
		this.publicationPOP.publish(m, topic);
	}

	
    /**
     * publication d'un message associé à plusieurs topics
     * @param msg
     * @param topics
     * @throws Exception
     */
    public void publishMessageWithManyTopics(MessageI m, String[] topics) throws Exception {
		this.publicationPOP.publish(m, topics);
		
	}

    
    /**
     * appel la publicatpublication de plusieurs messages associé à un seul topic
     * @param msg
     * @param topic
     * @throws Exception
     */
     public void publishMessages(MessageI[] ms, String topic) throws Exception {
		this.publicationPOP.publish(ms, topic);
		
	}

     
    /**
    * publication de plusieurs messages associés à plusieurs topics
    * @param msg
    * @param topics
    * @throws Exception
    */
    public void publishMessagesWithManyTopics(MessageI[] ms, String[] topics) throws Exception {
		this.publicationPOP.publish(ms, topics);
	}

     
     
     
	public void createTopic(String topic) throws Exception {
		this.managementPOP.createTopic(topic);
	}

	public void createTopics(String[] topics) throws Exception {
		this.managementPOP.createTopics(topics);
	}

	public void destroyTopics(String topic) throws Exception {
		this.managementPOP.destroyTopics(topic);
	}

	public boolean isTopic(String topic) throws Exception {
		return this.managementPOP.isTopic(topic);
	}

	public String[] getTopics() throws Exception {
		return this.managementPOP.getTopics();
	}

	public String getPublicationPortURI() throws Exception {
		return this.managementPOP.getPublicationPortURI();
	}



}
