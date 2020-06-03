package components;

import java.io.Serializable;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import connectors.ManagementCIConnector;
import connectors.PublicationCIConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import interfaces.ManagementCI;
import interfaces.PublicationCI;
import ports.ManagementCIPublisherOutboundPort;
import ports.PublicationCIPublisherOutboundPort;
import testsIntegration.SenariosPublisher;
import utiles.Message;
import utiles.Properties;
import utiles.TimeStamp;

public class Publisher extends AbstractComponent {

	/** Port sortant utilisé pour appeler le service publication */
	protected PublicationCIPublisherOutboundPort publicationPOP;
	/** Port sortant utilisé pour appeler le service management */
	protected ManagementCIPublisherOutboundPort managementPOP;

	/** Uri du port entrant du service management */
	protected String managementBIPURI;
	/**
	 * uri pour distinguer entre les publiers (utilisé lors du lancement des
	 * senarios)
	 */
	protected String publisherUri;
	
	
	/**Tests d'integrations*/
	protected SenariosPublisher sp ;
	

	/**
	 *
	 * @param managementBIPURI Uri du port entrant du service management
	 * @param uri              uri pour distinguer entre les publiers
	 * @throws Exception
	 */
	protected Publisher(String managementBIPURI, String uri) throws Exception {

		super(2, 1);

		assert managementBIPURI != null : new PreconditionException(
				"Publisher : Broker's management port can't be null");

		assert uri != null : new PreconditionException("Publisher : publisher uri can't be null");

		this.managementBIPURI = managementBIPURI;
		this.publisherUri = uri;

		// Ajout des interfaces, Cr�ation des ports, Publication des ports

		this.addRequiredInterface(ManagementCI.class);
		this.managementPOP = new ManagementCIPublisherOutboundPort(this);
		this.managementPOP.publishPort();

		this.addRequiredInterface(PublicationCI.class);
		this.publicationPOP = new PublicationCIPublisherOutboundPort(this);
		this.publicationPOP.publishPort();

		// Affichage de la console
		this.tracer.setTitle("publisher" + uri);
		this.tracer.setRelativePosition(Integer.parseInt(uri), 1);
		this.toggleTracing();

		
		//initialisation de tests senarios
		sp = new SenariosPublisher(this);
	}

	// ------------------------------------------------------------------------
	// Component life-cycle
	// ------------------------------------------------------------------------

	@Override
	public void start() throws ComponentStartException {
		super.start();
		this.logMessage("starting publisher component.");
		
		try {
	
		}catch (Exception e) {
			throw new ComponentStartException(e);
		}

	}

	@Override
	public void execute() throws Exception {
		super.execute();
		this.logMessage("executing publisher component.");

		try {


			this.doPortConnection(this.managementPOP.getPortURI(), this.managementBIPURI,
					ManagementCIConnector.class.getCanonicalName());


			String uripublicationPort = this.getPublicationUri();


			this.logMessage("L'uri publication du Broker = " + uripublicationPort);


			this.doPortConnection(this.publicationPOP.getPortURI(), uripublicationPort,
					PublicationCIConnector.class.getCanonicalName());


			assert this.publicationPOP.connected();
			assert this.publicationPOP.getConnector().connected();

			// Tests d'intégration / Différents Senarios du Publier
			switch (this.publisherUri) {
			case "0":
				sp.senario_One();
				sp.senario_Four(10, 0);
				break;
			case "1":
				sp.senario_Two();
				sp.senario_Four(10, 1);

				break;
			case "2":
				sp.senario_Three();
				sp.senario_Four(10, 2);

				break;
			default:
				sp.senario_One();
				sp.senario_Four(10, 3);

			}

		} catch (Exception e) {
			throw new Exception(e);
		}

	}

