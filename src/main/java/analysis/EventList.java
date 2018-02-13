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

import org.jlab.clas.physics.Particle;

public class EventList {

	private EventList() {
		setPID();
	}

	public static final Particle beamParticle = new Particle(11, 0.0, 0.0, 10.6, 0.0, 0.0, 0.0);
	public static final Particle targetParticle = new Particle(2212, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
	public static final Particle recoilProton = new Particle();

	public static final Particle firstElectron = new Particle();
	public static final Particle secondElectron = new Particle();
	public static final Particle dalitzPositron = new Particle();
	public static final Particle dalitzPhoton = new Particle();

	private void setPID() {
		firstElectron.changePid(11);
		secondElectron.changePid(11);
		dalitzPositron.changePid(-11);
		dalitzPhoton.changePid(22);

	}
}
