//CPSDistrib/src/versionMultiJvm/versionStaticMJvm/jars/CPSDistrib.jar


package versionMultiJvm.versionStaticMJvm;


import components.Publisher;
import components.Subscriber;
import fr.sorbonne_u.alasca.replication.combinators.FixedCombinator;
import fr.sorbonne_u.alasca.replication.combinators.LoneCombinator;
import fr.sorbonne_u.alasca.replication.combinators.MajorityVoteCombinator;
import fr.sorbonne_u.alasca.replication.combinators.RandomCombinator;
import fr.sorbonne_u.alasca.replication.components.ReplicationManager;
import fr.sorbonne_u.alasca.replication.examples.deployments.CVM;
import fr.sorbonne_u.alasca.replication.interfaces.PortFactoryI;
import fr.sorbonne_u.alasca.replication.ports.ReplicableInboundPort;
import fr.sorbonne_u.alasca.replication.ports.ReplicableOutboundPort;
import fr.sorbonne_u.alasca.replication.selectors.RandomDispatcherSelector;
import fr.sorbonne_u.alasca.replication.selectors.RoundRobinDispatcherSelector;
import fr.sorbonne_u.alasca.replication.selectors.WholeSelector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;
import fr.sorbonne_u.components.ports.InboundPortI;
import fr.sorbonne_u.components.ports.OutboundPortI;
import versionMultiJvm.versionStaticMJvm.components.Broker;



public class			DistributedCVM
extends		AbstractDistributedCVM
{
	protected static final String	JVM1_URI = "jvm1" ;
	protected static final String	JVM2_URI = "jvm2" ;
	protected static final String	JVM3_URI = "jvm3" ;


    protected static final String managementBIPURI1 = "managementBIPURI1";
    protected static final String managementBIPURI2 = "managementBIPURI2";
    
    protected static final String MANAGER_INBOUND_PORT_URI = "MANAGER_INBOUND_PORT_URI";
    
    public static final String[]		BROKER_INBOUND_PORT_URIS =
			new String[]{
				"brokerC_IP_URI1",
				"brokerC_IP_URI2"
	} ;
    
    public static enum SelectorType {
		ROUND_ROBIN,
		RANDOM,
		WHOLE
	}
    
	public static enum CombinatorType {
		FIXED,
		LONE,
		MAJORITY_VOTE,
		RANDOM
	}
	
	protected final SelectorType	currentSelector = SelectorType.WHOLE ;
	protected final CombinatorType	currentCombinator = CombinatorType.MAJORITY_VOTE ;

	public static final PortFactoryI PC =
			new PortFactoryI() {
				@Override
				public InboundPortI createInboundPort(ComponentI c)
						throws Exception
				{
					return new ReplicableInboundPort<String>(c) ;
				}

				@Override
				public InboundPortI createInboundPort(String uri, ComponentI c)
						throws Exception
				{
					return new ReplicableInboundPort<String>(uri, c) ;
				}

				@Override
				public OutboundPortI createOutboundPort(ComponentI c)
						throws Exception
				{
					return new ReplicableOutboundPort<String>(c) ;
				}

				@Override
				public OutboundPortI createOutboundPort(String uri, ComponentI c)
						throws Exception
				{
					return new ReplicableOutboundPort<String>(uri, c) ;
				}
			} ;
	
	
	public				DistributedCVM(String[] args, int xLayout, int yLayout)
	throws Exception
	{
		super(args, xLayout, yLayout);
	}




	@Override
	public void			instantiateAndPublish() throws Exception
	{
		System.out.println(thisJVMURI);
		if (thisJVMURI.equals(JVM1_URI)) {
			
			AbstractComponent.createComponent(
					ReplicationManager.class.getCanonicalName(), 
					new Object[] {
							1,
							MANAGER_INBOUND_PORT_URI,
							(currentSelector == SelectorType.ROUND_ROBIN ?
									new RoundRobinDispatcherSelector(
											BROKER_INBOUND_PORT_URIS.length)
								:	currentSelector == SelectorType.RANDOM ?
										new RandomDispatcherSelector()
									:	new WholeSelector()
							),
							(currentCombinator == CombinatorType.FIXED) ?
									new FixedCombinator<String>(1)
								:	currentCombinator == CombinatorType.LONE ?
										new LoneCombinator<String>()
									:	currentCombinator == CombinatorType.MAJORITY_VOTE ?
										new MajorityVoteCombinator<String>(
											(o1,o2) -> o1.equals(o2),
											RuntimeException.class
										)
										:	new RandomCombinator<String>(),
							PC,
							BROKER_INBOUND_PORT_URIS
					});
			
//			AbstractComponent.createComponent(
//	                Broker.class.getCanonicalName(),
//	                new Object[] { managementBIPURI1, MANAGER_INBOUND_PORT_URI, BROKER_INBOUND_PORT_URIS[0]});
//
//			AbstractComponent.createComponent(
//					Subscriber.class.getCanonicalName(),
//					new Object[] {managementBIPURI1, Integer.toString(2)});
//
//			AbstractComponent.createComponent(
//	                Broker.class.getCanonicalName(),
//	                new Object[] { managementBIPURI2, MANAGER_INBOUND_PORT_URI, BROKER_INBOUND_PORT_URIS[1]});
//
//			AbstractComponent.createComponent(
//					Publisher.class.getCanonicalName(),
//					new Object[] {managementBIPURI2, Integer.toString(2) });


		} else if (thisJVMURI.equals(JVM2_URI)) {

			AbstractComponent.createComponent(
	                Broker.class.getCanonicalName(),
	                new Object[] { managementBIPURI1, MANAGER_INBOUND_PORT_URI, BROKER_INBOUND_PORT_URIS[0]});
	    	
		
//			AbstractComponent.createComponent(
//					Publisher.class.getCanonicalName(),
//					new Object[] {managementBIPURI, Integer.toString(2) });
	    	

			AbstractComponent.createComponent(
					Subscriber.class.getCanonicalName(),
					new Object[] {managementBIPURI1, Integer.toString(2)});


		} else if (thisJVMURI.equals(JVM3_URI)) {
			
			AbstractComponent.createComponent(
	                Broker.class.getCanonicalName(),
	                new Object[] { managementBIPURI2, MANAGER_INBOUND_PORT_URI, BROKER_INBOUND_PORT_URIS[1]});
	    	
		
		
			AbstractComponent.createComponent(
					Publisher.class.getCanonicalName(),
					new Object[] {managementBIPURI2, Integer.toString(2) });
	    	
//
//			AbstractComponent.createComponent(
//					Subscriber.class.getCanonicalName(),
//					new Object[] {managementBIPURI, Integer.toString(2)});
			
		
		
		} else {

			System.out.println("Unknown JVM URI... " + thisJVMURI) ;

		}

		super.instantiateAndPublish();
	}

	
	
	
	public static void	main(String[] args)
	{
		try {
			DistributedCVM da  = new DistributedCVM(args, 2, 5) ;
			da.startStandardLifeCycle(15000L) ;
			Thread.sleep(10000L) ;
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
}
//-----------------------------------------------------------------------------
