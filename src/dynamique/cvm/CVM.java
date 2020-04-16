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
