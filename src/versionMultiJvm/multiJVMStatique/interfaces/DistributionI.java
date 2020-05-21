package versionMultiJvm.multiJVMStatique.interfaces;

import java.rmi.Remote;

public interface DistributionI extends Remote{
	
	public void distribute(Object... parameters) throws Exception ;

}
