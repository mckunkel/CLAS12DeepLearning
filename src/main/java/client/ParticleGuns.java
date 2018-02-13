/*  +__^_________,_________,_____,________^-.-------------------,
 *  | |||||||||   `--------'     |          |                   O
 *  `+-------------USMC----------^----------|___________________|
 *    `\_,---------,---------,--------------'
 *      / X MK X /'|       /'
 *     / X MK X /  `\    /'
 *    / X MK X /`-------'
 *   / X MK X /
 *  / X MK X /
 * (________(                @author m.c.kunkel
 *  `------'
*/
package client;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.jlab.clas.physics.Particle;

import domain.lundWriter.LundHeader;
import domain.lundWriter.OpenCloseLundFile;
import domain.lundWriter.OpenCloseLundFileImpl;

public class ParticleGuns {

	public static void main(String[] args) {
		LundHeader lundHeader = new LundHeader(1, 1, 1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

		int NEvents = 5000000; // 5 million for each particle should yield a
								// upper limit of 3M for p 1.5M for pi 05M for e
								// and gamma
		int nEventsPerFile = 10000;

		Map<String, Integer> particleMap = new HashMap<>();
		particleMap.put("Electron", 11);
		particleMap.put("Positron", -11);
		particleMap.put("Proton", 2212);
		particleMap.put("AntiProton", -2212);
		particleMap.put("Neutron", 2112);
		particleMap.put("AntiNeutron", -2112);
		particleMap.put("PiPlus", 211);
		particleMap.put("PiMinus", -211);
		particleMap.put("KPlus", 321);
		particleMap.put("KMinus", -321);
		particleMap.put("Gamma", 22);

		double pMin = 0.25; // GeV
		double pMax = 9.5; // GeV
		double thetaMin = 5.0; // 5 degrees, minimum DC acceptance
		double thetaMax = Math.toDegrees(Math.PI / 2.0) - 5.0; // 40 degrees,
																// maximum
																// clas12
																// acceptance
		double phiMin = -180.0; // degrees
		double phiMax = 180.0; // degrees

		double targetLength = 5.0;
		double targetCenter = 0.0;
		// OK first lets make the directories

		for (String key : particleMap.keySet()) {
			File theDir = new File(key);
			// if the directory does not exist, create it
			if (!theDir.exists()) {
				System.out.println("creating directory: " + theDir.getName());
				boolean result = false;

				try {
					theDir.mkdir();
					result = true;
				} catch (SecurityException se) {
					// handle it
				}
				if (result) {
					System.out.println(key + " directory created");
				}
			}
		}
		for (Map.Entry<String, Integer> entry : particleMap.entrySet()) {
			int lundPartNum = 1;
			int eventNumber = 0;

			String key = entry.getKey();
			Integer value = entry.getValue();

			OpenCloseLundFile openCloseLundFile = new OpenCloseLundFileImpl(
					key + "/" + key + "_" + lundPartNum + ".lund");
			openCloseLundFile.openLundFile();
			ParticleGeneratorWithVertices pgun = new ParticleGeneratorWithVertices(value, pMin, pMax, thetaMin,
					thetaMax, phiMin, phiMax, targetLength, targetCenter);
			for (int loop = 0; loop < NEvents; loop++) {
				if (eventNumber == nEventsPerFile) {
					eventNumber = 0;
					lundPartNum++;
					openCloseLundFile.closeLundFile();
					openCloseLundFile.openLundFile(key + "/" + key + "_" + lundPartNum + ".lund");
				}

				Particle particle = pgun.getNewParticle();

				openCloseLundFile.writeEvent(lundHeader);
				openCloseLundFile.writeEvent("1 " + particle.toLundString());
				openCloseLundFile.writeFlush();
				eventNumber++;
			}
			openCloseLundFile.closeLundFile();
			System.out.println("Done with " + key + " genertation");
		}
	}
}
