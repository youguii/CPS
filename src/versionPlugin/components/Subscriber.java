package versionPlugin.components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import interfaces.MessageFilterI;
import interfaces.MessageI;
import interfaces.ReceptionImplementationI;
import utiles.MessageFilter;
import utiles.Properties;
import versionPlugin.plugins.SubscriberPlugin;

public class Subscriber
extends AbstractComponent
implements ReceptionImplementationI{
    
    
	protected final static String	MY_PLUGIN_URI = "subscriber-plugin-uri" ;

	protected SubscriberPlugin plugin;

	protected MessageI message;

	
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
    protected Subscriber(String managementBIPURI,
    		String uri)
    
    throws Exception{
    	
        super(1, 0);
        
        assert managementBIPURI != null : 
        new PreconditionException("Suscriber : Broker's management port can't be null");
        
        this.managementBIPURI = managementBIPURI;
        this.subscriberUri = uri;
        
        
        //Affichage de la console
		this.tracer.setTitle("subscriber"+uri) ;
		this.tracer.setRelativePosition(Integer.parseInt(uri), 2) ;
		this.toggleTracing() ;


    }
    
    // ------------------------------------------------------------------------
    // Component life-cycle
    // ------------------------------------------------------------------------

    @Override
    public void        execute() throws Exception
    {
        super.execute();
        this.logMessage("starting suscriber component.") ;
            

        // Install the plug-in.
		this.plugin = new SubscriberPlugin(managementBIPURI, subscriberUri) ;
		plugin.setPluginURI(MY_PLUGIN_URI) ;
		this.installPlugin(plugin) ;

		
            // Différents Senarios du Publier
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
            	default :
            		senario_One(); 	
            }            
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
		MessageFilter f = new MessageFilter(new Properties());
		f.getProperties().putProp("Végétarien", true);

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
		MessageFilter f2 = new MessageFilter(new Properties());
		f2.getProperties().putProp("Européen", false);

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

    	
    
    // ReceptionCI Methods
    @Override
    public void acceptMessage(MessageI m) throws Exception {
        this.logMessage("Subscriber a reçu " + m.getURI());
        this.message = m;	
    	
    }
    
    @Override
    public void acceptMessage(MessageI[] ms) throws Exception {
    	this.logMessage("Subscriber a reçu des messages dont " + ms[0].getURI());
    }
    
    // ManagementCI Methods
    
    public void createTopic(String topic) throws Exception {
    	this.logMessage("Subscriber crée le topic "+topic);
    	this.plugin.createTopic(topic);
    }

    public void createTopics(String[] topics) throws Exception {
    	this.logMessage("Subscriber crée plusieurs topics ");
    	this.plugin.createTopics(topics);
    }

    public void destroyTopic(String topic) throws Exception {
    	this.logMessage("Subscriber détruit le topic "+topic);
    	this.plugin.destroyTopics(topic);
    }

    public boolean isTopic(String topic) throws Exception {
        return this.plugin.isTopic(topic);
    }

    public String[] getTopics() throws Exception {
        return this.plugin.getTopics();
    }
    
    public String getPublicationPortURI() throws Exception {
    	return this.plugin.getPublicationPortURI();
    }
    
    
    // Subscription Methods
    
    public void subscribeToTopic(String topic) throws Exception {
    	this.logMessage("Subscriber fait subscribe 1");
    	
    	this.plugin.subscribe(topic, this.plugin.getReceptionPortURI());
    	
    }

    // passage des topics en parametre
    public void subscribeToTopics(String [] topics) throws Exception {
    	this.logMessage("Subscriber fait subscribe 2");
    	
    	this.plugin.subscribe(topics, this.plugin.getReceptionPortURI());
    }

    public void subscribeWithFilter(String topic, MessageFilterI filter) throws Exception {
    	this.logMessage("Subscriber fait subscribe with filter");
    	
    	this.plugin.subscribe(topic, filter, this.plugin.getReceptionPortURI());
    }

    public void modifyFilter(String topic, MessageFilterI newFilter) throws Exception {
    	
    	this.plugin.modifyFilter(topic, newFilter, this.plugin.getReceptionPortURI());
    }

    public void unsubscribe(String topic) throws Exception {
    	
    	this.plugin.unsubscribe(topic, this.plugin.getReceptionPortURI());

    }
    
}


