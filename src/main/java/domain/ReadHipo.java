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
package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;
import org.jlab.io.hipo.HipoDataSource;

import domain.utils.MutableInt;

public class ReadHipo {
	private HipoDataSource hipoReader = null;
	private String[] fileList = null;
	private String recBankName;
	private int NinputFiles = 0;

	private Map<Integer, MutableInt> countMap;
	private List<Integer> eventList;

	public ReadHipo(String[] fileList) {
		this.fileList = fileList;
		this.NinputFiles = fileList.length;

		init();
	}

	private void init() {
		this.countMap = new HashMap<>();
		this.eventList = new ArrayList<>();
		this.recBankName = "REC::Particle";

	}

	public void run() {
		for (int i = 0; i < NinputFiles; i++) {
			System.out.println("operating on file " + this.fileList[i]);
			this.hipoReader = new HipoDataSource();
			this.hipoReader.open(this.fileList[i]);
			readHipo();
		}
	}

	private void readHipo() {

		for (int evnt = 1; evnt < getNEvents(); evnt++) {// getNEvents()
			DataEvent event = (DataEvent) this.hipoReader.gotoEvent(evnt);
			countPID(11, event, this.recBankName, evnt);

		}

	}

	private int getNEvents() {
		return this.hipoReader.getSize();
	}

	private void countPID(int pid, DataEvent aEvent, String bankName, int evnt) {
		if (aEvent.hasBank(bankName)) {
			DataBank aBank = aEvent.getBank(bankName);
			int Nrows = aBank.rows();
			for (int t = 0; t < Nrows - 1; t++) {
				addPID(aBank, t, evnt);
			}
		}
	}

	private void addPID(DataBank aBank, int evntIndex, int evnt) {
		int pid = aBank.getInt("pid", evntIndex);
		if (pid != 0) {
			countMap.putIfAbsent(pid, new MutableInt());
			countMap.get(pid).increment();
			eventList.add(evnt);
		}

	}

	/**
	 * @return the countMap
	 */
	public Map<Integer, MutableInt> getCountMap() {
		return countMap;
	}

	/**
	 * @return the eventList
	 */
	public List<Integer> getEventList() {
		return eventList;
	}

	public static void main(String[] args) {
		String dirName = "/Volumes/DATA/CLAS12/MachineLearning/ReconstructedFiles/Electron/Torus-0.75Sol0.8/";
		String part1Name = "out_Electron_Tor-0.75Sol0.8_1.hipo";
		List<String> aList = new ArrayList<>();
		aList.add(dirName + part1Name);
		String[] array = aList.toArray(new String[0]);

		ReadHipo readHipo = new ReadHipo(array);
		readHipo.run();
		int events = readHipo.getCountMap().get(11).get();
		System.out.println(events + " counts of 11 " + (double) ((double) events / 10000.0));

		for (Integer it1 : readHipo.getEventList()) {
			System.out.println(it1);
		}

	}
}
