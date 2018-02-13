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

import java.util.List;

public class MLObject {
	private ParticleContainer pContainer = null;
	private List<CherenkovContainer> ccList = null;
	private List<CalorimeterContainer> ecList = null;

	public MLObject() {
	}

	public ParticleContainer getpContainer() {
		return pContainer;
	}

	public void setpContainer(ParticleContainer pContainer) {
		this.pContainer = pContainer;
	}

	public List<CherenkovContainer> getCcList() {
		return ccList;
	}

	public void setCcList(List<CherenkovContainer> ccList) {
		this.ccList = ccList;
	}

	public List<CalorimeterContainer> getEcList() {
		return ecList;
	}

	public void setEcList(List<CalorimeterContainer> ecList) {
		this.ecList = ecList;
	}

}
