package testsIntegration;

import components.Subscriber;
import fr.sorbonne_u.components.AbstractComponent;
import interfaces.MessageFilterI;
import interfaces.MessageI;
import utiles.MessageFilter;

public class SenariosSubscriber {

	protected Subscriber subscriber;

	public SenariosSubscriber(Subscriber subscriber) {
		super();
		this.subscriber = subscriber;
	}

	

	/**
	 * senario 1: un souscripteur souscrit à un topic, il crée un topic, crée
	 * plusieurs topic , appelle à la distruction d'un topic
	 * 
	 * @throws Exception
	 */
	public void senario_One() throws Exception {
		// Souscrire au topic Danses
		this.subscriber.runTask(new AbstractComponent.AbstractTask() {
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

		this.subscriber.runTask(new AbstractComponent.AbstractTask() {
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

		this.subscriber.runTask(new AbstractComponent.AbstractTask() {
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
		this.subscriber.runTask(new AbstractComponent.AbstractTask() {
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
		this.subscriber.runTask(new AbstractComponent.AbstractTask() {
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

		this.subscriber.runTask(new AbstractComponent.AbstractTask() {
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

		this.subscriber.runTask(new AbstractComponent.AbstractTask() {
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
		

		// Initialiser un filtre
		MessageFilter f = new MessageFilter() {
			
			@Override
			public synchronized boolean filter(MessageI m) throws Exception {
				//on veut garder les messages avec une longueur < 100 
				Integer lenM = m.getProperties().getIntProp("lenM");
				if(lenM != null && lenM > 200) {
					return false;
				}
				return true;
			}
		};
		
//		MessageFilter f = new MessageFilter(2, null, null, null);

		// Souscrire à un topic avec filtre
		this.subscriber.runTask(new AbstractComponent.AbstractTask() {
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
		
		// Souscrir à un topic sans filtre
		this.subscriber.runTask(new AbstractComponent.AbstractTask() {
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
		MessageFilter f2 = new MessageFilter(2, null, null, null);

		// Souscrire à un topic avec filtre
		this.subscriber.runTask(new AbstractComponent.AbstractTask() {
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

	/**
	 * senario 4: publication de plusieurs messages chacun associé à un topic,
	 * publication d'un ensemble de messages associé à un ensemble de topics
	 * 
	 * @throws Exception
	 */

	public void senario_Four(int i) throws Exception {
		//tableau de topics 
		String[] topics = {"Sport", "Peche", "Voyage", "Musique", "Art" /*, "Theatre", "Cinema", "Histoire", "Science", "Divertissement", 
				"Literature", "Medecine", "Mathematique", "Informatique", "Algorithmique", "Composant", "Electronique", "Commerce", "Hight-Tech", "People"*/};
		
		
		//int r = Double.valueOf(Math.random()*19).intValue();
		int r = i % 5;
		
		this.subscriber.subscribeToTopic(topics[r]);
		if(i > 0) {	
			// Publication de plusieurs messages
			this.subscriber.runTask(new AbstractComponent.AbstractTask() {
				@Override
				public void run() {
					try {
						//for (int counter = 100; counter < 104; counter++) {
							Thread.sleep(500);
							senario_Four(i - 1);
						//}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
		}

	}

	
	
}
