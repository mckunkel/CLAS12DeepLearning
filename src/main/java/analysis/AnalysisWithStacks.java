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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.jlab.clas.pdg.PDGDatabase;
import org.jlab.clas.physics.LorentzVector;
import org.jlab.clas.physics.Particle;
import org.jlab.clas.physics.Vector3;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.ui.TCanvas;
import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;
import org.jlab.io.hipo.HipoDataSource;

public class AnalysisWithStacks {

	private static int getpid(DataBank aBank, int evntIndex) {
		int pid = aBank.getInt("pid", evntIndex);
		if (pid == 0) {
			return -3312;
		}
		return pid;
	}

	private static int getCharge(DataBank aBank, int evntIndex) {
		int charge = aBank.getInt("charge", evntIndex);
		return charge;

	}

	private static Vector3 getMomentumVector(DataBank aBank, int evntIndex) {
		Vector3 momVector = new Vector3();
		momVector.setXYZ(aBank.getFloat("px", evntIndex), aBank.getFloat("py", evntIndex),
				aBank.getFloat("pz", evntIndex));
		return momVector;
	}

	private static Vector3 getVertexVector(DataBank aBank, int evntIndex) {
		Vector3 vrtVector = new Vector3();
		vrtVector.setXYZ(aBank.getFloat("vx", evntIndex), aBank.getFloat("vy", evntIndex),
				aBank.getFloat("vz", evntIndex));
		return vrtVector;
	}

	private static LorentzVector getLorentzVector(DataBank aBank, int evntIndex) {
		LorentzVector aLorentzVector = new LorentzVector();
		double mass = PDGDatabase.getParticleById(getpid(aBank, evntIndex)).mass();
		aLorentzVector.setVectM(getMomentumVector(aBank, evntIndex), mass);
		return aLorentzVector;
	}

	private static Particle getParticle(DataBank aBank, int evntIndex) {
		Particle aParticle = new Particle();
		aParticle.setVector(getLorentzVector(aBank, evntIndex), getVertexVector(aBank, evntIndex));
		aParticle.changePid(getpid(aBank, evntIndex));
		return aParticle;

	}

	private static Particle beamTargetParticle() {
		Particle sum = new Particle();
		sum.copy(EventList.beamParticle);
		sum.combine(EventList.targetParticle, +1);
		return sum;
	}

	public static void main(String[] args) {

		// First step: Read in the file you want to look at:
		// String dir = "/Volumes/FutureBoots/data/EtaPData/Torus1.0Sol0.8/";
		String dir = "/Volumes/DATA/CLAS12/EtaPrimeAnalysis/Torus1.0Sol0.8/";
		List<String> aList = new ArrayList<>();
		aList.add(dir + "out_FullDilepton_Tor1.0Sol0.8_0.hipo");
		aList.add(dir + "out_FullDilepton_Tor1.0Sol0.8_1.hipo");
		aList.add(dir + "out_FullDilepton_Tor1.0Sol0.8_2.hipo");
		aList.add(dir + "out_FullDilepton_Tor1.0Sol0.8_3.hipo");
		aList.add(dir + "out_FullDilepton_Tor1.0Sol0.8_4.hipo");

		HipoDataSource reader = new HipoDataSource();

		// Get the number of events in the hipo-file
		DataEvent event = null;

		// Define a bank you want to look at:
		DataBank RecBank = null;

		// some hists
		H2F mxP_vs_mEpEmGam = new H2F("y_vs_x", 100, 0, 3.75, 100, 0, 3.75);
		H1F mxP = new H1F("mxP", 100, 0, 3.75);
		H1F mEpEmGam = new H1F("mEpEmGam", 100, 0, 3.75);

		String bnkName = "REC::Particle";

		for (String string : aList) {
			reader.open(string);
			int Nevents = reader.getSize();

			for (int i = 0; i < Nevents; i++) {
				event = (DataEvent) reader.gotoEvent(i);

				// Each hipo-file has some banks with information
				// here is one example how to access one:
				// =====================================================
				if (event.hasBank(bnkName)) {
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
							protonStack.push(getParticle(RecBank, h));
							protonList.add(getParticle(RecBank, h));
						}
						if (RecBank.getInt("pid", h) == 11) {
							emStack.push(getParticle(RecBank, h));
						}
						if (RecBank.getInt("pid", h) == -11) {
							epStack.push(getParticle(RecBank, h));
						}
						if (RecBank.getInt("pid", h) == 22) {
							gammaStack.push(getParticle(RecBank, h));
						}
					}
					// =====================================================
					if (epStack.size() > 0 && emStack.size() > 0 && protonStack.size() > 0 && gammaStack.size() > 0) {
						for (Particle particle : protonStack) {
							for (Particle p : emStack) {
								Particle sum = beamTargetParticle();
								sum.combine(particle, -1);
								sum.combine(p, -1);

								mxP.fill(sum.mass());
							}

						}
						for (Particle em : emStack) {
							for (Particle ep : epStack) {
								for (Particle gamma : gammaStack) {
									Particle p = new Particle();
									p.copy(em);
									p.combine(ep, +1);
									p.combine(gamma, +1);
									mEpEmGam.fill(p.mass());
								}
							}
						}

					}
				}
				// =====================================================
			}
		}
		TCanvas c = new TCanvas("c", 500, 500);
		c.divide(1, 2);
		c.cd(0);
		c.draw(mxP);
		c.cd(1);
		c.draw(mEpEmGam);
	}

}
