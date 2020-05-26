package versionMultiJvm.broker_repartition.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import versionMultiJvm.broker_repartition.interfaces.DistributionCI;
import versionMultiJvm.broker_repartition.interfaces.DistributionI;

public class 	DistributionInboundPort 
extends 	AbstractInboundPort 
implements 	DistributionCI {

	private static final long serialVersionUID = 1L;
	
	public DistributionInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, DistributionCI.class, owner);
		assert	owner instanceof DistributionI ;
	}

	public DistributionInboundPort(ComponentI owner) throws Exception {
		super(DistributionCI.class, owner) ;
		assert	owner instanceof DistributionI ;
	}

	
	@Override
	public void distribute(Object... parameters) throws Exception {
		this.getOwner().handleRequestSync(
				o -> ((DistributionI)o)).distribute(parameters);

	}

}
