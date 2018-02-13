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

public class CalorimeterContainer {

	private int detectorType;
	private int layer;
	private double energy;

	public int getDetectorType() {
		return detectorType;
	}

	public void setDetectorType(int detectorType) {
		this.detectorType = detectorType;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + detectorType;
		long temp;
		temp = Double.doubleToLongBits(energy);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + layer;
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
		CalorimeterContainer other = (CalorimeterContainer) obj;
		if (detectorType != other.detectorType)
			return false;
		if (Double.doubleToLongBits(energy) != Double.doubleToLongBits(other.energy))
			return false;
		if (layer != other.layer)
			return false;
		return true;
	}

}
