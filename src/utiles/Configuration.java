package utiles;

public class Configuration {

	/**
	 * chemin du fichier oû sont ecrit les resultat des temps moyens d'acheminement
	 * de message
	 */
	public static String path_t = "/home/mourad/git/Projet_cps_final/CPS/src/versionMultiJvm/broker_repartition/testsPerformance/Averages_Times.txt";

	/**
	 * chemin du fichier oû sont ecrit les resultat pour le nombre moyen de messages
	 * en attente de livraison
	 */
	public static String path_m = "/home/mourad/git/Projet_cps_final/CPS/src/versionMultiJvm/broker_repartition/testsPerformance/Averages_nbMessages.txt";

	/** nombre d'executions de CVM simple */
	public static int nbExecutions = 10;

	/**
	 * nombre de publishers de la premiere execution CVM simple nombre de publishers
	 * max = nbExecutions x nb_publishers_CVM
	 */
	public static int nb_publishers_CVM = 10;

	/** nombre de publishers DCVM dans chaque JVM */
	public static int nb_publishers_DCVM = 10;
}
