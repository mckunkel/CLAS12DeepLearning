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
package domain.MLObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jlab.clas.pdg.PDGDatabase;
import org.jlab.clas.physics.LorentzVector;
import org.jlab.clas.physics.Particle;
import org.jlab.clas.physics.Vector3;
import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

public class RecParticle {

	private static String bankName = "REC::Particle";
	private static String tbHitsBankName = "TimeBasedTrkg::TBTracks";

	public RecParticle() {
	}

	public static Map<MLObject, Integer> skimBank(DataEvent aEvent, int pid) {
		Map<MLObject, Integer> pMap = new HashMap<>();
		List<Particle> tbHitsList = fillParticleTBList(aEvent, tbHitsBankName);

		if (aEvent.hasBank(bankName)) {
			DataBank aBank = aEvent.getBank(bankName);
			int Nrows = aBank.rows();
			for (int t = 0; t < Nrows; t++) {
				if (getPID(aBank, t) != 0) {
					MLObject mlObject = new MLObject();
					mlObject.setpContainer(createMLParticle(aBank, t));
					mlObject.setEcList(RecCalorimeter.createCalorimeter(aEvent, t));
					mlObject.setCcList(RecCherenkov.createCherenkov(aEvent, t));
					if (checkNeutral(pid)) {
						if (mlObject.getEcList().size() > 0) {
							pMap.put(mlObject, getPID(aBank, t));
						}
					} else {
						if (compareParticletoTBBank(mlObject.getpContainer(), tbHitsList)) {
							pMap.put(mlObject, getPID(aBank, t));
						}
					}
				}

			}
		}
		return pMap;
	}

	public static Map<MLObject, Integer> skimBankExclusive(DataEvent aEvent, int pid) {
		Map<MLObject, Integer> pMap = new HashMap<>();
		List<Particle> tbHitsList = fillParticleTBList(aEvent, tbHitsBankName);

		if (aEvent.hasBank(bankName)) {
			DataBank aBank = aEvent.getBank(bankName);
			int Nrows = aBank.rows();
			for (int t = 0; t < Nrows; t++) {
				if (getPID(aBank, t) == pid) {
					MLObject mlObject = new MLObject();
					mlObject.setpContainer(createMLParticle(aBank, t));
					mlObject.setEcList(RecCalorimeter.createCalorimeter(aEvent, t));
					mlObject.setCcList(RecCherenkov.createCherenkov(aEvent, t));
					if (checkNeutral(pid)) {
						if (mlObject.getEcList().size() > 0) {
							pMap.put(mlObject, getPID(aBank, t));
						}
					} else {
						if (compareParticletoTBBank(mlObject.getpContainer(), tbHitsList)) {
							pMap.put(mlObject, getPID(aBank, t));
						}
					}
				}

			}
		}
		return pMap;
	}

	public static Map<MLObject, Integer> skimBankIDCrisis(DataEvent aEvent, int pid) {
		Map<MLObject, Integer> pMap = new HashMap<>();
		List<Particle> tbHitsList = fillParticleTBList(aEvent, tbHitsBankName);

		if (aEvent.hasBank(bankName)) {
			DataBank aBank = aEvent.getBank(bankName);
			int Nrows = aBank.rows();
			for (int t = 0; t < Nrows; t++) {
				if (getPID(aBank, t) != 0) {
					MLObject mlObject = new MLObject();
					mlObject.setpContainer(createMLParticle(aBank, t));
					mlObject.setEcList(RecCalorimeter.createCalorimeter(aEvent, t));
					mlObject.setCcList(RecCherenkov.createCherenkov(aEvent, t));
					if (checkNeutral(pid)) {
						if (mlObject.getEcList().size() > 0) {
							pMap.put(mlObject, getPID(aBank, t));
						}
					} else {
						if (compareParticletoTBBank(mlObject.getpContainer(), tbHitsList)) {
							pMap.put(mlObject, getPID(aBank, t));
						}
					}
				}

			}
		}
		return pMap;
	}

	private static ParticleContainer createMLParticle(DataBank aBank, int evntIndex) {

		ParticleContainer pContainer = new ParticleContainer();
		pContainer.setPx(aBank.getFloat("px", evntIndex));
		pContainer.setPy(aBank.getFloat("py", evntIndex));
		pContainer.setPz(aBank.getFloat("pz", evntIndex));
		double ptot = Math.sqrt(
				Math.pow(pContainer.getPx(), 2) + Math.pow(pContainer.getPy(), 2) + Math.pow(pContainer.getPz(), 2));

		pContainer.setPtot(ptot);
		double theta = Math.acos(pContainer.getPz() / ptot);
		double phi = Math.atan2(pContainer.getPy(), pContainer.getPx());

		pContainer.setTheta(theta);
		pContainer.setPhi(phi);
		pContainer.setVx(aBank.getFloat("vx", evntIndex));
		pContainer.setVy(aBank.getFloat("vy", evntIndex));
		pContainer.setVz(aBank.getFloat("vz", evntIndex));

		return pContainer;
	}

	private static int getPID(DataBank aBank, int evntIndex) {
		return aBank.getInt("pid", evntIndex);
	}

	private static List<Particle> fillParticleTBList(DataEvent aEvent, String bankName) {
		List<Particle> aList = new ArrayList<>();
		if (aEvent.hasBank(bankName)) {
			DataBank aBank = aEvent.getBank(bankName);
			int Nrows = aBank.rows();
			for (int t = 0; t < Nrows; t++) {
				aList.add(getTBParticle(aBank, t));
			}
		}
		return aList;
	}

	private static Particle getTBParticle(DataBank aBank, int evntIndex) {
		Particle aParticle = new Particle();
		LorentzVector aLorentzVector = new LorentzVector();
		Vector3 momVector = new Vector3();
		momVector.setXYZ(aBank.getFloat("p0_x", evntIndex), aBank.getFloat("p0_y", evntIndex),
				aBank.getFloat("p0_z", evntIndex));
		double mass = PDGDatabase.getParticleMass(11);

		aLorentzVector.setVectM(momVector, mass);

		Vector3 vrtVector = new Vector3();
		vrtVector.setXYZ(aBank.getFloat("Vtx0_x", evntIndex), aBank.getFloat("Vtx0_y", evntIndex),
				aBank.getFloat("Vtx0_z", evntIndex));

		aParticle.setVector(aLorentzVector, vrtVector);
		aParticle.changePid(11);
		return aParticle;

	}

	private static boolean compareParticletoTBBank(ParticleContainer pContainer, List<Particle> tbHitsList) {
		for (Particle tbPart : tbHitsList) {
			if (Math.abs(pContainer.getPx() - tbPart.px()) < 0.01 && Math.abs(pContainer.getPy() - tbPart.py()) < 0.01
					&& Math.abs(pContainer.getPz() - tbPart.pz()) < 0.01) {
				return true;
			}
		}

		return false;
	}

	private static boolean checkNeutral(int particleID) {
		if (PDGDatabase.getParticleById(particleID).charge() == 0) {
			return true;
		} else
			return false;
	}
}
