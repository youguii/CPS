package versionMultiJvm.broker_repartition.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import connectors.ReceptionCIConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import interfaces.ManagementCI;
import interfaces.ManagementImplementationI;
import interfaces.MessageFilterI;
import interfaces.MessageI;
import interfaces.PublicationCI;
import interfaces.PublicationsImplementationI;
import interfaces.ReceptionCI;
import interfaces.SubscriptionImplementationI;
import ports.ManagementCIBrokerInboundPort;
import ports.PublicationCIBrokerInboundPort;
import ports.ReceptionCIBrokerOutboundPort;
import utiles.MessageFilter;
import versionMultiJvm.broker_repartition.connectors.DistributionConnector;
import versionMultiJvm.broker_repartition.interfaces.DistributionCI;
import versionMultiJvm.broker_repartition.ports.DistributionInboundPort;
import versionMultiJvm.broker_repartition.ports.DistributionOutboundPort;
import versionMultiJvm.broker_repartition.testsPerformance.TestsPerform;

public class Broker extends AbstractComponent
		implements ManagementImplementationI, SubscriptionImplementationI, PublicationsImplementationI,
		DistributionCI{

	/** Topic associé à Uri du sbscriber + messageFilter */
	protected HashMap<String, HashMap<String, MessageFilterI>> subscribersForEachTopic;
	/** URI du subscriber associé au port connecté vers lui */
	protected HashMap<String, ReceptionCIBrokerOutboundPort> portForEachSubscriber;
	/** Maps qui contienent les messages */
	protected HashMap<MessageI, HashMap<String, MessageFilterI>> msgToSubscribers;
	protected HashMap<MessageI[], HashMap<String, MessageFilterI>> msgsToSubscribers;
	

	/** les locks utilisé pour la synchroniation */
	protected final ReentrantReadWriteLock sub4TopicLock;
	protected final ReentrantLock publishMethodsStructureLock;
	protected final ReentrantLock publishMethodsStructure2Lock;
	
	protected final ReentrantReadWriteLock portForEachSubscriberLock;

	/** les conditions utilisé avec les locks */
	final Condition clock1;
	final Condition clock2;

	/** uri du pool utilisé pour management */
	public static final String MANAGEMENT_POOL_URI = "ManPoolUri";
	/** uri du pool utilisé pour publication */
	public static final String PUBLICATION_POOL_URI = "PubPoolUri";

	/** Port entrant du service management */
	protected ManagementCIBrokerInboundPort managementBIP;
	/** Port entrant du service publication */
	protected PublicationCIBrokerInboundPort publicationBIP;

	/** Uri du port entrant du service management */
	protected String managementBIPURI;
	/** Uri du port entrant du service reception */
	protected String receptionSIPURI;
	
	
	protected DistributionInboundPort broker_distrib_ip;
	protected String [] brokers_inbound_port_uris;
	protected String distributionBIPURI;
	
	protected ArrayList<DistributionOutboundPort> brokerDistribOP;
	
	
	//Tests de performances 	
	protected TestsPerform tp;
	protected String brokerUri;
	public static boolean fini;
	protected boolean modeTest;
	
	/**
	 *
	 * @param managementBIPURI Uri du port entrant du service management
	 * @throws Exception
	 */
	protected Broker(
			String managementBIPURI,
			String [] brokers_inbound_port_uris,
			String distributionBIPURI,
			String brokerUri,
			boolean modeTest
			
	) throws Exception {
		super(10, 0);

		assert managementBIPURI != null : new PreconditionException("Broker : Broker's management port can't be null");

		
		this.managementBIPURI = managementBIPURI;
		

		this.subscribersForEachTopic = new HashMap<>();
		this.portForEachSubscriber = new HashMap<>();
		this.msgToSubscribers = new HashMap<>();

		this.msgsToSubscribers = new HashMap<>(); 

		// Initialisation du Lock
		this.sub4TopicLock = new ReentrantReadWriteLock();
		this.publishMethodsStructureLock = new ReentrantLock(); 
		this.publishMethodsStructure2Lock = new ReentrantLock(); 

		this.portForEachSubscriberLock= new ReentrantReadWriteLock();

		this.clock1 = publishMethodsStructureLock.newCondition();
		this.clock2 = publishMethodsStructure2Lock.newCondition();

		// Création des pools de threads
		this.createNewExecutorService(PUBLICATION_POOL_URI, 10, false); // Publication

		this.createNewExecutorService(MANAGEMENT_POOL_URI, 10, false); // Management

		// Ajout des interfaces, Creation et Publication des ports
		this.addOfferedInterface(ManagementCI.class);
		this.managementBIP = new ManagementCIBrokerInboundPort(managementBIPURI, this,
				this.getExecutorServiceIndex(MANAGEMENT_POOL_URI));
		this.managementBIP.publishPort() ;

		this.addOfferedInterface(PublicationCI.class);
		this.publicationBIP = new PublicationCIBrokerInboundPort(this,
				this.getExecutorServiceIndex(PUBLICATION_POOL_URI));
		this.publicationBIP.publishPort() ;

		this.addRequiredInterface(ReceptionCI.class);
		
		
		
		// Ajout du port pour la connexion avec les autres brokers
		
		this.distributionBIPURI = distributionBIPURI;
		
		this.addRequiredInterface(DistributionCI.class);
		
		this.brokers_inbound_port_uris = brokers_inbound_port_uris;
		this.brokerDistribOP = new ArrayList<DistributionOutboundPort>();
		
		this.addOfferedInterface(DistributionCI.class);
		this.broker_distrib_ip = new DistributionInboundPort(distributionBIPURI, this) ;
		this.broker_distrib_ip.publishPort() ;
		

		// Affichage de la console
		this.tracer.setTitle("Broker");
		this.tracer.setRelativePosition(1, 0);
		this.toggleTracing();
		
		
		
		//Tests de performance
		this.modeTest= modeTest;
		if(this.modeTest){
			tp = new TestsPerform();
			this.brokerUri = brokerUri;
			fini = false;
		}
	

	}

//-------------------------------------------------------------------------
// Component life-cycle
//-------------------------------------------------------------------------

	@Override
	public void start() throws ComponentStartException {
		super.start();
		this.logMessage("starting broker component.");

		try {
			// Connexion avec les autres brokers
			for(int i=0; i < brokers_inbound_port_uris.length ;i++) {
				if(!(brokers_inbound_port_uris[i].equals(distributionBIPURI))) {
					brokerDistribOP.add(new DistributionOutboundPort(this)) ;
				}
			}
			for(int i=0; i < brokerDistribOP.size();i++) {

				brokerDistribOP.get(i).publishPort();
				
				this.doPortConnection(
						(brokerDistribOP.get(i)).getPortURI(),
						brokers_inbound_port_uris[i],
						DistributionConnector.class.getCanonicalName()) ;
			}

					
			for(int i = 0; i < 3 ; i++) {
				this.runTask(new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							//Thread.sleep(1000);
							((Broker) this.getTaskOwner()).sendMessageToSubscriber();
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				});
	
				this.runTask(new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							//Thread.sleep(1000);
							((Broker) this.getTaskOwner()).sendMessagesToSubscriber();
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				});
			}
			
			
			//Tests de performances
			if(this.modeTest){
				new Thread(){
					@Override
					public void run(){
						tp.nbMessages_en_Attente(msgToSubscribers , publishMethodsStructureLock);
					}
					
				}.start();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void finalise() throws Exception {
		this.logMessage("stopping broker component.");
		this.printExecutionLogOnFile("broker");

		
		
		// Déconnexion des ports connectés
		
		portForEachSubscriberLock.readLock().lock();
		for(DistributionOutboundPort p : brokerDistribOP) {
			p.doDisconnection();
		}
		portForEachSubscriberLock.readLock().unlock();

		for (ReceptionCIBrokerOutboundPort p : portForEachSubscriber.values()) {
			p.doDisconnection();
		}
		super.finalise();
	}

	@Override
	public void shutdown() throws ComponentShutdownException {
		
		//Tests de performances
		if(this.modeTest){
			fini = true;
			tp.calcul_Average_nbMessage(msgToSubscribers , publishMethodsStructureLock, this.brokerUri);
		}
		
		try {
			// Dépublication des ports
			this.managementBIP.unpublishPort();
			this.publicationBIP.unpublishPort();
			
			for(DistributionOutboundPort p : brokerDistribOP) {
				p.unpublishPort();;
			}
			
			broker_distrib_ip.unpublishPort();

			portForEachSubscriberLock.readLock().lock();
			for (ReceptionCIBrokerOutboundPort p : portForEachSubscriber.values()) {
				p.unpublishPort();
			}			
			portForEachSubscriberLock.readLock().unlock();


		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

		
		

	
	// -------------------------------------------------------------------------
	// Component usefullMethodes
	// -------------------------------------------------------------------------

	// PublicationCI Methods

	@Override
	public void publish(MessageI m, String topic) throws Exception {
		this.logMessage("Broker, a recu un message de publisher");

		HashMap<String, MessageFilterI> map = new HashMap<>();
		
		Serializable [] parameters = {m, topic, this.getPublicationPortURI()};
		

		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					for(DistributionOutboundPort p : brokerDistribOP) {
						p.distribute(parameters);
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		
		
		
		/*
		 * S'il y a des abonnés au topic: On les récupère dans une map intermediaire
		 * pour les associer au message Si non si le topic n'existe pas on le crée
		 */
		sub4TopicLock.readLock().lock();
		try {

			if (subscribersForEachTopic.containsKey(topic))
				map.putAll(subscribersForEachTopic.get(topic));
			else {
				subscribersForEachTopic.put(topic, new HashMap<>());
			}
		} finally {
			sub4TopicLock.readLock().unlock();
		}

		// On associe les abonnés au message
		if (!map.isEmpty()) {
			publishMethodsStructureLock.lock();
			try {

				msgToSubscribers.put(m, map);

				clock1.signal();

			} finally {
				publishMethodsStructureLock.unlock();
			}
		}


	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		this.logMessage("Broker, a recu un message à plusieurs topics");

		HashMap<String, MessageFilterI> map = new HashMap<>();
		
		Serializable [] parameters = {m, topics, this.getPublicationPortURI()};

		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					for(DistributionOutboundPort p : brokerDistribOP) {
						p.distribute(parameters);
					}				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		
		
		/*
		 * Pour chauqe topic, s'il y a des abonnés au topic: On les récupère dans une
		 * map intermediaire pour les associer au message Si non si le topic n'existe
		 * pas on le crée
		 */
		sub4TopicLock.readLock().lock();
		try {
			for (int i = 0; i < topics.length; i++) {
				// Récupérer les abonnés pour chaque topic, les stocker dans la map
				// intermédiaire
				if (subscribersForEachTopic.containsKey(topics[i]))
					map.putAll(subscribersForEachTopic.get(topics[i]));
				else {
					subscribersForEachTopic.put(topics[i], new HashMap<>());
				}
			}
		} finally {
			sub4TopicLock.readLock().unlock();
		}

		if (!map.isEmpty()) {
			publishMethodsStructureLock.lock();
			try {
				// Associer le message aux bons abonnés
				msgToSubscribers.put(m, map);

				clock1.signal();

			} finally {
				publishMethodsStructureLock.unlock();
			}
		}

	}

	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		this.logMessage("Broker, a recu plusieurs messages");

		// Map va contenir les abonnés à topic
		HashMap<String, MessageFilterI> map = new HashMap<>();

		Serializable  [] parameters = {ms, topic, this.getPublicationPortURI()};

		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					for(DistributionOutboundPort p : brokerDistribOP) {
						p.distribute(parameters);
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		
		/*
		 * S'il y a des abonnés au topic: On les récupère dans une map intermediaire
		 * pour les associer au message Si non si le topic n'existe pas on le crée
		 */
		sub4TopicLock.readLock().lock();
		try {
			if (subscribersForEachTopic.containsKey(topic))
				map.putAll(subscribersForEachTopic.get(topic));
			else {
				subscribersForEachTopic.put(topic, new HashMap<>());
			}
		} finally {
			sub4TopicLock.readLock().unlock();
		}

		// On associe les abonnés aux messages
		if (!map.isEmpty()) {
			publishMethodsStructure2Lock.lock();
			try {

				msgsToSubscribers.put(ms, map);

				clock2.signal();

			} finally {
				publishMethodsStructure2Lock.unlock();
			}
		}

	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {

		this.logMessage("Broker, a recu plusieurs messages de plusieurs topics");

		HashMap<String, MessageFilterI> map = new HashMap<>();

		Serializable [] parameters = {ms, topics, this.getPublicationPortURI()};

		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					for(DistributionOutboundPort p : brokerDistribOP) {
						p.distribute(parameters);
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		
		
		/*
		 * Pour chaque topic, s'il y a des abonnés au topic: On les récupère dans une
		 * map intermediaire pour les associer au message Si non si le topic n'existe
		 * pas on le crée
		 */
		sub4TopicLock.readLock().lock();
		try {
			for (int i = 0; i < topics.length; i++) {
				// Récupérer les abonnés pour chaque topic, les stocker dans la map
				// intermédiaire
				if (subscribersForEachTopic.containsKey(topics[i]))
					map.putAll(subscribersForEachTopic.get(topics[i]));
				else {
					subscribersForEachTopic.put(topics[i], new HashMap<>());
				}
			}
		} finally {
			sub4TopicLock.readLock().unlock();
		}

		// Associer les messages aux bons abonnés
		if (!map.isEmpty()) {
			publishMethodsStructure2Lock.lock();
			try {
				msgsToSubscribers.put(ms, map);

				clock2.signal();

			} finally {
				publishMethodsStructure2Lock.unlock();
			}
		}

	}

	// ReceptionCI Methods
	/**
	 * methode qui fait appel au bon port du subscriber pour lui transmettre un
	 * message
	 * 
	 * @param m message
	 * @throws Exception
	 */
	public void sendMessageToSubscriber() throws Exception {
		MessageI msg = null;
		HashMap<String, MessageFilterI> subscribers = new HashMap<>();

		while (true) {
			
			publishMethodsStructureLock.lock();
				try {
					while(msgToSubscribers.isEmpty()) { 
						clock1.await();
					}	
					// On récupère un message et ses subcribers depuis msgToSubscribers
					for (Entry<MessageI, HashMap<String, MessageFilterI>> entry : msgToSubscribers.entrySet()) {
	
						msg = entry.getKey();
						subscribers = entry.getValue();
						break;
					}
					
					for (Entry<String, MessageFilterI> entry : subscribers.entrySet()) {
						
						// S'il n'y a pas de filtre, on envoie le message
						if (entry.getValue() == null ) {
							portForEachSubscriberLock.readLock().lock();
							
							this.logMessage("  ");
							this.logMessage("Send 1 : Broker envoie un message à Subscriber "+entry.getKey());

							portForEachSubscriber.get(entry.getKey()).acceptMessage(msg);
							portForEachSubscriberLock.readLock().unlock();
	
						}
						// le filtre est respecté, on envoie le message
						else if( entry.getValue().filter(msg)){
							portForEachSubscriberLock.readLock().lock();

							this.logMessage("  ");
							this.logMessage("Send 1 : Broker envoie un message à Subscriber "+entry.getKey());
							
							portForEachSubscriber.get(entry.getKey()).acceptMessage(msg);
							portForEachSubscriberLock.readLock().unlock();
						}
					}
					msgToSubscribers.remove(msg);
	
				} finally {
					publishMethodsStructureLock.unlock();
				}

		}

	}

	/**
	 * methode qui fait appel au bon port du subscriber pour lui transmettre une
	 * liste de messages
	 * 
	 * @param ms
	 * @throws Exception
	 */
	public void sendMessagesToSubscriber() throws Exception {

		MessageI[] msgs = null;
		HashMap<String, MessageFilterI> subscribers = new HashMap<>();

		while (true) {
			
			publishMethodsStructure2Lock.lock();				
			try {
				while(msgsToSubscribers.isEmpty()) {
					clock2.await();
				}
					// On récupère un message et ses subcribers depuis msgToSubscribers
					for (Map.Entry<MessageI[], HashMap<String, MessageFilterI>> entry : msgsToSubscribers.entrySet()) {
						msgs = entry.getKey();
						subscribers = entry.getValue();
						
						break;
					}

					for (Entry<String, MessageFilterI> entry : subscribers.entrySet()) {
						boolean res = true;
						if (entry.getValue() != null)

							// Teste la présence du filtre
							for (MessageI m : msgs) {
								if (!entry.getValue().filter(m)) {
									// Teste si le filtre n'est pas respecté
									res = false;
									break;
								}
							}
						if (res) {
							portForEachSubscriberLock.readLock().lock();
							this.logMessage("  ");
							this.logMessage("Send 2 : Broker envoie plusieurs messages à Subscriber "+entry.getKey());

							portForEachSubscriber.get(entry.getKey()).acceptMessage(msgs);
							portForEachSubscriberLock.readLock().unlock();

						}
					}
					msgsToSubscribers.remove(msgs);
				
			} finally {
				publishMethodsStructure2Lock.unlock();
			}
		}

	}

	// ManagementCI Methods


	@Override
	public void createTopic(String topic) throws Exception {
		this.logMessage("Broker, topic crée " + topic);

		this.sub4TopicLock.writeLock().lock();
		try {
			if (!subscribersForEachTopic.containsKey(topic))
				subscribersForEachTopic.put(topic, new HashMap<>());
		} finally {
			this.sub4TopicLock.writeLock().unlock();
		}
	}


	@Override
	public void createTopics(String[] topics) throws Exception {
		this.logMessage("Broker, topics crées");

		this.sub4TopicLock.writeLock().lock();
		try {
			for (int i = 0; i < topics.length; i++) {
				if (!subscribersForEachTopic.containsKey(topics[i]))
					subscribersForEachTopic.put(topics[i], new HashMap<>());
			}
		} finally {
			this.sub4TopicLock.writeLock().unlock();
		}
	}


	@Override
	public void destroyTopics(String topic) throws Exception {
		this.logMessage("Broker, topic détruit " + topic);

		this.sub4TopicLock.writeLock().lock();
		try {
			if (subscribersForEachTopic.containsKey(topic)) {
				subscribersForEachTopic.remove(topic);
			}
			assert (!subscribersForEachTopic.containsKey(topic));
		} finally {
			this.sub4TopicLock.writeLock().unlock();
		}
	}

	@Override
	public boolean isTopic(String topic) throws Exception {
		boolean res = false;

		this.sub4TopicLock.readLock().lock();
		try {
			res = subscribersForEachTopic.containsKey(topic);
		} finally {
			this.sub4TopicLock.readLock().unlock();
		}
		return res;
	}

	@Override
	public String[] getTopics() throws Exception {
		String[] res;

		this.sub4TopicLock.readLock().lock();
		try {
			Object[] result = (subscribersForEachTopic.keySet()).toArray();

			res = new String[result.length];
			for (int i = 0; i < res.length; i++) {
				res[i] = (String) result[i];
			}
		} finally {
			this.sub4TopicLock.readLock().unlock();
		}

		return res;
	}

	@Override
	public String getPublicationPortURI() throws Exception {
		this.logMessage("Broker a envoyé son port Publication");

		return this.publicationBIP.getPortURI();
	}

	@Override
	public void subscribe(String topic, String inboundPortURI) throws Exception {
		this.sub4TopicLock.writeLock().lock();

		try {
			HashMap<String, MessageFilterI> uris = subscribersForEachTopic.get(topic);
			if (uris != null)
				uris.put(inboundPortURI, null);
			else {
				uris = new HashMap<>();
				uris.put(inboundPortURI, null);
			}
			subscribersForEachTopic.put(topic, uris);

			this.logMessage("Broker, Le souscriber " + inboundPortURI + " a souscrit a " + topic);
		} finally {
			this.sub4TopicLock.writeLock().unlock();
		}

		portForEachSubscriberLock.writeLock().lock();

		if(!portForEachSubscriber.containsKey(inboundPortURI)) {
			ReceptionCIBrokerOutboundPort receptionBOP = new ReceptionCIBrokerOutboundPort(this);
			receptionBOP.publishPort() ;

			portForEachSubscriber.put(inboundPortURI, receptionBOP);
			
		this.doPortConnection(receptionBOP.getPortURI(), inboundPortURI, ReceptionCIConnector.class.getCanonicalName());
		}
		portForEachSubscriberLock.writeLock().unlock();

		this.logMessage("Broker, connexion établie");
	}

	@Override
	public void subscribe(String[] topics, String inboundPortURI) throws Exception {
		this.sub4TopicLock.writeLock().lock();

		try {
			for (int i = 0; i < topics.length; i++) {
				HashMap<String, MessageFilterI> uris = subscribersForEachTopic.get(topics[i]);
				if (uris != null)
					uris.put(inboundPortURI, null);
				else {
					uris = new HashMap<>();
					uris.put(inboundPortURI, null);
				}
				subscribersForEachTopic.put(topics[i], uris);
			}
		} finally {
			this.sub4TopicLock.writeLock().unlock();
		}

		this.logMessage("Broker, un Subscriber a souscrit à plusieurs topics");

		
		if(!portForEachSubscriber.containsKey(inboundPortURI)) {
			ReceptionCIBrokerOutboundPort receptionBOP = new ReceptionCIBrokerOutboundPort(this);
			receptionBOP.publishPort() ;

			portForEachSubscriber.put(inboundPortURI, receptionBOP);
			
		this.doPortConnection(receptionBOP.getPortURI(), inboundPortURI, ReceptionCIConnector.class.getCanonicalName());
		}
		portForEachSubscriberLock.writeLock().unlock();

	}

	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception {
		this.sub4TopicLock.writeLock().lock();

		try {
			HashMap<String, MessageFilterI> uris = subscribersForEachTopic.get(topic);
			if (uris != null)
				uris.put(inboundPortURI, (MessageFilter) filter);
			else {
				uris = new HashMap<>();
				uris.put(inboundPortURI, (MessageFilter) filter);
			}
			subscribersForEachTopic.put(topic, uris);
		} finally {
			this.sub4TopicLock.writeLock().unlock();
		}

		
		if(!portForEachSubscriber.containsKey(inboundPortURI)) {
			ReceptionCIBrokerOutboundPort receptionBOP = new ReceptionCIBrokerOutboundPort(this);
			receptionBOP.publishPort() ;

			portForEachSubscriber.put(inboundPortURI, receptionBOP);
			
		this.doPortConnection(receptionBOP.getPortURI(), inboundPortURI, ReceptionCIConnector.class.getCanonicalName());
		}
		portForEachSubscriberLock.writeLock().unlock();

	}

	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception {
		this.sub4TopicLock.writeLock().lock();

		try {
			HashMap<String, MessageFilterI> uris = subscribersForEachTopic.get(topic);
			if (uris != null)
				uris.put(inboundPortURI, newFilter);
			else {
				uris = new HashMap<>();
				uris.put(inboundPortURI, newFilter);
			}
			subscribersForEachTopic.put(topic, uris);
		} finally {
			this.sub4TopicLock.writeLock().unlock();
		}

		
		if(!portForEachSubscriber.containsKey(inboundPortURI)) {
			ReceptionCIBrokerOutboundPort receptionBOP = new ReceptionCIBrokerOutboundPort(this);
			receptionBOP.publishPort() ;

			portForEachSubscriber.put(inboundPortURI, receptionBOP);
			
		this.doPortConnection(receptionBOP.getPortURI(), inboundPortURI, ReceptionCIConnector.class.getCanonicalName());
		}
		portForEachSubscriberLock.writeLock().unlock();

	}


	@Override
	public void unsubscribe(String topic, String inboundPortURI) throws Exception {
		this.logMessage("Broker, un Subscriber a désouscrit");

		this.sub4TopicLock.writeLock().lock();
		try {
			HashMap<String, MessageFilterI> uris = subscribersForEachTopic.get(topic);
			uris.remove(inboundPortURI);
			subscribersForEachTopic.put(topic, uris);
		} finally {
			this.sub4TopicLock.writeLock().unlock();
		}
		
		if(!portForEachSubscriber.containsKey(inboundPortURI)) {
			ReceptionCIBrokerOutboundPort receptionBOP = new ReceptionCIBrokerOutboundPort(this);
			receptionBOP.publishPort() ;

			portForEachSubscriber.put(inboundPortURI, receptionBOP);

		this.doPortConnection(receptionBOP.getPortURI(), inboundPortURI, ReceptionCIConnector.class.getCanonicalName());
		}
		portForEachSubscriberLock.writeLock().unlock();

	}
	
// ##################################### CALL BrokerManager ###############################################

	public void receive(MessageI m, String topic) throws Exception {
		this.logMessage("Broker, a recu un message de publisher");

		HashMap<String, MessageFilterI> map = new HashMap<>();
		
		/*
		 * S'il y a des abonnés au topic: On les récupère dans une map intermediaire
		 * pour les associer au message Si non si le topic n'existe pas on le crée
		 */
		sub4TopicLock.readLock().lock();
		try {

			if (subscribersForEachTopic.containsKey(topic))
				map.putAll(subscribersForEachTopic.get(topic));
			else {
				subscribersForEachTopic.put(topic, new HashMap<>());
			}
		} finally {
			sub4TopicLock.readLock().unlock();
		}

		// On associe les abonnés au message
		if (!map.isEmpty()) {
			publishMethodsStructureLock.lock();
			try {

				msgToSubscribers.put(m, map);

				clock1.signal();

			} finally {
				publishMethodsStructureLock.unlock();
			}
		}


	}

	public void receive(MessageI m, String[] topics) throws Exception {
		this.logMessage("Broker, a recu un message à plusieurs topics");

		HashMap<String, MessageFilterI> map = new HashMap<>();
		

		/*
		 * Pour chauqe topic, s'il y a des abonnés au topic: On les récupère dans une
		 * map intermediaire pour les associer au message Si non si le topic n'existe
		 * pas on le crée
		 */
		sub4TopicLock.readLock().lock();
		try {
			for (int i = 0; i < topics.length; i++) {
				// Récupérer les abonnés pour chaque topic, les stocker dans la map
				// intermédiaire
				if (subscribersForEachTopic.containsKey(topics[i]))
					map.putAll(subscribersForEachTopic.get(topics[i]));
				else {
					subscribersForEachTopic.put(topics[i], new HashMap<>());
				}
			}
		} finally {
			sub4TopicLock.readLock().unlock();
		}

		if (!map.isEmpty()) {
			publishMethodsStructureLock.lock();
			try {
				// Associer le message aux bons abonnés
				msgToSubscribers.put(m, map);

				clock1.signal();

			} finally {
				publishMethodsStructureLock.unlock();
			}
		}

	}

	public void receive(MessageI[] ms, String topic) throws Exception {
		this.logMessage("Broker, a recu plusieurs messages");

		// Map va contenir les abonnés à topic
		HashMap<String, MessageFilterI> map = new HashMap<>();
		
		/*
		 * S'il y a des abonnés au topic: On les récupère dans une map intermediaire
		 * pour les associer au message Si non si le topic n'existe pas on le crée
		 */
		sub4TopicLock.readLock().lock();
		try {
			if (subscribersForEachTopic.containsKey(topic))
				map.putAll(subscribersForEachTopic.get(topic));
			else {
				subscribersForEachTopic.put(topic, new HashMap<>());
			}
		} finally {
			sub4TopicLock.readLock().unlock();
		}

		// On associe les abonnés aux messages
		if (!map.isEmpty()) {
			publishMethodsStructure2Lock.lock();
			try {

				msgsToSubscribers.put(ms, map);

				clock2.signal();

			} finally {
				publishMethodsStructure2Lock.unlock();
			}
		}

	}

	public void receive(MessageI[] ms, String[] topics) throws Exception {

		this.logMessage("Broker, a recu plusieurs messages de plusieurs topics");

		HashMap<String, MessageFilterI> map = new HashMap<>();
		
		/*
		 * Pour chaque topic, s'il y a des abonnés au topic: On les récupère dans une
		 * map intermediaire pour les associer au message Si non si le topic n'existe
		 * pas on le crée
		 */
		sub4TopicLock.readLock().lock();
		try {
			for (int i = 0; i < topics.length; i++) {
				// Récupérer les abonnés pour chaque topic, les stocker dans la map
				// intermédiaire
				if (subscribersForEachTopic.containsKey(topics[i]))
					map.putAll(subscribersForEachTopic.get(topics[i]));
				else {
					subscribersForEachTopic.put(topics[i], new HashMap<>());
				}
			}
		} finally {
			sub4TopicLock.readLock().unlock();
		}

		// Associer les messages aux bons abonnés
		if (!map.isEmpty()) {
			publishMethodsStructure2Lock.lock();
			try {
				msgsToSubscribers.put(ms, map);

				clock2.signal();

			} finally {
				publishMethodsStructure2Lock.unlock();
			}
		}


	}

	@Override
	public void distribute(Object... params) throws Exception {
		MessageI m = null;
		MessageI [] ms = null;
		String topic = null;
		String [] topics = null;
		String uriPublication = null;
		
		if(params[2] instanceof String ) {
			uriPublication = (String) params[2];
			if(!(uriPublication.equals(this.getPublicationPortURI()))) {
				if(params[0] instanceof MessageI){
					m = (MessageI) params[0];
					if(params[1] instanceof String){
						topic = (String) params[1];
						receive(m, topic);
					}else{
						topics = (String[]) params[1];
						receive(m, topics);
					}
					
					
				}else if(params[0] instanceof MessageI[]){
					ms = (MessageI []) params[0];
					if(params[1] instanceof String){
						topic = (String) params[1];
						receive(ms, topic);
					}else{
						topics = (String[]) params[1];
						receive(ms, topics);
					}
				}
				
			}
			
		}
	
	}
	
}
