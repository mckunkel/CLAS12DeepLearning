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

public class Data {

	private double[] px;
	private double[] py;
	private double[] pz;
	private double[] p;
	private double invariantMass;

	public Data() {

	}

	public Data(double[] px, double[] py, double[] pz, double[] p, double invariantMass) {
		super();
		this.px = px;
		this.py = py;
		this.pz = pz;
		this.p = p;
		this.invariantMass = invariantMass;
	}

	public double[] getPx() {
		return px;
	}

	public void setPx(double[] px) {
		this.px = px;
	}

	public double[] getPy() {
		return py;
	}

	public void setPy(double[] py) {
		this.py = py;
	}

	public double[] getPz() {
		return pz;
	}

	public void setPz(double[] pz) {
		this.pz = pz;
	}

	public double[] getP() {
		return p;
	}

	public void setP(double[] p) {
		this.p = p;
	}

	public double getInvariantMass() {
		return invariantMass;
	}

	public void setInvariantMass(double invariantMass) {
		this.invariantMass = invariantMass;
	}

}
