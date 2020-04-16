package versionPlugin.components;

import java.io.Serializable;
import java.net.InetAddress;
import java.sql.Timestamp;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import utiles.Message;
import utiles.Properties;
import utiles.TimeStamp;
import versionPlugin.plugins.PublisherPlugin;

public class Publisher
extends AbstractComponent {
	

	protected final static String	MY_PLUGIN_URI = "publisher-plugin-uri" ;

	protected PublisherPlugin plugin;
	
	/** Uri du port entrant du service management */
	protected String managementBIPURI;
	/**
	 * uri pour distinguer entre les publiers (utilisé lors du lancement des
	 * senarios)
	 */
	protected String publisherUri;

	/**
	 *
	 * @param managementBIPURI Uri du port entrant du service management
	 * @param uri              uri pour distinguer entre les publiers
	 * @throws Exception
	 */
    protected Publisher(String managementBIPURI,
            String uri) throws Exception {
       
    	super(1, 0);
    	   
    	
        this.managementBIPURI = managementBIPURI;
        this.publisherUri = uri;

       
        assert managementBIPURI != null : 
        	new PreconditionException("Publisher : Broker's management port can't be null");
        
        assert uri != null : 
        	new PreconditionException("Publisher : publisher uri can't be null");
    
        
           
        //Affichage de la console
 		this.tracer.setTitle("publisher"+uri) ;
 		this.tracer.setRelativePosition(Integer.parseInt(uri), 1) ;
 		this.toggleTracing() ;
        
    }
    
    // ------------------------------------------------------------------------
    // Component life-cycle
    // ------------------------------------------------------------------------

    
    @Override
    public void    execute() throws Exception
    {
        super.execute();
        this.logMessage("executing publisher component.") ;

        // Install the plug-in.
		this.plugin = new PublisherPlugin(managementBIPURI, publisherUri ) ;
		plugin.setPluginURI(MY_PLUGIN_URI) ;
		this.installPlugin(plugin) ;


            
            // Différents Senarios du Publier
            switch (this.publisherUri) {
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
	 * premier senario qui crée des topics, publie un message à un topic précis,
	 * appele la distruction d'un topic, publie plusieurs messages
	 * 
	 * @throws Exception
	 */
	public void senario_One() throws Exception {
		// Création de topic
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Publisher) this.getTaskOwner()).createTopic("Salutations");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Création de topic
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Publisher) this.getTaskOwner()).createTopic("Cultures");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Création de plusieurs topics
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Publisher) this.getTaskOwner()).createTopics();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Publication d'un message au sujet "Salutation"
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Publisher) this.getTaskOwner()).publishMessage("Salutations",
							generateMsg("mp1", null, null, "Hello in english"));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Création de topic
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Publisher) this.getTaskOwner()).createTopic("Saisons");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Déstruction de topic
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Publisher) this.getTaskOwner()).destroyTopic("Cultures");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Publication de plusieurs messages
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					for (int i = 2000; i < 2005; i++) {
						Thread.sleep(500);
						((Publisher) this.getTaskOwner()).publishMessage("Saisons",
								generateMsg("mp" + i, null, null, "L'hiver " + i + " il a fait froid"));
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Création de topic "Guerres"
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Publisher) this.getTaskOwner()).createTopic("Guerres");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

	}

	/**
	 * senario 2: crée des topics publie plusieurs messages associé à un topic
	 * ,publication de message à un topic precis, publication d un message avec
	 * différents topics
	 * 
	 * @throws Exception
	 */
	public void senario_Two() throws Exception {

		// Création de topic
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Publisher) this.getTaskOwner()).createTopic("Langues");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Création de topic
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Publisher) this.getTaskOwner()).createTopic("Danses");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Création de topic
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Publisher) this.getTaskOwner()).createTopic("Pays");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Création et publication de messages sans filtre et avec un seul topic
		Message[] msgs = { generateMsg("mp2", null, null, "Français"), generateMsg("mp3", null, null, "Anglais") };

		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Publisher) this.getTaskOwner()).publishMessages(msgs, "Langues");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Publication d'un message avec un topic et sans filtre
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Publisher) this.getTaskOwner()).publishMessage("Danses", generateMsg("mp4", null, null, "Salsa"));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Publication d'un message avec filtre
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Publisher) this.getTaskOwner()).publishMessage("Pays",
							generateMsg("mp5", "Européen", false, "Inde"));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Publication d'un message avec plusieurs topics
		String[] topics = { "Danses", "Pays" };
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Publisher) this.getTaskOwner())
							.publishMessageWithManyTopics(generateMsg("mp6", null, null, "Tango"), topics);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

	}

	/**
	 * senario 3: publication de plusieurs messages chacun associé à un topic,
	 * publication d'un ensemble de messages associé à un ensemble de topics
	 * 
	 * @throws Exception
	 */

	public void senario_Three() throws Exception {
		// Publication de plusieurs messages
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					for (int counter = 100; counter < 104; counter++) {
						Thread.sleep(500);
						((Publisher) this.getTaskOwner()).publishMessage("Salutations",
								generateMsg("mp" + counter, null, null, "Salut" + counter));
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Création de messages et topics et publication
		Message[] msgs = { generateMsg("mp7", null, null, "Japon"), generateMsg("mp8", null, null, "Écosse") };

		String[] topics = { "Cultures", "Pays" };
		this.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Publisher) this.getTaskOwner()).publishMessagesWithManyTopics(msgs, topics);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

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
	public Message generateMsg(String uri, String proprertyName, Object propertyValue, Serializable contenu)
			throws Exception {
		Message msg = new Message(uri, new TimeStamp((new Timestamp(System.currentTimeMillis())).getTime(),
				InetAddress.getLocalHost().getHostAddress()), new Properties(), contenu);

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
		this.plugin.publishMessage(msg, topic);

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
		this.plugin.publishMessageWithManyTopics(msg, topics);

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

		this.plugin.publishMessages(msg, topic);

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

		this.plugin.publishMessagesWithManyTopics(msg, topics);
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
		this.plugin.createTopic(topic);
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
		this.plugin.createTopics(topics);
	}

	/**
	 * distruction d'un topic
	 * 
	 * @param topic
	 * @throws Exception
	 */
	public void destroyTopic(String topic) throws Exception {
		this.logMessage("Publisher détruit le topic " + topic);

		this.plugin.destroyTopics(topic);
	}

	/**
	 * test si le nom en parametre est réelement un topic
	 * 
	 * @param topic
	 * @return
	 * @throws Exception
	 */
	public boolean isTopic(String topic) throws Exception {
		return this.plugin.isTopic(topic);
	}

	/**
	 *
	 * @return une liste de tous les topics
	 * @throws Exception
	 */
	public String[] getTopics() throws Exception {
		return this.plugin.getTopics();
	}

	/**
	 *
	 * @return l uri du port entrant du service publication
	 * @throws Exception
	 */
	public String getPublicationUri() throws Exception {
		return this.plugin.getPublicationPortURI();
	}
}