	@Override
	public void finalise() throws Exception {
		this.logMessage("stopping publisher component.");
		this.printExecutionLogOnFile("publisher");


		this.managementPOP.doDisconnection();
		this.publicationPOP.doDisconnection();

		super.finalise();
	}

	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			this.managementPOP.unpublishPort();
			this.publicationPOP.unpublishPort();

		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}

		super.shutdown();
	}

	// ------------------------------------------------------------------------
	// Component usefullMethodes
	// ------------------------------------------------------------------------


	/**
	 * Génération de Message avec au maximum une propriété Fonction qui facilite la
	 * création d'un message minimal
	 * 
	 * @param uri           l'uri du Message
	 * @param proprertyName le nom de la propriété qui est associé au message
	 * @param propertyValue la valeur de la propriété
	 * @param contenu       le contenu du message qui est un objet serialisable
	 * @return un Objet de type Message initialisé avec les différents parametres
	 * @throws Exception
	 */
	public Message generateMsg(String uri, HashMap<String,Object> props, Serializable contenu)
			throws Exception {
	
		
		Message msg = new Message(uri, new TimeStamp((new Timestamp(System.currentTimeMillis())).getTime(),
				InetAddress.getLocalHost().getHostAddress()), new Properties(), contenu);

		String proprertyName;
		Object propertyValue;
		if(props != null) {
			for (Map.Entry<String, Object> entry : props.entrySet()) {
				
				proprertyName = entry.getKey();
				propertyValue = entry.getValue();
	
				if (propertyValue instanceof String)
					msg.getProperties().putProp(proprertyName, (String) propertyValue);
				if (propertyValue instanceof Byte)
					msg.getProperties().putProp(proprertyName, (Byte) propertyValue);
				if (propertyValue instanceof Integer)
					msg.getProperties().putProp(proprertyName, (Integer) propertyValue);
				if (propertyValue instanceof Float)
					msg.getProperties().putProp(proprertyName, (Float) propertyValue);
				if (propertyValue instanceof Double)
					msg.getProperties().putProp(proprertyName, (Double) propertyValue);
				if (propertyValue instanceof Character)
					msg.getProperties().putProp(proprertyName, (Character) propertyValue);
				if (propertyValue instanceof Long)
					msg.getProperties().putProp(proprertyName, (Long) propertyValue);
				if (propertyValue instanceof Short)
					msg.getProperties().putProp(proprertyName, (Short) propertyValue);
				if (propertyValue instanceof Boolean)
					msg.getProperties().putProp(proprertyName, (Boolean) propertyValue);
			}
		}
		
		return msg;

	}

	// PublicationCI Methods

	/**
	 * publication d un message à un topic
	 * 
	 * @param topic
	 * @param msg
	 * @throws Exception
	 */
	public void publishMessage(String topic, Message msg) throws Exception {

		this.logMessage("Publisher publie le message " + msg.getURI());
		this.publicationPOP.publish(msg, topic);

	}

	/**
	 * publication d'un message associé à plusieurs topics
	 * 
	 * @param msg
	 * @param topics
	 * @throws Exception
	 */
	public void publishMessageWithManyTopics(Message msg, String[] topics) throws Exception {

		this.logMessage("Publisher publie le message " + msg.getURI() + " correspondant à plusieurs topics");
		this.publicationPOP.publish(msg, topics);

	}

	/**
	 * publication de plusieurs messages associé à un seul topic
	 * 
	 * @param msg
	 * @param topic
	 * @throws Exception
	 */
	public void publishMessages(Message[] msg, String topic) throws Exception {

		this.logMessage("Publisher fait publish messages");

		this.publicationPOP.publish(msg, topic);

	}

	/**
	 * publication de plusieurs messages associés à plusieurs topics
	 * 
	 * @param msg
	 * @param topics
	 * @throws Exception
	 */
	public void publishMessagesWithManyTopics(Message[] msg, String[] topics) throws Exception {

		this.logMessage("Publisher fait publish messages with many topics");

		this.publicationPOP.publish(msg, topics);
	}

	// ManagementCI Methods
	/**
	 * creation d'un nouveau topic
	 * 
	 * @param topic
	 * @throws Exception
	 */
	public void createTopic(String topic) throws Exception {

		this.logMessage("Publisher crée le topic " + topic);
		this.managementPOP.createTopic(topic);
	}

	/**
	 * création de plusieurs topics
	 * 
	 * @throws Exception
	 */
	public void createTopics() throws Exception {
		String[] topics = new String[2];
		topics[0] = "Danses";
		topics[1] = "Chansons";

		this.logMessage("Publisher crée plusieurs topics");
		this.managementPOP.createTopics(topics);
	}

	/**
	 * distruction d'un topic
	 * 
	 * @param topic
	 * @throws Exception
	 */
	public void destroyTopic(String topic) throws Exception {
		this.logMessage("Publisher détruit le topic " + topic);

		this.managementPOP.destroyTopics(topic);
	}

	/**
	 * test si le nom en parametre est réelement un topic
	 * 
	 * @param topic
	 * @return
	 * @throws Exception
	 */
	public boolean isTopic(String topic) throws Exception {
		return this.managementPOP.isTopic(topic);
	}

	/**
	 *
	 * @return une liste de tous les topics
	 * @throws Exception
	 */
	public String[] getTopics() throws Exception {
		return this.managementPOP.getTopics();
	}

	/**
	 *
	 * @return l uri du port entrant du service publication
	 * @throws Exception
	 */
	public String getPublicationUri() throws Exception {
		return this.managementPOP.getPublicationPortURI();
	}

}
