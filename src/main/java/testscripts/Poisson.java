package testscripts;

import java.util.Random;

import org.jlab.groot.data.H1F;
import org.jlab.groot.ui.TCanvas;

public class Poisson {
	private static Random random = new Random();

	public static final int poisson(double a) {
		double limit = Math.exp(-a), prod = random.nextDouble();
		int n;
		for (n = 0; prod >= limit; n++)
			prod *= random.nextDouble();
		return n;
	}

	public static int getPoissonRandom(double mean) {
		Random r = new Random();
		double L = Math.exp(-mean);
		int k = 0;
		double p = 1.0;
		do {
			p = p * r.nextDouble();
			k++;
		} while (p > L);
		return k - 1;
	}

	public static double poissonRandomNumber(double lambda) {
		double L = Math.exp(-lambda);
		double k = 0;
		double p = 1;
		do {
			k = k + 1;
			double u = Math.random();
			p = p * u;
		} while (p > L);
		return k - 1.;
	}

	public static double landauTest(double x) {
		double ex = Math.exp(-x);
		double factor = -(x + ex) / 2.0;
		return Math.exp(factor) / Math.sqrt(2.0 * Math.PI);

	}

	public static void main(String[] args) {
		TCanvas canvas = new TCanvas("canvas", 500, 500);
		H1F dist = new H1F("dist", 20, 0, 10);
		H1F dist2 = new H1F("dist2", 20, 0, 10);
		H1F dist3 = new H1F("dist3", 20, 0, 10);

		for (int i = 0; i < 100000; i++) {
			dist.fill(poisson(1));
			dist2.fill(poisson(2));
			dist3.fill(Math.round(Math.exp(-random.nextInt(6))));
			System.out.println(Math.round(Math.exp(-random.nextInt())));
		}

		canvas.draw(dist);
		dist2.setLineColor(3);
		canvas.draw(dist2, "same");

		H1F added = dist.histClone("added");

		added.add(dist2);
		added.setLineColor(5);
		canvas.draw(added, "same");
		dist3.setLineColor(2);
		canvas.draw(dist3);

	}

}
