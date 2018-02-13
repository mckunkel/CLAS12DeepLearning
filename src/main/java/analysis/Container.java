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

import java.util.List;
import java.util.Map;

import org.jlab.clas.physics.Particle;

public class Container {

	private Map<String, List<Particle>> aMap;
	private String fileName;
	private int eventNumber;

	public Container(String fileName, int eventNumber, Map<String, List<Particle>> aMap) {
		this.aMap = aMap;
		this.fileName = fileName;
		this.eventNumber = eventNumber;
	}

	public Map<String, List<Particle>> getaMap() {
		return aMap;
	}

	public void setaMap(Map<String, List<Particle>> aMap) {
		this.aMap = aMap;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getEventNumber() {
		return eventNumber;
	}

	public void setEventNumber(int eventNumber) {
		this.eventNumber = eventNumber;
	}

}
