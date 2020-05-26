package versionMultiJvm.broker_repartition.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import versionMultiJvm.broker_repartition.interfaces.DistributionCI;

public class	DistributionOutboundPort 
extends 	AbstractOutboundPort 
implements	DistributionCI {

	private static final long serialVersionUID = 1L;


	public DistributionOutboundPort(String uri, ComponentI owner) throws Exception {
		
		super(uri, DistributionCI.class, owner);
	}

	public DistributionOutboundPort(ComponentI owner) throws Exception {
		
		super(DistributionCI.class, owner) ;
	}

	
	@Override
	public void distribute(Object... parameters) throws Exception {
		
		((DistributionCI)this.connector).distribute(parameters) ;
	}

}
