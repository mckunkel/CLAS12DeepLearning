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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jlab.clas.pdg.PDGDatabase;
import org.jlab.clas.physics.LorentzVector;
import org.jlab.clas.physics.Particle;
import org.jlab.clas.physics.Vector3;
import org.jlab.io.base.DataBank;

public class AnalysisMethods {

	private AnalysisMethods() {

	}

	public static int getpid(DataBank aBank, int evntIndex) {
		int pid = aBank.getInt("pid", evntIndex);
		if (pid == 0) {
			return -3312;
		}
		return pid;
	}

	public static int getCharge(DataBank aBank, int evntIndex) {
		int charge = aBank.getInt("charge", evntIndex);
		return charge;

	}

	public static Vector3 getMomentumVector(DataBank aBank, int evntIndex) {
		Vector3 momVector = new Vector3();
		momVector.setXYZ(aBank.getFloat("px", evntIndex), aBank.getFloat("py", evntIndex),
				aBank.getFloat("pz", evntIndex));
		return momVector;
	}

	public static Vector3 getVertexVector(DataBank aBank, int evntIndex) {
		Vector3 vrtVector = new Vector3();
		vrtVector.setXYZ(aBank.getFloat("vx", evntIndex), aBank.getFloat("vy", evntIndex),
				aBank.getFloat("vz", evntIndex));
		return vrtVector;
	}

	public static LorentzVector getLorentzVector(DataBank aBank, int evntIndex) {
		LorentzVector aLorentzVector = new LorentzVector();
		double mass = PDGDatabase.getParticleById(getpid(aBank, evntIndex)).mass();
		aLorentzVector.setVectM(getMomentumVector(aBank, evntIndex), mass);
		return aLorentzVector;
	}

	public static Particle getParticle(DataBank aBank, int evntIndex) {
		Particle aParticle = new Particle();
		aParticle.setVector(getLorentzVector(aBank, evntIndex), getVertexVector(aBank, evntIndex));
		aParticle.changePid(getpid(aBank, evntIndex));
		return aParticle;

	}

	public static Particle beamTargetParticle() {
		Particle sum = new Particle();
		sum.copy(EventList.beamParticle);
		sum.combine(EventList.targetParticle, +1);
		return sum;
	}

	public static List<Particle> mcList(DataBank aBank) {
		List<Particle> aList = new ArrayList<>();
		int epCount = 0;
		int emCount = 0;
		int gammaCount = 0;
		for (int h = 0; h < aBank.rows(); h++) {
			if (aBank.getInt("pid", h) == 11) {
				aList.add(AnalysisMethods.getParticle(aBank, h));
				emCount++;
			}
			if (aBank.getInt("pid", h) == -11) {
				aList.add(AnalysisMethods.getParticle(aBank, h));
				epCount++;
			}
			if (aBank.getInt("pid", h) == 22) {
				aList.add(AnalysisMethods.getParticle(aBank, h));
				gammaCount++;
			}
		}
		if (emCount == 2 && epCount == 1) {// && gammaCount == 1
			aList.remove(aList.size() - 1);
			return aList;
		}
		return new ArrayList<>();
	}

	public static List<Particle> mcListAll(DataBank aBank) {
		List<Particle> aList = new ArrayList<>();
		int epCount = 0;
		int emCount = 0;
		int gammaCount = 0;
		for (int h = 0; h < aBank.rows(); h++) {
			if (aBank.getInt("pid", h) == 11) {
				aList.add(AnalysisMethods.getParticle(aBank, h));
				emCount++;
			}
			if (aBank.getInt("pid", h) == -11) {
				aList.add(AnalysisMethods.getParticle(aBank, h));
				epCount++;
			}
			if (aBank.getInt("pid", h) == 22) {
				aList.add(AnalysisMethods.getParticle(aBank, h));
				gammaCount++;
			}
		}
		if (emCount == 2 && epCount == 1 && aList.get(aList.size() - 1).pid() == 11) {
			return aList;
		}
		return new ArrayList<>();
	}

	public static Map<String, List<Particle>> getMCMap(DataBank aBank) {

		List<Particle> mcList = mcListAll(aBank);

		List<Particle> dalitzElectronList = new ArrayList<>();
		List<Particle> scatteredElectronList = new ArrayList<>();
		List<Particle> dalitzPositronList = new ArrayList<>();
		List<Particle> gammaList = new ArrayList<>();

		scatteredElectronList.add(mcList.get(mcList.size() - 1));
		for (int i = 0; i < mcList.size() - 1; i++) {
			if (mcList.get(i).pid() == 11) {
				dalitzElectronList.add(mcList.get(i));
			} else if (mcList.get(i).pid() == -11) {
				dalitzPositronList.add(mcList.get(i));
			} else if (mcList.get(i).pid() == 22) {
				gammaList.add(mcList.get(i));
			}
		}

		Map<String, List<Particle>> aMap = new HashMap<>();
		aMap.put("Scattered", scatteredElectronList);
		aMap.put("DalitzEm", dalitzElectronList);
		aMap.put("DalitzEp", dalitzPositronList);
		aMap.put("Gamma", gammaList);

		return aMap;

	}

	public static String mcPID(DataBank aBank) {
		double pidMass = mclistmass(aBank);
		String retString = "";
		if (Math.abs(pidMass - 0.1349) < 0.01) {
			retString = "pi0";
		} else if (Math.abs(pidMass - 0.547) < 0.01) {
			retString = "eta";

		} else if (Math.abs(pidMass - 0.775) < 0.15) {
			retString = "omega";

		} else if (Math.abs(pidMass - 0.957) < 0.01) {
			retString = "etaP";

		} else {
			retString = "unknown";
			// System.out.println(pidMass + " ++++ unknown mass...list size" +
			// mcList(aBank).size());
			// for (Particle p : mcList(aBank)) {
			// System.out.println(p.mass() + " " + p.pid());
			// }
		}

		return retString;

	}

	public static boolean matchTrack(DataBank aBank, Particle p) {
		List<Particle> mcList = mcList(aBank);
		for (Particle particle : mcList) {
			// && Math.abs(p.p() - particle.p()) < 0.05

			if (p.pid() == particle.pid() && Math.abs(p.phi() - particle.phi()) < 0.025
					&& Math.abs(p.theta() - particle.theta()) < 0.0075) {
				return true;
			}
		}
		return false;
	}

	public static Particle matchedMCParticle(DataBank aBank, Particle p) {
		Particle pnew = new Particle();
		List<Particle> mcList = mcList(aBank);
		for (Particle particle : mcList) {
			if (p.pid() == particle.pid()) {
				pnew.copy(particle);
			}
		}
		return pnew;
	}

	public static double mclistmass(DataBank aBank) {
		List<Particle> aList = mcList(aBank);

		Particle particle = aList.get(0);
		for (int i = 1; i < aList.size(); i++) {
			particle.combine(aList.get(i), +1);
		}
		// Particle particle = new Particle(22, 0, 0, 0);
		// for (Particle p : mcList(aBank)) {
		// particle.combine(p, +1);
		// }
		return particle.mass();
	}

	public static String getPName(double mass, int signal, double limit) {
		if (signal == 1) {
			if (Math.abs(mass - 0.1349) < 0.01) {
				return "pi0";
			} else if (Math.abs(mass - 0.547) < 0.018) {
				return "eta";

			} else if (Math.abs(mass - 0.957) < 0.01) {
				return "etaPrime";

			}
			// else if (Math.abs(mass - 0.782) < 0.150) {
			// return "rho/omega";
			//
			// }
			else {
				return "Background";
			}
		}
		return "Background";
	}
}
