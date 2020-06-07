package components;

import connectors.ManagementCIConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import interfaces.ManagementCI;
import interfaces.MessageFilterI;
import interfaces.MessageI;
import interfaces.ReceptionCI;
import interfaces.ReceptionImplementationI;
import ports.ManagementCISubscriberOutboundPort;
import ports.ReceptionCISubscriberInboundPort;
import testsIntegration.SenariosSubscriber;
import versionMultiJvm.broker_repartition.testsPerformance.TestsPerform;

public class Subscriber 
extends AbstractComponent 
implements ReceptionImplementationI {

	protected MessageI message;

	/** Port sortant utilisé pour appeler le service management */
	protected ManagementCISubscriberOutboundPort managementSubscriberOP;
	/** Port sortant utilisé pour appeler le service reception */
	protected ReceptionCISubscriberInboundPort receptionSubscriberIP;

	/** Uri du port entrant du service management */
	protected String managementBIPURI;
	/**
	 * uri pour distinguer entre les souscripteurs (utilisé lors du lancement des
	 * senarios)
	 */
	protected String subscriberUri;

	
	/**Tests d'integrations*/
	protected SenariosSubscriber ss;
	
	
	/**Tests de performance*/	
	protected TestsPerform tp;
	protected boolean modeTest;

	/**
	 *
	 * @param managementBIPURI Uri du port entrant du service management
	 * @param uri              uri pour distinguer entre les publiers
	 * @throws Exception
	 */
	protected Subscriber(String managementBIPURI, String uri, boolean modeTest)

		throws Exception {

		super(20, 0);

		assert managementBIPURI != null : new PreconditionException(
				"Suscriber : Broker's management port can't be null");

		this.managementBIPURI = managementBIPURI;
		this.subscriberUri = uri;

		// Ajout des interfaces, Création des ports, Publication des ports

		this.addRequiredInterface(ManagementCI.class);
		this.managementSubscriberOP = new ManagementCISubscriberOutboundPort(this);
		this.managementSubscriberOP.publishPort();

		this.addOfferedInterface(ReceptionCI.class);
		this.receptionSubscriberIP = new ReceptionCISubscriberInboundPort(this);
		this.receptionSubscriberIP.publishPort();


		// Affichage de la console
		this.tracer.setTitle("subscriber" + uri);
		this.tracer.setRelativePosition(Integer.parseInt(uri), 2);
		this.toggleTracing();
		
		
		//initialisation de tests senarios
		this.ss = new SenariosSubscriber(this);
		
		//tests de performance
		this.modeTest = modeTest;
		if(modeTest)
			this.tp = new TestsPerform();
	
	}

	// ------------------------------------------------------------------------
	// Component life-cycle
	// ------------------------------------------------------------------------

	@Override
	public void start() throws ComponentStartException {
		super.start();
		this.logMessage("starting subscriber component.");
		
		try {

			this.doPortConnection(this.managementSubscriberOP.getPortURI(), this.managementBIPURI,
					ManagementCIConnector.class.getCanonicalName());

		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
		
	}
	
	@Override
	public void execute() throws Exception {
		super.execute();
		this.logMessage("starting suscriber component.");

		try {

			// Tests d'intégration / Différents Senarios du Publier
			switch (this.subscriberUri) {
			case "0":
				this.ss.senario_Four(20);
//				this.ss.senario_One();
				break;
			case "1":
				this.ss.senario_Four(20);
//				this.ss.senario_Two();
				break;
			case "2":	
				this.ss.senario_Four(20);
//				this.ss.senario_Three();
				break;
			default:
				this.ss.senario_Four(20);
//				this.ss.senario_One();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void finalise() throws Exception {

		
		
		
		this.logMessage("stopping subscriber component.");
		this.printExecutionLogOnFile("subscriber");

		this.managementSubscriberOP.doDisconnection();

		super.finalise();
	}

	@Override
	public void shutdown() throws ComponentShutdownException {

		if(modeTest)
			tp.writeTimes(this.subscriberUri);
		
		try {
			this.managementSubscriberOP.unpublishPort();
			this.receptionSubscriberIP.unpublishPort();

		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	// ------------------------------------------------------------------------
	// Component usefullMethodes
	// ------------------------------------------------------------------------

	// ReceptionImplementationI Methods

	/**
	 * methode appelé depuis le broker pour la reception de message
	 * 
	 * @param m
	 * @throws Exception
	 */
	@Override
	public void acceptMessage(MessageI m) throws Exception {
		//calculs du temps d'acheminement d'un message
		if(modeTest)
			tp.calculTimes(m);

		this.logMessage("Subscriber a reçu " + m.getURI());
		this.message = m;
		
		
	}

	// ReceptionCI Methods
	/**
	 * methode appelé depuis le broker pour la reception de messages
	 * 
	 * @param ms
	 * @throws Exception
	 */
	@Override
	public void acceptMessage(MessageI[] ms) throws Exception {
		//calculs du temps d'acheminement d'un message
		if(modeTest)
			for(int i = 0 ; i< ms.length; i++) {
				tp.calculTimes(ms[i]);
			}		
		
		this.logMessage("Subscriber a reçu des messages dont " + ms[0].getURI());
	}

	// Management call Methods

	public void createTopic(String topic) throws Exception {
		this.logMessage("Subscriber crée le topic " + topic);
		this.managementSubscriberOP.createTopic(topic);
	}

	public void createTopics(String[] topics) throws Exception {
		this.logMessage("Subscriber crée plusieurs topics ");
		this.managementSubscriberOP.createTopics(topics);
	}

	public void destroyTopic(String topic) throws Exception {
		this.logMessage("Subscriber détruit le topic " + topic);
		this.managementSubscriberOP.destroyTopics(topic);
	}

	public boolean isTopic(String topic) throws Exception {
		return this.managementSubscriberOP.isTopic(topic);
	}

	public String[] getTopics() throws Exception {
		return this.managementSubscriberOP.getTopics();
	}

	public String getPublicationPortURI() throws Exception {
		return this.managementSubscriberOP.getPublicationPortURI();
	}

	// Subscription Methods
	/**
	 * souscription à un topic
	 * 
	 * @param topic
	 * @throws Exception
	 */
	public void subscribeToTopic(String topic) throws Exception {
		this.logMessage("Subscriber fait subscribe au topic " + topic);
		
		this.managementSubscriberOP.subscribe(topic, this.receptionSubscriberIP.getPortURI());

	}

	/**
	 * souscription à une liste de topics
	 * 
	 * @param topics
	 * @throws Exception
	 */
	public void subscribeToTopics(String[] topics) throws Exception {
		this.logMessage("Subscriber fait subscribe à plusieurs topics");

		this.managementSubscriberOP.subscribe(topics, this.receptionSubscriberIP.getPortURI());
	}

	/**
	 * souscription à un topic avec un filtre
	 * 
	 * @param topic
	 * @param filter
	 * @throws Exception
	 */
	public void subscribeWithFilter(String topic, MessageFilterI filter) throws Exception {
		this.logMessage("Subscriber fait subscribe with filter au topic " + topic);

		this.managementSubscriberOP.subscribe(topic, filter, this.receptionSubscriberIP.getPortURI());
	}

	/**
	 * modification d'un filtre
	 * 
	 * @param topic
	 * @param newFilter
	 * @throws Exception
	 */
	public void modifyFilter(String topic, MessageFilterI newFilter) throws Exception {

		this.managementSubscriberOP.modifyFilter(topic, newFilter, this.receptionSubscriberIP.getPortURI());
	}

	/**
	 * se désabonner d'un topic
	 * 
	 * @param topic
	 * @throws Exception
	 */
	public void unsubscribe(String topic) throws Exception {
		this.logMessage("Subscriber se désouscrit du topic " + topic);

		this.managementSubscriberOP.unsubscribe(topic, this.receptionSubscriberIP.getPortURI());

	}

}
