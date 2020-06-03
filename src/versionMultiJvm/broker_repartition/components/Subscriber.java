package versionMultiJvm.broker_repartition.components;

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
import utiles.MessageFilter;

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

	/**
	 *
	 * @param managementBIPURI Uri du port entrant du service management
	 * @param uri              uri pour distinguer entre les publiers
	 * @throws Exception
	 */
	protected Subscriber(String managementBIPURI, String uri)

			throws Exception {

		super(1, 1);

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
				senario_One();
				break;
			case "1":
				senario_Two();
				break;
			case "2":
				senario_Three();
				break;
			default:
				senario_One();
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

	/**
	 * senario 1: un souscripteur souscrit à un topic, il crée un topic, crée
	 * plusieurs topic , appelle à la distruction d'un topic
	 * 
	 * @throws Exception
	 */
	public void senario_One() throws Exception {
		// Souscrire au topic Danses
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Subscriber) this.getTaskOwner()).subscribeToTopic("Danses");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Subscriber) this.getTaskOwner()).createTopic("Animaux");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Créer plusieurs topics
		String[] topics = new String[2];
		topics[0] = "Villes";
		topics[1] = "Finances";

		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Subscriber) this.getTaskOwner()).createTopics(topics);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Détruire un topic
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Subscriber) this.getTaskOwner()).destroyTopic("Villes");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

	}

	/**
	 * senario 2: un souscripteur souscrit à un topic, crée plusieurs topic, se
	 * désabonne d'un topic
	 * 
	 * @throws Exception
	 */
	public void senario_Two() throws Exception {
		// Souscription à un topic
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Subscriber) this.getTaskOwner()).subscribeToTopic("Maladies");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Création d'un tableau de topics pour souscrire à plusieurs sujets
		String[] topics = new String[2];
		topics[0] = "Arts";
		topics[1] = "Voyages";

		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Subscriber) this.getTaskOwner()).subscribeToTopics(topics);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Subscriber) this.getTaskOwner()).unsubscribe("Arts");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	/**
	 * senario 3: un souscripteur souscrit à un topic, souscrit à un topic avec un
	 * filtre.
	 * 
	 * @throws Exception
	 */
	public void senario_Three() throws Exception {
		// Souscrir à un topic sans filtre
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Subscriber) this.getTaskOwner()).subscribeToTopic("Salutations");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Initialiser un filtre
		MessageFilterI f = new MessageFilterI() {
			
			@Override
			public boolean filter(MessageI m) throws Exception {
				//on veut garder les messages avec une longueur < 100 
				Integer lenM = m.getProperties().getIntProp("lenM");
				if(lenM != null && lenM > 200) {
					return false;
				}
				return true;
			}
		};
	
		// Souscrire à un topic avec filtre
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Subscriber) this.getTaskOwner()).subscribeWithFilter("Nourriture", f);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Initialiser un filtre
		MessageFilter f2 = new MessageFilter(2, null, null, null);

		// Souscrire à un topic avec filtre
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Subscriber) this.getTaskOwner()).subscribeWithFilter("Pays", f2);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

	}

	// ReceptionImplementationI Methods

	/**
	 * methode appelé depuis le broker pour la reception de message
	 * 
	 * @param m
	 * @throws Exception
	 */
	@Override
	public void acceptMessage(MessageI m) throws Exception {
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
