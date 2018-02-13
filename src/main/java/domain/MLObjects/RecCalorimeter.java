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

public class RecCalorimeter {
	private static String bankName = "REC::Calorimeter";

	public static List<CalorimeterContainer> createCalorimeter(DataEvent aEvent, int rowIndex) {
		List<CalorimeterContainer> aList = new ArrayList<>();
		if (aEvent.hasBank(bankName)) {
			DataBank aBank = aEvent.getBank(bankName);
			int Nrows = aBank.rows();
			for (int t = 0; t < Nrows; t++) {
				int pindex = aBank.getShort("pindex", t);
				if (pindex == rowIndex) {
					CalorimeterContainer cContainer = new CalorimeterContainer();
					cContainer.setDetectorType(aBank.getByte("detector", t));
					cContainer.setLayer(aBank.getByte("layer", t));
					cContainer.setEnergy(aBank.getFloat("energy", t));
					aList.add(cContainer);
				}
			}
		}
		return new ArrayList<>(new HashSet<>(aList));
	}
}
