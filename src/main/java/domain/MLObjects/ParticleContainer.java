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

public class ParticleContainer {
	private double px;
	private double py;
	private double pz;
	private double ptot;
	private double theta;
	private double phi;
	private double vx;
	private double vy;
	private double vz;

	public ParticleContainer() {

	}

	public double getPx() {
		return px;
	}

	public void setPx(double px) {
		this.px = px;
	}

	public double getPy() {
		return py;
	}

	public void setPy(double py) {
		this.py = py;
	}

	public double getPz() {
		return pz;
	}

	public void setPz(double pz) {
		this.pz = pz;
	}

	public double getPtot() {
		return ptot;
	}

	public void setPtot(double ptot) {
		this.ptot = ptot;
	}

	public double getTheta() {
		return theta;
	}

	public void setTheta(double theta) {
		this.theta = theta;
	}

	public double getPhi() {
		return phi;
	}

	public void setPhi(double phi) {
		this.phi = phi;
	}

	public double getVx() {
		return vx;
	}

	public void setVx(double vx) {
		this.vx = vx;
	}

	public double getVy() {
		return vy;
	}

	public void setVy(double vy) {
		this.vy = vy;
	}

	public double getVz() {
		return vz;
	}

	public void setVz(double vz) {
		this.vz = vz;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(phi);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(ptot);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(px);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(py);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(pz);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(theta);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(vx);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(vy);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(vz);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParticleContainer other = (ParticleContainer) obj;
		if (Double.doubleToLongBits(phi) != Double.doubleToLongBits(other.phi))
			return false;
		if (Double.doubleToLongBits(ptot) != Double.doubleToLongBits(other.ptot))
			return false;
		if (Double.doubleToLongBits(px) != Double.doubleToLongBits(other.px))
			return false;
		if (Double.doubleToLongBits(py) != Double.doubleToLongBits(other.py))
			return false;
		if (Double.doubleToLongBits(pz) != Double.doubleToLongBits(other.pz))
			return false;
		if (Double.doubleToLongBits(theta) != Double.doubleToLongBits(other.theta))
			return false;
		if (Double.doubleToLongBits(vx) != Double.doubleToLongBits(other.vx))
			return false;
		if (Double.doubleToLongBits(vy) != Double.doubleToLongBits(other.vy))
			return false;
		if (Double.doubleToLongBits(vz) != Double.doubleToLongBits(other.vz))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ParticleContainer [px=" + px + ", py=" + py + ", pz=" + pz + "]";
	}

}
