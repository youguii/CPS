package versionMultiJvm.multiJVMStatique.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import versionMultiJvm.multiJVMStatique.interfaces.DistributionCI;

public class	DistributionConnector 
extends 	AbstractConnector 
implements 	DistributionCI {

	@Override
	public void distribute(Object... parameters) throws Exception {
		((DistributionCI)this.offering).distribute(parameters);

	}

}
