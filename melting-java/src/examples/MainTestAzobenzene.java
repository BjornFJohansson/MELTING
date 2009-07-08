package examples;

import java.util.ArrayList;
import java.util.Properties;

public class MainTestAzobenzene {

	public static void main(String[] args) {
		ArrayList<String> azobenzeneMethods = new ArrayList<String>();
		azobenzeneMethods.add("asa05");

		Properties azobenzeneSequences = MainTest.loadSequencesTest("src/examples/test/azobenzeneSequences.txt");
		
		System.out.print("\n\n melting.sequences \t TmExp \t asa05 \n");

		MainTest.displayResults(azobenzeneSequences, azobenzeneMethods, "dnadna", "Na=1", "0.000002", "-azo");
	}

}
