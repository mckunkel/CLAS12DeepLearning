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
import java.util.HashSet;
import java.util.List;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

public class RecCherenkov {
	private static String bankName = "REC::Cherenkov";

	public static List<CherenkovContainer> createCherenkov(DataEvent aEvent, int rowIndex) {
		List<CherenkovContainer> aList = new ArrayList<>();
		if (aEvent.hasBank(bankName)) {
			DataBank aBank = aEvent.getBank(bankName);
			int Nrows = aBank.rows();
			for (int t = 0; t < Nrows; t++) {
				int pindex = aBank.getShort("pindex", t);
				if (pindex == rowIndex) {
					CherenkovContainer cContainer = new CherenkovContainer();
					cContainer.setDetectorType(aBank.getByte("detector", t));
					cContainer.setNphe(aBank.getShort("nphe", t));
					cContainer.setPhi(aBank.getFloat("phi", t));
					cContainer.setTheta(aBank.getFloat("theta", t));
					aList.add(cContainer);
				}
			}
		}
		return new ArrayList<>(new HashSet<>(aList));
	}
}
