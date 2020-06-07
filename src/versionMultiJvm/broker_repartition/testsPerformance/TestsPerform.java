package versionMultiJvm.broker_repartition.testsPerformance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import interfaces.MessageFilterI;
import interfaces.MessageI;
import versionMultiJvm.broker_repartition.components.Broker;

public class TestsPerform {


	protected BufferedWriter bOut;

	protected BufferedWriter bOut2;
	
	protected ArrayList<Long> times;

	protected ArrayList<Integer> nbMessages_t;

	

	
	public TestsPerform() {
		super();


		try {
			File inputFile = new File(Configuration.path_t);
			bOut = new BufferedWriter(new FileWriter(inputFile, true));
			
			File inputFile2 = new File(Configuration.path_m);
			bOut2 = new BufferedWriter(new FileWriter(inputFile2, true));


		} catch (IOException e) {
			System.out.println(e);
		}

		this.times = new ArrayList<>();

		nbMessages_t = new ArrayList<Integer>();

	}

	public void calculTimes(MessageI m) {
		// calculs du temps d'acheminement d'un message
		long t = new Timestamp(System.currentTimeMillis()).getTime();
		try {
			this.times.add(t - m.getTimeStamp().getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeTimes(String subscriberUri) {

		System.out.println("---------------affichage des timestamp--------------");
		long totalTime = 0;
		for (Long g : times) {
			totalTime += g;
		}
		System.out.println("Average time = " + totalTime / (long) times.size());

		if (bOut != null)
			try {
				bOut.write("Subscriber "+subscriberUri+" : "+String.valueOf(totalTime / (long) times.size()) + "\n");

				bOut.close();
			} catch (IOException ec) {
				System.out.println(ec);
			}

	}

	public void calcul_Average_nbMessage(HashMap<MessageI, HashMap<String, MessageFilterI>> msgToSubscribers,
			ReentrantLock publishMethodsStructureLock, String brokerUri) {

		
		try {

			// Tests de performances
//			publishMethodsStructureLock.lock();
//			System.out.println("taille de map des messages >> " + msgToSubscribers.size());
//			publishMethodsStructureLock.unlock();

			System.out.println("---------------affichage des nbMessages --------------");
			long totalnbMessage = 0;
			for (int t : nbMessages_t) {
				totalnbMessage += t;
			}
			if (bOut2 != null) {
				System.out.println("Broker "+brokerUri+" : Average nbMessages = " +String.valueOf(totalnbMessage / (double) nbMessages_t.size()));
				bOut2.write("Broker "+brokerUri+" : Average nbMessages = " + String.valueOf(totalnbMessage / (double) nbMessages_t.size())+ "\n");
				bOut2.close();
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void nbMessages_en_Attente(HashMap<MessageI, HashMap<String, MessageFilterI>> msgToSubscribers,
			ReentrantLock publishMethodsStructureLock) {
		
		while (true) {
			if (!Broker.fini) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				publishMethodsStructureLock.lock();
					nbMessages_t.add(msgToSubscribers.size());
//					System.out.println("taille de msgtosubscribers >>>>>>>"+msgToSubscribers.size());
				publishMethodsStructureLock.unlock();
			} else {
				break;
			}
		}

	}

}
