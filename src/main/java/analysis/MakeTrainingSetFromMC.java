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
package analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jlab.clas.physics.Particle;
import org.jlab.groot.data.H1F;
import org.jlab.groot.ui.TCanvas;
import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;
import org.jlab.io.hipo.HipoDataSource;

public class MakeTrainingSetFromMC {

	public static void main(String[] args) throws IOException {
		// First step: Read in the file you want to look at:
		// String dir = "/Volumes/FutureBoots/data/EtaPData/Torus1.0Sol0.8/";
		double limit = 0.015; // this is the mass spread limit for ML selection
								// of possible particles

		String dir = "/Volumes/DATA/CLAS12/EtaPrimeAnalysis/Torus1.0Sol0.8/";
		List<String> aList = new ArrayList<>();
		File[] listOfFiles = new File(dir).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".hipo");
			}
		});
		for (int i = 0; i < 100; i++) {// listOfFiles.length
			if (listOfFiles[i].isFile()) {
				// System.out.println("File " + listOfFiles[i].getName());
				aList.add(dir + listOfFiles[i].getName());
			}
		}

		HipoDataSource reader = new HipoDataSource();

		// Get the number of events in the hipo-file
		DataEvent event = null;

		// Define a bank you want to look at:
		DataBank MCBank = null;

		String bnkName = "MC::Particle";
		H1F mEpEmGam = new H1F("mEpEmGam", 100, 0.01, 1.05);
		H1F epemangle = new H1F("epemangle", 100, -1, 1);
		H1F epemGammaangle = new H1F("epemGammaangle", 100, -1.01, 1.01);

		boolean onceRan = true;
		for (String string : aList) {
			System.out.println(string);
			String SAMPLE_CSV_FILE = "TrainingSample.csv";
			CSVPrinter csvPrinter = null;
			BufferedWriter writer = null;
			try {
				writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_FILE), Charset.forName("US-ASCII"),
						StandardOpenOption.CREATE, StandardOpenOption.APPEND);
				if (onceRan) {
					csvPrinter = new CSVPrinter(writer,
							CSVFormat.DEFAULT.withHeader("ID", "PID", "EpEmAngle", "InvariantMassEpEmGam",
									"InvariantMassEpEm", "EmPx", "EmPy", "EmPz", "EmE", "EmTheta", "EmPhi", "GamPx",
									"GamPy", "GamPz", "GamE", "GamTheta", "GamPhi"));

					onceRan = false;
				} else {
					csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withIgnoreHeaderCase());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			reader.open(string);
			int Nevents = reader.getSize();

			for (int i = 0; i < Nevents; i++) {
				event = (DataEvent) reader.gotoEvent(i);

				// Each hipo-file has some banks with information
				// here is one example how to access one:
				// =====================================================
				if (event.hasBank(bnkName) && !AnalysisMethods.mcList(event.getBank(bnkName)).isEmpty()) {
					// Get the bank itself, when existing:
					MCBank = event.getBank(bnkName);

					AnalysisMethods.getMCMap(MCBank);

					// for (Map.Entry<String, List<Particle>> entry :
					// AnalysisMethods.getMCMap(MCBank).entrySet()) {
					// String key = entry.getKey();
					// List<Particle> value = entry.getValue();
					// // ...
					// }
					List<Particle> gamma = AnalysisMethods.getMCMap(MCBank).get("Gamma");
					Particle scatteredEm = AnalysisMethods.getMCMap(MCBank).get("Scattered").get(0);
					Particle dalitzEm = AnalysisMethods.getMCMap(MCBank).get("DalitzEm").get(0);
					Particle dalitzEp = AnalysisMethods.getMCMap(MCBank).get("DalitzEp").get(0);

					// now lets create an EpEm particle from the dalitz Em and
					// one for the scattered Em
					Particle EpEmScattered = new Particle();
					Particle EpEmDalitz = new Particle();

					EpEmScattered.copy(scatteredEm);
					EpEmScattered.combine(dalitzEp, +1);
					EpEmDalitz.copy(dalitzEm);
					EpEmDalitz.combine(dalitzEp, +1);

					// Particle gammaAdded = new Particle(22, 0.0, 0.0, 0.0);
					// for (Particle particle : gamma) {
					// gammaAdded.combine(particle, +1);
					// }
					// // Test with all gammas
					// Particle EpEmGamTemp = new Particle();
					// EpEmGamTemp.copy(EpEmDalitz);
					// EpEmGamTemp.combine(gammaAdded, +1);
					// Particle emInFrame = new Particle();
					// emInFrame.copy(dalitzEm);
					// emInFrame.inFrame(EpEmGamTemp);
					// Particle epInFrame = new Particle();
					// epInFrame.copy(dalitzEp);
					// epInFrame.inFrame(EpEmGamTemp);
					//
					// Particle xParticle = new Particle();
					// xParticle.copy(EpEmDalitz);
					// xParticle.inFrame(EpEmGamTemp);
					//
					// Particle gamInFrame = new Particle();
					// gamInFrame.copy(gammaAdded);
					// gamInFrame.inFrame(EpEmGamTemp);
					// double epEmAngle = emInFrame.cosTheta(epInFrame);
					// double epEmGamAngle = xParticle.cosTheta(gamInFrame);
					//
					// mEpEmGam.fill(EpEmGamTemp.mass());
					// epemangle.fill(epEmAngle);
					// epemGammaangle.fill(epEmGamAngle);
					// try {
					// csvPrinter.printRecord(1,
					// AnalysisMethods.getPName(EpEmGamTemp.mass(), 1, limit),
					// emInFrame.cosTheta(epInFrame),
					// xParticle.cosTheta(gammaAdded.inFrame(EpEmGamTemp)),
					// EpEmGamTemp.mass(), EpEmDalitz.mass(), dalitzEm.px(),
					// dalitzEm.py(), dalitzEm.pz(),
					// dalitzEm.theta(), dalitzEm.phi());
					// } catch (Exception e) {
					// // TODO: handle exception
					// }

					for (Particle particle : gamma) {
						Particle EpEmGamTemp = new Particle();
						EpEmGamTemp.copy(EpEmDalitz);
						EpEmGamTemp.combine(particle, +1);

						Particle emInFrame = new Particle();
						emInFrame.copy(dalitzEm);
						emInFrame.inFrame(EpEmGamTemp);
						Particle epInFrame = new Particle();
						epInFrame.copy(dalitzEp);
						epInFrame.inFrame(EpEmGamTemp);

						Particle xParticle = new Particle();
						xParticle.copy(EpEmDalitz);
						xParticle.inFrame(EpEmGamTemp);

						Particle gamInFrame = new Particle();
						gamInFrame.copy(particle);
						gamInFrame.inFrame(EpEmGamTemp);

						double epEmAngle = emInFrame.cosTheta(epInFrame);
						double epEmGamAngle = xParticle.cosTheta(gamInFrame);

						mEpEmGam.fill(EpEmGamTemp.mass());
						epemangle.fill(epEmAngle);
						epemGammaangle.fill(epEmGamAngle);

						int signal = 1;
						if (AnalysisMethods.getPName(EpEmGamTemp.mass(), 1, limit).equals("Background")) {
							signal = 0;
						}
						try {
							csvPrinter.printRecord(signal, AnalysisMethods.getPName(EpEmGamTemp.mass(), 1, limit),
									epEmAngle, EpEmGamTemp.mass(), EpEmDalitz.mass(), dalitzEm.px(), dalitzEm.py(),
									dalitzEm.pz(), dalitzEm.e(), dalitzEm.theta(), dalitzEm.phi(), particle.px(),
									particle.py(), particle.pz(), particle.e(), particle.theta(), particle.phi());
						} catch (Exception e) {
							// TODO: handle exception
						}
						// scattered
						Particle EpEmScatGamTemp = new Particle();
						EpEmScatGamTemp.copy(EpEmScattered);
						EpEmScatGamTemp.combine(particle, +1);

						Particle emScatInFrame = new Particle();
						emScatInFrame.copy(scatteredEm);
						emScatInFrame.inFrame(EpEmScatGamTemp);

						Particle ep4ScatInFrame = new Particle();
						ep4ScatInFrame.copy(dalitzEp);
						ep4ScatInFrame.inFrame(EpEmScatGamTemp);
						double epEmScatAngle = emScatInFrame.cosTheta(ep4ScatInFrame);
						try {
							csvPrinter.printRecord(0, "Background", epEmScatAngle, EpEmScatGamTemp.mass(),
									EpEmScattered.mass(), scatteredEm.px(), scatteredEm.py(), scatteredEm.pz(),
									scatteredEm.e(), scatteredEm.theta(), scatteredEm.phi(), particle.px(),
									particle.py(), particle.pz(), particle.e(), particle.theta(), particle.phi());
						} catch (Exception e) {
							// TODO: handle exception
						}
					} // end for loop
				}
				// =====================================================
			}
			csvPrinter.flush();
		}
		TCanvas c6 = new TCanvas("c6", 500, 500);
		c6.divide(1, 3);
		c6.cd(0);
		c6.draw(mEpEmGam);
		c6.cd(1);
		c6.getCanvas().getPad(1).getAxisY().setLog(true);

		c6.draw(epemangle);
		c6.cd(2);
		c6.getCanvas().getPad(2).getAxisY().setLog(true);
		c6.draw(epemGammaangle);
	}
}
