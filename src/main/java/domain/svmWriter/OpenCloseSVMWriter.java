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

import java.util.Map;

import domain.MLObjects.MLObject;

public interface OpenCloseSVMWriter {
	public void openSVMFile();

	public void openSVMFile(String outputLundName);

	public void closeSVMFile();

	public void writeEvent(Map<MLObject, Integer> mlObjectMap);

	public void writeFlush();
}
