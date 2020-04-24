package dynamique.cvm;

import dynamique.components.Assembler;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

public class			CVM
extends		AbstractCVM
{
	 public static final String managementBIPURI = "managementBIPURI";

	public CVM() throws Exception {}
	
	
	
	@Override
	public void			deploy() throws Exception
	{
		// --------------------------------------------------------------------
		// Configuration phase
		// --------------------------------------------------------------------

		// debugging mode configuration; comment and uncomment the line to see
		// the difference
//		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.PUBLIHSING) ;
//		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CONNECTING) ;
//		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.COMPONENT_DEPLOYMENT) ;

		// --------------------------------------------------------------------
		// Creation phase
		// --------------------------------------------------------------------

		AbstractComponent.createComponent(
								Assembler.class.getCanonicalName(),
								new Object[]{AbstractCVM.thisJVMURI}) ;

		super.deploy() ;
	}

	public static void	main(String[] args) {
		try {
			System.out.println("demarage du dynamyque");
			CVM c = new CVM() ;
			c.startStandardLifeCycle(10000L) ;
			Thread.sleep(10000L) ;
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}


}
