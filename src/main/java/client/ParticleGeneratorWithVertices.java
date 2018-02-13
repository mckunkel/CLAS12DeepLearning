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
package client;

import java.util.Random;

import org.jlab.clas.physics.Particle;
import org.jlab.clas.physics.Vector3;
import org.jlab.groot.data.H1F;
import org.jlab.groot.ui.TCanvas;

public class ParticleGeneratorWithVertices extends org.jlab.clas.physics.ParticleGenerator {

	private double targetLength = 0.0;
	private double targetCenter = 0.0;

	public ParticleGeneratorWithVertices(int pid) {
		super(pid);
	}

	public ParticleGeneratorWithVertices(int pid, double pmin, double pmax, double thmin, double thmax, double phimin,
			double phimax, double targetLength, double targetCenter) {
		super(pid, pmin, pmax, thmin, thmax, phimin, phimax);
		this.targetCenter = targetCenter;
		this.targetLength = targetLength;
	}

	public Particle getNewParticle() {
		Particle particle = new Particle();
		particle.setVector(this.getParticle().vector(), setVector3());
		particle.changePid(this.getParticle().pid());
		return particle;
	}

	public Vector3 setVector3() {
		double rand = -2.5 + new Random().nextDouble() * this.targetLength + this.targetCenter;
		return (new Vector3(0.0, 0.0, rand));
	}

	public static void main(String[] args) {
		H1F aH1f = new H1F("a", "a", 1000, 6, 13);
		for (int i = 0; i < 10; i++) {
			ParticleGeneratorWithVertices pgun = new ParticleGeneratorWithVertices(11, 0.2, 10, 4.5, 40, -180, 180, 5.0,
					2);
			aH1f.fill(pgun.getNewParticle().pid());
			System.out.println(pgun.getNewParticle().toLundString());

		}
		TCanvas canvas = new TCanvas("name", 800, 800);
		canvas.draw(aH1f);
	}

}
