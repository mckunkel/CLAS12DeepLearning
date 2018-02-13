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

public class LundHeader {
	private static final long serialVersionUID = 1L;
	private int numParticles;
	private int numTargetNuc;
	private int numTargetProt;
	private double targetPol;
	private double beamPol;
	private double x;
	private double y;
	private double W;
	private double q2;
	private double nu;

	public LundHeader(int numParticles, int numTargetNuc, int numTargetProt, double targetPol, double beamPol, double x,
			double y, double W, double q2, double nu) {
		this.numParticles = numParticles;
		this.numTargetNuc = numTargetNuc;
		this.numTargetProt = numTargetProt;
		this.targetPol = targetPol;
		this.beamPol = beamPol;
		this.x = x;
		this.y = y;
		this.W = W;
		this.q2 = q2;
		this.nu = nu;
	}

	@Override
	public String toString() {
		return numParticles + " " + numTargetNuc + " " + numTargetProt + " " + targetPol + " " + beamPol + " " + x + " "
				+ y + " " + W + " " + q2 + " " + nu;
	}
}
