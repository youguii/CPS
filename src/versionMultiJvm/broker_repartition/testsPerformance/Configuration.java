package versionMultiJvm.broker_repartition.testsPerformance;

public class Configuration {

	/**
	 * chemin du fichier oû sont ecrit les resultat des temps moyens d'acheminement
	 * de message
	 */
	public static String path_t = "/home/kiska/git/CPS/src/versionMultiJvm/broker_repartition/testsPerformance/Averages_Times.txt";

	/**
	 * chemin du fichier oû sont ecrit les resultat pour le nombre moyen de messages
	 * en attente de livraison
	 */
	public static String path_m = "/home/kiska/git/CPS/src/versionMultiJvm/broker_repartition/testsPerformance/Averages_nbMessages.txt";


	/** nombre de publishers DCVM dans chaque JVM */
	public static int nb_publishers_DCVM = 10;
}
