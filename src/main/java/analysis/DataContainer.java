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

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jlab.clas.physics.Particle;
import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;
import org.jlab.io.hipo.HipoDataSource;

public class DataContainer {

	private List<Container> aList;

	private HipoDataSource reader;
	private String fileName;
	private String bankName;
	private DataEvent event;
	private DataBank RecBank;

	public DataContainer(String fileName, String bankName, List<Container> aList) {
		this.aList = aList;
		this.reader = new HipoDataSource();
		this.fileName = fileName;
		this.bankName = bankName;
		runHipo();

	}

	private void runHipo() {
		reader.open(this.fileName);
		int Nevents = reader.getSize();
		for (int i = 0; i < Nevents; i++) {
			event = (DataEvent) reader.gotoEvent(i);
			if (i % 1000 == 0) {
				System.out.println("Done with " + i / Nevents + " Events");

			}
			if (event.hasBank(this.bankName)) {
				RecBank = event.getBank(this.bankName);
				List<Particle> protonList = new ArrayList<>();
				List<Particle> epList = new ArrayList<>();
				List<Particle> emList = new ArrayList<>();
				List<Particle> gammaList = new ArrayList<>();
				for (int h = 0; h < RecBank.rows(); h++) {
					if (RecBank.getInt("pid", h) == 2212) {
						protonList.add(AnalysisMethods.getParticle(RecBank, h));
					}
					if (RecBank.getInt("pid", h) == 11) {
						emList.add(AnalysisMethods.getParticle(RecBank, h));
					}
					if (RecBank.getInt("pid", h) == -11) {
						epList.add(AnalysisMethods.getParticle(RecBank, h));
					}
					if (RecBank.getInt("pid", h) == 22) {
						gammaList.add(AnalysisMethods.getParticle(RecBank, h));
					}
				}
				if (epList.size() > 0 && emList.size() > 0 && protonList.size() > 0 && gammaList.size() > 0) {
					Map<String, List<Particle>> aMap = new HashMap<>();
					aMap.put("proton", protonList);
					aMap.put("electron", emList);
					aMap.put("positron", epList);
					aMap.put("gamma", gammaList);

				}
			}
		}
	}

	public static void main(String[] args) {
		String bankName = "REC::Particle";

		String dir = "/Volumes/DATA/CLAS12/EtaPrimeAnalysis/Torus1.0Sol0.8/";

		List<Container> aList = new ArrayList<>();

		File[] listOfFiles = new File(dir).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".hipo");
			}
		});
		for (int i = 0; i < 10; i++) {// listOfFiles.length
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
				DataContainer dataContainer = new DataContainer(dir + listOfFiles[i].getName(), bankName, aList);
			}
		}
	}
}
