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

import java.io.Serializable;

public class Response implements Serializable {

	private double emPx;// for electron
	private double emPy;// for electron
	private double emPz;// for electron
	private double gammaPx;// for gamma
	private double gammaPy;// for gamma
	private double gammaPz;// for gamma
	private int pid;
	private int type; // 0 for background 1 for signal
	private double epemOpeningAngle;
	private double gammaStarGammaAngle; // in cm of epemgamma
	private double ivEpEmGam;
	private double ivEpEm;

	public Response(double emPx, double emPy, double emPz, double gammaPx, double gammaPy, double gammaPz, int pid,
			int type, double epemOpeningAngle, double gammaStarGammaAngle, double ivEpEmGam, double ivEpEm) {
		super();
		this.emPx = emPx;
		this.emPy = emPy;
		this.emPz = emPz;
		this.gammaPx = gammaPx;
		this.gammaPy = gammaPy;
		this.gammaPz = gammaPz;
		this.pid = pid;
		this.type = type;
		this.epemOpeningAngle = epemOpeningAngle;
		this.gammaStarGammaAngle = gammaStarGammaAngle;
		this.ivEpEmGam = ivEpEmGam;
		this.ivEpEm = ivEpEm;
	}

	public double getEmPx() {
		return emPx;
	}

	public void setEmPx(double emPx) {
		this.emPx = emPx;
	}

	public double getEmPy() {
		return emPy;
	}

	public void setEmPy(double emPy) {
		this.emPy = emPy;
	}

	public double getEmPz() {
		return emPz;
	}

	public void setEmPz(double emPz) {
		this.emPz = emPz;
	}

	public double getGammaPx() {
		return gammaPx;
	}

	public void setGammaPx(double gammaPx) {
		this.gammaPx = gammaPx;
	}

	public double getGammaPy() {
		return gammaPy;
	}

	public void setGammaPy(double gammaPy) {
		this.gammaPy = gammaPy;
	}

	public double getGammaPz() {
		return gammaPz;
	}

	public void setGammaPz(double gammaPz) {
		this.gammaPz = gammaPz;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getEpemOpeningAngle() {
		return epemOpeningAngle;
	}

	public void setEpemOpeningAngle(double epemOpeningAngle) {
		this.epemOpeningAngle = epemOpeningAngle;
	}

	public double getGammaStarGammaAngle() {
		return gammaStarGammaAngle;
	}

	public void setGammaStarGammaAngle(double gammaStarGammaAngle) {
		this.gammaStarGammaAngle = gammaStarGammaAngle;
	}

	public double getIvEpEmGam() {
		return ivEpEmGam;
	}

	public void setIvEpEmGam(double ivEpEmGam) {
		this.ivEpEmGam = ivEpEmGam;
	}

	public double getIvEpEm() {
		return ivEpEm;
	}

	public void setIvEpEm(double ivEpEm) {
		this.ivEpEm = ivEpEm;
	}

}
