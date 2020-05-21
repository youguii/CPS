package versionMultiJvm.multiJVMStatique.components;

import java.util.HashMap;
import java.util.Map;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.InboundPortI;
import fr.sorbonne_u.components.ports.OutboundPortI;
import versionMultiJvm.multiJVMStatique.connectors.DistributionConnector;
import versionMultiJvm.multiJVMStatique.interfaces.DistributionCI;
import versionMultiJvm.multiJVMStatique.interfaces.DistributionI;
import versionMultiJvm.multiJVMStatique.ports.DistributionInboundPort;
import versionMultiJvm.multiJVMStatique.ports.DistributionOutboundPort;

public class BrokersManager extends AbstractComponent implements DistributionI {
	
	/** inbound port through which clients call this component.				*/
	protected InboundPortI		inboundPort ;
	
	/** outbound ports through which this component calls brokers.			*/
	protected OutboundPortI[]	outboundPorts ;
	
	/** a function mapping outbound ports to unique numbers.				*/
	protected Map<OutboundPortI,Integer>	numbers ;

	/** URIs of the inbound ports of the server to connect this component.	*/
	protected String[]			brokerInboundPortURIs ;
	
	

	protected BrokersManager(
			int nbThreads, 
			String ownInboundPortURI,
			String[] brokerInboundPortURIs) throws Exception {
		
		
		super(nbThreads, 0);

		assert	nbThreads > 0 ;
		assert	ownInboundPortURI != null ;
		assert	brokerInboundPortURIs != null && brokerInboundPortURIs.length > 0 ;
		
		this.init(ownInboundPortURI, brokerInboundPortURIs);
											
	}

	
	protected BrokersManager(
			String reflectionInboundPortURI, 
			int nbThreads, 
			int nbSchedulableThreads,
			String ownInboundPortURI,
			String[] brokerInboundPortURIs) throws Exception{
		
		super(reflectionInboundPortURI, nbThreads, nbSchedulableThreads);

		assert	nbThreads > 0 ;
		assert	ownInboundPortURI != null ;
		assert	brokerInboundPortURIs != null && brokerInboundPortURIs.length > 0 ;
		
		this.init(ownInboundPortURI, brokerInboundPortURIs);
	}
	
	

	protected void init (
			String ownInboundPortURI,
			String[] brokerInboundPortURIs) throws Exception {
		
		// Create InboundPort
		this.addOfferedInterface(DistributionCI.class);
		this.inboundPort = new DistributionInboundPort(ownInboundPortURI, this);
		//assert	this.inboundPort instanceof DistributionCI ;
		this.inboundPort.publishPort() ;
	
		//assert	this.inboundPort instanceof DistributionCI ;
		
		// Create OutboundPorts
		this.addRequiredInterface(DistributionCI.class);
		this.outboundPorts = new OutboundPortI[brokerInboundPortURIs.length] ;
		this.numbers = new HashMap<OutboundPortI,Integer>() ;
		
		for (int i = 0 ; i < brokerInboundPortURIs.length ; i++) {
			this.outboundPorts[i] = new DistributionOutboundPort(this) ;
			this.numbers.put(this.outboundPorts[i], i) ;
			
			//assert	this.outboundPorts[i] instanceof DistributionCI ;
			this.outboundPorts[i].publishPort() ;
		}
		this.brokerInboundPortURIs = brokerInboundPortURIs ;
		
		
		this.tracer.setTitle("BrokersManager") ;
		this.tracer.setRelativePosition(1, 0) ;
		this.toggleTracing() ;
	}
	
	
	@Override
	public void			start() throws ComponentStartException
	{
		super.start() ;
		for (int i = 0 ; i < this.brokerInboundPortURIs.length ; i++) {
			try {
				this.doPortConnection(
						this.outboundPorts[i].getPortURI(),
						brokerInboundPortURIs[i],
						DistributionConnector.class.getCanonicalName());
			} catch (Exception e) {
				throw new ComponentStartException(e) ;
			}
		}
	}
	
	@Override
	public void			finalise() throws Exception
	{
		for (int i = 0 ; i < this.outboundPorts.length ; i++) {
			this.doPortDisconnection(this.outboundPorts[i].getPortURI()) ;
		}
		super.finalise() ;
	}
	
	@Override
	public void			shutdown() throws ComponentShutdownException
	{
		try {
			for (int i = 0 ; i < this.outboundPorts.length ; i++) {
				this.outboundPorts[i].unpublishPort() ;
			}
			this.inboundPort.unpublishPort() ;
			
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown() ;
	}
	
	
	@Override
	public void distribute(Object... parameters) throws Exception {
		for (int i = 0 ; i <  this.outboundPorts.length ; i++) {
			try {
					((DistributionCI) this.outboundPorts[i]).distribute(parameters) ;
			} catch(Exception e) {
				throw new Exception(e);
			}
		}
		
	}

}