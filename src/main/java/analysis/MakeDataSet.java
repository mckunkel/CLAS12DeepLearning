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
import java.util.Stack;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jlab.clas.physics.Particle;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.ui.TCanvas;
import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;
import org.jlab.io.hipo.HipoDataSource;

public class MakeDataSet {

	public static void main(String[] args) throws IOException {

		// First step: Read in the file you want to look at:
		// String dir = "/Volumes/FutureBoots/data/EtaPData/Torus1.0Sol0.8/";
		double limit = 0.065; // this is the mass spread limit for ML selection
								// of possible particles

		String dir = "/Volumes/DATA/CLAS12/EtaPrimeAnalysis/Torus1.0Sol0.8/";
		List<String> aList = new ArrayList<>();
		File[] listOfFiles = new File(dir).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".hipo");
			}
		});
		for (int i = 0; i < 50; i++) {// listOfFiles.length
			if (listOfFiles[i].isFile()) {
				// System.out.println("File " + listOfFiles[i].getName());
				aList.add(dir + listOfFiles[i].getName());
			}
		}
		//
		// aList.add(dir + "out_FullDilepton_Tor1.0Sol0.8_60.hipo");
		//
		// aList.add(dir + "out_FullDilepton_Tor1.0Sol0.8_61.hipo");
		// aList.add(dir + "out_FullDilepton_Tor1.0Sol0.8_62.hipo");
		// aList.add(dir + "out_FullDilepton_Tor1.0Sol0.8_63.hipo");
		// aList.add(dir + "out_FullDilepton_Tor1.0Sol0.8_65.hipo");
		// aList.add(dir + "out_FullDilepton_Tor1.0Sol0.8_66.hipo");
		// aList.add(dir + "out_FullDilepton_Tor1.0Sol0.8_67.hipo");

		HipoDataSource reader = new HipoDataSource();

		// Get the number of events in the hipo-file
		DataEvent event = null;

		// Define a bank you want to look at:
		DataBank RecBank = null;
		// some hists
		H2F mxP_vs_mEpEmGam = new H2F("y_vs_x", 100, 0, 3.75, 100, 0, 3.75);
		H1F mxP = new H1F("mxP", 100, 0.6, 3.75);
		H1F mEpEmGam = new H1F("mEpEmGam", 100, 0.6, 3.75);
		H1F epemangle = new H1F("epemangle", 100, -1, 1);
		H1F epemGammaangle = new H1F("epemGammaangle", 100, -1, 1);

		H2F mEpEmGam_epemGammaangle = new H2F("mEpEmGam_epemGammaangle", 100, 0.6, 3.75, 100, -1, 1);
		H2F mEpEmGam_epemangle = new H2F("mEpEmGam_epemangle", 100, 0.6, 3.75, 100, -1, 1);
		H1F eptheta = new H1F("eptheta", 100, -0.02, 0.02);
		H1F epphi = new H1F("epphi", 100, -0.05, 0.05);
		H1F gammatheta = new H1F("gammatheta", 100, -0.02, 0.02);
		H1F gammaphi = new H1F("gammaphi", 100, -0.05, 0.05);

		H1F epP = new H1F("epP", 100, -0.2, 0.2);

		H1F gammaP = new H1F("gammaP", 100, -0.2, 0.2);

		H1F nGamma = new H1F("NGamma", 20, 0, 10);

		H1F mvm = new H1F("MVM", 100, -4.1, 1.1);
		String bnkName = "REC::Particle";
		// String bnkName = "MC::Particle";
		boolean onceRan = true;
		for (String string : aList) {
			System.out.println(string);
			String SAMPLE_CSV_FILE = "MLsample.csv";
			CSVPrinter csvPrinter = null;
			BufferedWriter writer = null;
			try {
				writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_FILE), Charset.forName("US-ASCII"),
						StandardOpenOption.CREATE, StandardOpenOption.APPEND);
				if (onceRan) {
					csvPrinter = new CSVPrinter(writer,
							CSVFormat.DEFAULT.withHeader("ID", "PID", "MCPID", "MCMASS", "EpEmAngle", "EmEpGamAngle",
									"InvariantMassEpEmGam", "InvariantMassEpEm", "EmPx", "EmPy", "EmPz", "EmTheta",
									"EmPhi"));
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
				if (event.hasBank(bnkName) && event.hasBank("MC::Particle")
						&& !AnalysisMethods.mcList(event.getBank("MC::Particle")).isEmpty()) {
					DataBank mCBank = event.getBank("MC::Particle");

					if (AnalysisMethods.mcList(mCBank).isEmpty()) {
						System.out.println("########## ");
						for (int h = 0; h < mCBank.rows(); h++) {
							System.out.println(AnalysisMethods.getParticle(mCBank, h).pid() + "  "
									+ AnalysisMethods.getParticle(mCBank, h).px() + "  " + event.getBank("MC::Particle")
									+ "  ZERO????   " + event.getBank("MC::Header").getInt("event", 0));
						}
					}
					// Get the bank itself, when existing:
					RecBank = event.getBank(bnkName);
					// I am interested first in pe+e-gamma
					// lets create a Stack for each
					Stack<Particle> epStack = new Stack<>();
					Stack<Particle> emStack = new Stack<>();
					Stack<Particle> gammaStack = new Stack<>();
					Stack<Particle> protonStack = new Stack<>();
					List<Particle> protonList = new ArrayList<>();

					// Now loop over all entries inside the bank:
					// =====================================================
					for (int h = 0; h < RecBank.rows(); h++) {
						if (RecBank.getInt("pid", h) == 2212) {
							protonStack.push(AnalysisMethods.getParticle(RecBank, h));
							protonList.add(AnalysisMethods.getParticle(RecBank, h));
						}
						if (RecBank.getInt("pid", h) == 11) {
							emStack.push(AnalysisMethods.getParticle(RecBank, h));
						}
						if (RecBank.getInt("pid", h) == -11) {
							epStack.push(AnalysisMethods.getParticle(RecBank, h));
						}
						if (RecBank.getInt("pid", h) == 22) {
							gammaStack.push(AnalysisMethods.getParticle(RecBank, h));
						}
					}
					// if (!gammaStack.isEmpty()) {
					nGamma.fill(gammaStack.size());
					// }
					// =====================================================
					if (epStack.size() == 1 && emStack.size() > 0 && gammaStack.size() == 1) {// &&
																								// protonStack.size()
																								// >
																								// 0
						// if (epStack.size() == 1 && emStack.size() == 2 &&
						// protonStack.size() == 1
						// && gammaStack.size() == 1) {

						// System.out.println(emStack.size() + " " +
						// epStack.size() + " " + gammaStack.size());
						for (Particle particle : protonStack) {
							for (Particle p : emStack) {
								Particle sum = AnalysisMethods.beamTargetParticle();
								sum.combine(particle, -1);
								sum.combine(p, -1);

								mxP.fill(sum.mass());
							}

						}
						for (Particle em : emStack) {
							// Particle em = emStack.get(0);
							for (Particle ep : epStack) {

								for (Particle gamma : gammaStack) {
									int signal = 0;
									String pName = "unknown";
									Particle p = new Particle();
									p.copy(em);
									p.combine(ep, +1);
									p.combine(gamma, +1);
									mEpEmGam.fill(p.mass());
									// if
									// ((AnalysisMethods.mcPID(mCBank).equals("omega")))
									// {
									// System.out.println(AnalysisMethods.mcPID(mCBank)
									// + " " + i + " "
									// +
									// event.getBank("MC::Header").getInt("event",
									// 0));
									//
									// }
									if (AnalysisMethods.matchTrack(mCBank, em)
											&& AnalysisMethods.matchTrack(mCBank, ep)) {
										mvm.fill((AnalysisMethods.mclistmass(mCBank) - p.mass()));

									}
									if (Math.abs(AnalysisMethods.mclistmass(mCBank) - p.mass()) < limit) {
										epphi.fill(ep.phi() - AnalysisMethods.matchedMCParticle(mCBank, ep).phi());
										eptheta.fill(
												ep.theta() - AnalysisMethods.matchedMCParticle(mCBank, ep).theta());

										epP.fill(ep.p() - AnalysisMethods.matchedMCParticle(mCBank, ep).p());

										gammaphi.fill(
												gamma.phi() - AnalysisMethods.matchedMCParticle(mCBank, gamma).phi());
										gammatheta.fill(gamma.theta()
												- AnalysisMethods.matchedMCParticle(mCBank, gamma).theta());
										gammaP.fill(gamma.p() - AnalysisMethods.matchedMCParticle(mCBank, gamma).p());
										// System.out.println(
										// AnalysisMethods.mclistmass(mCBank) +
										// " " + p.mass() + " " + i);
										signal = 1;
										pName = AnalysisMethods.mcPID(mCBank);

										//
										// System.out.println(
										// "MC matched at event " + i + " " +
										// AnalysisMethods.mcPID(mCBank));
										//

										// if
										// (AnalysisMethods.matchTrack(mCBank,
										// em)
										// && AnalysisMethods.matchTrack(mCBank,
										// ep)) {// &&
										// // AnalysisMethods.matchTrack(mCBank,
										// // gamma)
										// signal = 1;
										// pName =
										// AnalysisMethods.mcPID(mCBank);
										// System.out.println(
										// "MC matched at event " + i + " " +
										// AnalysisMethods.mcPID(mCBank));
										// }
									}

									// put e+e- in frame of e+e-gamma and check
									// the angle
									Particle xParticle = new Particle();
									xParticle.copy(em);
									xParticle.combine(ep, +1);
									xParticle.inFrame(p);

									epemGammaangle.fill(xParticle.cosTheta(gamma.inFrame(p)));
									mEpEmGam_epemGammaangle.fill(p.mass(), xParticle.cosTheta(gamma));
									// double epemGammaAngle =
									// xParticle.cosTheta(gamma);
									// now lets see the angle between the
									// leptons in the parent frame
									Particle emInFrame = new Particle();
									emInFrame.copy(em);
									emInFrame.inFrame(p);

									Particle epInFrame = new Particle();
									epInFrame.copy(ep);
									epInFrame.inFrame(p);

									// double epemAngle =
									// emInFrame.cosTheta(epInFrame);

									epemangle.fill(emInFrame.cosTheta(epInFrame));
									mEpEmGam_epemangle.fill(p.mass(), emInFrame.cosTheta(epInFrame));

									// record here
									// "ID", "PID", "EpEmAngle", "EmEpGamAngle",
									// "InvariantMassEpEmGam",
									// "InvariantMassEpEm", "EmPx", "EmPy",
									// "EmPz", "EmTheta", "EmPhi"
									try {
										csvPrinter.printRecord(signal,
												AnalysisMethods.getPName(p.mass(), signal, limit), pName,
												AnalysisMethods.mclistmass(mCBank), emInFrame.cosTheta(epInFrame),
												xParticle.cosTheta(gamma.inFrame(p)), xParticle.mass(), p.mass(),
												em.px(), em.py(), em.pz(), em.theta(), em.phi());
									} catch (Exception e) {
										// TODO: handle exception
									}

								}
							}
						}

					}
				}
				// =====================================================
			}
			csvPrinter.flush();

		}
		TCanvas c = new TCanvas("c", 500, 500);
		c.divide(1, 2);
		c.cd(0);
		c.draw(mxP);
		c.cd(1);
		c.draw(mEpEmGam);

		TCanvas c2 = new TCanvas("c2", 500, 500);
		c2.divide(1, 2);
		c2.cd(0);
		c2.draw(epemangle);
		c2.cd(1);
		c2.draw(epemGammaangle);

		TCanvas c3 = new TCanvas("c3", 500, 500);
		c3.divide(1, 2);
		c3.cd(0);
		c3.draw(mEpEmGam_epemangle, "colz");
		c3.cd(1);
		c3.draw(mEpEmGam_epemGammaangle, "colz");

		TCanvas c4 = new TCanvas("c4", 500, 500);
		c4.divide(1, 2);
		c4.cd(0);
		c4.draw(eptheta);
		c4.cd(1);
		c4.draw(epphi);

		TCanvas c5 = new TCanvas("c5", 500, 500);
		c5.divide(1, 2);
		c5.cd(0);
		c5.draw(gammatheta);
		c5.cd(1);
		c5.draw(gammaphi);

		TCanvas c6 = new TCanvas("c6", 500, 500);
		c6.divide(1, 2);
		c6.cd(0);
		c6.draw(gammaP);
		c6.cd(1);
		c6.draw(epP);

		TCanvas gamma = new TCanvas("mvm", 500, 500);
		gamma.draw(mvm);

	}
}
