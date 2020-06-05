package testsIntegration;

import fr.sorbonne_u.components.AbstractComponent;
import utiles.Message;

import java.util.HashMap;

import components.Publisher;

public class SenariosPublisher {
	
	protected Publisher publisher;

	public SenariosPublisher(Publisher publisher) {
		super();
		this.publisher = publisher;
	}
	
	/**
	 * premier senario qui crée des topics, publie un message à un topic précis,
	 * appele la distruction d'un topic, publie plusieurs messages
	 * 
	 * @throws Exception
	 */
	public void senario_One() throws Exception {
		// Création de topic
		this.publisher.runTask(new AbstractComponent.AbstractTask() {
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
		this.publisher.runTask(new AbstractComponent.AbstractTask() {
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
		this.publisher.runTask(new AbstractComponent.AbstractTask() {
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
		this.publisher.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Publisher) this.getTaskOwner()).publishMessage("Salutations",
							publisher.generateMsg("mp1", null, "Hello in english"));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Création de topic
		this.publisher.runTask(new AbstractComponent.AbstractTask() {
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
		this.publisher.runTask(new AbstractComponent.AbstractTask() {
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
		this.publisher.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					for (int i = 2000; i < 2005; i++) {
						Thread.sleep(500);
						((Publisher) this.getTaskOwner()).publishMessage("Saisons",
								publisher.generateMsg("mp" + i, null, "L'hiver " + i + " il a fait froid"));
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Création de topic "Guerres"
		this.publisher.runTask(new AbstractComponent.AbstractTask() {
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
		this.publisher.runTask(new AbstractComponent.AbstractTask() {
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
		this.publisher.runTask(new AbstractComponent.AbstractTask() {
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
		this.publisher.runTask(new AbstractComponent.AbstractTask() {
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
		Message[] msgs = { publisher.generateMsg("mp2", null, "Français"), publisher.generateMsg("mp3", null, "Anglais") };

		this.publisher.runTask(new AbstractComponent.AbstractTask() {
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
		this.publisher.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Publisher) this.getTaskOwner()).publishMessage("Danses", publisher.generateMsg("mp4", null, "Salsa"));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		
		// Publication d'un message avec un topic et sans filtre
		this.publisher.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Publisher) this.getTaskOwner()).publishMessage("Nourriture", publisher.generateMsg("mp5Bis", null, "Inde"));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		
		// Publication d'un message avec filtre
		this.publisher.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					
					HashMap<String, Object> props = new HashMap<String, Object>();
					props.put("Europen", false);
					
					((Publisher) this.getTaskOwner()).publishMessage("Pays", publisher.generateMsg("mp5", null, "Inde"));
				
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		//Publication d'un message avec plusieurs topics
		String[] topics = { "Danses", "Pays" };
		this.publisher.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					((Publisher) this.getTaskOwner())
							.publishMessageWithManyTopics(publisher.generateMsg("mp6", null, "Tango"), topics);
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
		this.publisher.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					for (int counter = 100; counter < 104; counter++) {
						Thread.sleep(500);
						((Publisher) this.getTaskOwner()).publishMessage("Pays",
								publisher.generateMsg("mp" + counter, null, "Salut" + counter));
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Création de messages et topics et publication
		Message[] msgs = { publisher.generateMsg("mp7", null, "Japon"), publisher.generateMsg("mp8", null, "Écosse") };

		String[] topics = { "Cultures", "Pays" };
		this.publisher.runTask(new AbstractComponent.AbstractTask() {
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
	 * senario 4: publication de plusieurs messages chacun associé à un topic,
	 * publication d'un ensemble de messages associé à un ensemble de topics
	 * 
	 * @throws Exception
	 */

	public void senario_Four(int i, int uri) throws Exception {
		//tableau de topics 
		String[] topics = {"Sport", "Peche", "Voyage", "Musique", "Art", "Theatre", "Cinema", "Histoire", "Science", "Divertissement", 
				"Literature", "Medecine", "Mathematique", "Informatique", "Algorithmique", "Composant", "Electronique", "Commerce", "Hight-Tech", "People"};
		
		
//		int r = Double.valueOf(Math.random()*19).intValue();
		int r = i % 20;

		this.publisher.publishMessage(topics[r],publisher.generateMsg("mp_Test"+uri+"_"+i, null, "Salut" + i));
		if(i > 0) {	
			// Publication de plusieurs messages
			this.publisher.runTask(new AbstractComponent.AbstractTask() {
				@Override
				public void run() {
					try {
						//for (int counter = 100; counter < 104; counter++) {
							Thread.sleep(500);
							senario_Four(i - 1, uri);
						//}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
		}

	}
	
	
	


}
