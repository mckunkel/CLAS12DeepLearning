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
package domain.lundWriter;

public interface OpenCloseLundFile {
	public void openLundFile();

	public void openLundFile(String outputLundName);

	public void closeLundFile();

	public void writeEvent(LundHeader lHeader);

	public void writeEvent(String lPart);

	public void writeFlush();
}
