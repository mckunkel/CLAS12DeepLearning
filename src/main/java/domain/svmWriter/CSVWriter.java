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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVWriter {

	private String outFile;
	private CSVPrinter csvPrinter;
	private String header;
	private BufferedWriter writer;

	public CSVWriter(String outFile, String... strings) {
		this.outFile = outFile;
		createHeader(strings);
		System.out.println(header);
		try {
			createWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createWriter() throws IOException {
		try {
			this.writer = Files.newBufferedWriter(Paths.get(outFile));
			this.csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(header));
		} finally {
			System.out.println("If you got this, you messed up");
		}
	}

	private void createHeader(String... strings) {
		StringBuilder result = new StringBuilder();
		for (String string : strings) {
			// result.append("\"");

			result.append(string);
			// result.append("\"");

			result.append(",");
		}
		this.header = result.length() > 0 ? result.substring(0, result.length() - 1) : "";
	}

	public void record(String... x) {
		StringBuilder result = new StringBuilder();
		for (String string : x) {
			// result.append("\"");

			result.append(string);
			// result.append("\"");

			result.append(",");
		}
		String finalResult = result.length() > 0 ? result.substring(0, result.length() - 1) : "";
		System.out.println(finalResult);
		try {
			this.csvPrinter.printRecord(finalResult);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void flushWriter() {
		try {
			csvPrinter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {

		CSVWriter csvWriter = new CSVWriter("sample.csv", "test1", "test2", "test3", "test4");
		csvWriter.record("we", "are", "there", "now");
		csvWriter.record("are", "we", "there", "yet");
		csvWriter.record("we", "are", "not", "there");
		csvWriter.flushWriter();

	}
}
