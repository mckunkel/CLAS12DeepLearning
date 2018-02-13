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
package domain.svmWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import domain.MLObjects.CalorimeterContainer;
import domain.MLObjects.CherenkovContainer;
import domain.MLObjects.MLObject;
import domain.MLObjects.ParticleContainer;

public class OpenCloseSVMWriterImpl implements OpenCloseSVMWriter {

	private String outputSVMName;
	private FileWriter fw = null;
	private BufferedWriter bw = null;
	private PrintWriter out = null;

	public OpenCloseSVMWriterImpl(String outputSVMName) {
		this.outputSVMName = outputSVMName;
	}

	public void openSVMFile() {
		if (new File(outputSVMName).exists()) {
			System.err.println("This Lund file already exists");
			System.err.println("Please delete it or rename the output file");
			System.exit(0);
		} else {
			try {
				fw = new FileWriter(outputSVMName, true);
				bw = new BufferedWriter(fw);
				out = new PrintWriter(bw);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void openSVMFile(String outputLundName) {
		if (new File(outputLundName).exists()) {
			System.err.println("This Lund file already exists");
			System.err.println("Please delete it or rename the output file");
			System.exit(0);
		} else {
			try {
				fw = new FileWriter(outputLundName, true);
				bw = new BufferedWriter(fw);
				out = new PrintWriter(bw);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeSVMFile() {
		try {
			out.close();
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeEvent(Map<MLObject, Integer> aMap) {
		for (Map.Entry<MLObject, Integer> entry : aMap.entrySet()) {
			MLObject key = entry.getKey();
			Integer value = entry.getValue();
			ParticleContainer pContainer = key.getpContainer();
			List<CherenkovContainer> cherenkovContainer = key.getCcList();
			List<CalorimeterContainer> calorimeterContainer = key.getEcList();

			out.write(value + " ");
			writeParticle(pContainer);
			writeCC(cherenkovContainer);
			writeEC(calorimeterContainer, pContainer.getPtot());
			out.write("\n");

		}
	}

	public void writeFlush() {
		out.flush();
	}

	private void writeParticle(ParticleContainer pContainer) {
		out.write("1:" + pContainer.getPx() + " ");
		out.write("2:" + pContainer.getPy() + " ");
		out.write("3:" + pContainer.getPz() + " ");
		out.write("4:" + pContainer.getPtot() + " ");
		out.write("5:" + pContainer.getTheta() + " ");
		out.write("6:" + pContainer.getPhi() + " ");
		out.write("7:" + pContainer.getVx() + " ");
		out.write("8:" + pContainer.getVy() + " ");
		out.write("9:" + pContainer.getVz() + " ");
	}

	private void writeCC(List<CherenkovContainer> cherenkovContainer) {
		int placer = 50;
		for (CherenkovContainer cc : cherenkovContainer) {
			out.print(placer + ":" + cc.getDetectorType() + " ");
			out.print((placer + 1) + ":" + cc.getNphe() + " ");
			out.print((placer + 2) + ":" + cc.getPhi() + " ");
			out.print((placer + 3) + ":" + cc.getTheta() + " ");
			placer += 4;
		}
	}

	private void writeEC(List<CalorimeterContainer> calorimeterContainer, double pTot) {
		int placer = 500;
		double eTot = 0.0;

		for (CalorimeterContainer ec : calorimeterContainer) {
			out.print(placer + ":" + ec.getDetectorType() + " ");
			out.print((placer + 1) + ":" + ec.getLayer() + " ");
			out.print((placer + 2) + ":" + ec.getEnergy() + " ");
			eTot = eTot + ec.getEnergy();
			placer += 3;
		}
		if (calorimeterContainer.size() > 0) {
			out.print(placer + ":" + eTot + " ");
			placer++;
			out.print(placer + ":" + eTot / pTot + " ");
		}

	}
}
