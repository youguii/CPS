package dynamique.components;

import java.util.HashSet;
import java.util.Set;

import components.Broker;
import components.Publisher;
import components.Subscriber;
import dynamique.cvm.CVM;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cvm.ComponentVirtualMachineI;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.pre.dcc.connectors.DynamicComponentCreationConnector;
import fr.sorbonne_u.components.pre.dcc.interfaces.DynamicComponentCreationI;
import fr.sorbonne_u.components.pre.dcc.ports.DynamicComponentCreationOutboundPort;

@RequiredInterfaces(required = {DynamicComponentCreationI.class})
public class Assembler
extends AbstractComponent{
	
	protected DynamicComponentCreationOutboundPort	dccOutPort ;
	protected String								jvmURI ;
	protected Set<String>							deployerURIs ;


	protected Assembler(String jvmURI) {
		super(1,0);
		
		this.jvmURI = jvmURI;
		this.deployerURIs = new HashSet<String>() ;

		
//		this.tracer.setTitle("Assembler") ;
//		this.tracer.setRelativePosition(0, 0) ;
//		this.toggleTracing() ;
	}
	
	@Override
	public void			start() throws ComponentStartException
	{
		super.start() ;
		
		try {
			this.dccOutPort = new DynamicComponentCreationOutboundPort(this) ;
			this.dccOutPort.publishPort() ;
			this.doPortConnection(
				this.dccOutPort.getPortURI(),
				this.jvmURI + AbstractCVM.DCC_INBOUNDPORT_URI_SUFFIX,
				DynamicComponentCreationConnector.class.getCanonicalName()) ;

					
		
		} catch (Exception e) {
			throw new ComponentStartException(e) ;
		}
		
		
		
	}
	
	@Override
	public void			execute() throws Exception
	{
		super.execute() ;
		
		String brokerURI =
				this.dccOutPort.createComponent(
						Broker.class.getCanonicalName(),
						new Object[] {CVM.managementBIPURI}) ;
		this.deployerURIs.add(brokerURI) ;
		System.out.println("creation broker");
		
		for(int i= 0; i < 3; i++) {
			String publisherURI =
					this.dccOutPort.createComponent(
							Publisher.class.getCanonicalName(),
							new Object[] {CVM.managementBIPURI, Integer.toString(i)}) ;
			System.out.println("creation publiser"+i);
	
			this.deployerURIs.add(publisherURI) ;
		}

		for(int i= 0; i < 3; i++) {
			String subscriberURI =
				this.dccOutPort.createComponent(
					Subscriber.class.getCanonicalName(),
					new Object[] {CVM.managementBIPURI, Integer.toString(i)}) ;
			this.deployerURIs.add(subscriberURI) ;
			System.out.println("creation subscriber"+i);

		}
		
		for (String uri : this.deployerURIs) {
			
			this.dccOutPort.startComponent(uri) ;
			
		}
		

		for (String uri : this.deployerURIs) {

			this.dccOutPort.executeComponent(uri) ;
			
		}
		


	}


	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 */
	@Override
	public void			finalise() throws Exception
	{

		System.out.println("Dans finalise");
		this.doPortDisconnection(this.dccOutPort.getPortURI()) ;

		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public void			shutdown() throws ComponentShutdownException
	{
		try {
			System.out.println("Dans shutdown");
			this.dccOutPort.unpublishPort() ;
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}
	
//	public void 	componentsCreation() throws Exception
//	{
//
//		String brokerURI =
//				this.dccOutPort.createComponent(
//						Broker.class.getCanonicalName(),
//						new Object[] {CVM.managementBIPURI}) ;
//		this.deployerURIs.add(brokerURI) ;
//		System.out.println("creation broker");
//		
//		for(int i= 0; i < 3; i++) {
//			String publisherURI =
//					this.dccOutPort.createComponent(
//							Publisher.class.getCanonicalName(),
//							new Object[] {CVM.managementBIPURI, Integer.toString(i)}) ;
//			System.out.println("creation publiser"+i);
//	
//			this.deployerURIs.add(publisherURI) ;
//		}
//
//		for(int i= 0; i < 3; i++) {
//			String subscriberURI =
//				this.dccOutPort.createComponent(
//					Subscriber.class.getCanonicalName(),
//					new Object[] {CVM.managementBIPURI, Integer.toString(i)}) ;
//			this.deployerURIs.add(subscriberURI) ;
//			System.out.println("creation subscriber"+i);
//
//		}
//		
//	}
}
// -----------------------------------------------------------------------------

	
	
	
	
	

