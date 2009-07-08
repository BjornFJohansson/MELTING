package examples;

import java.util.ArrayList;
import java.util.Properties;

public class MainTestLockedAcidNucleic {

	public static void main(String[] args) {
		ArrayList<String> lockedAcidNucleicMethods = new ArrayList<String>();
		lockedAcidNucleicMethods.add("mct04");

		Properties lockedAcidNucleicSequences = MainTest.loadSequencesTest("src/examples/test/lockedAcidNucleicSequences.txt");
		
		System.out.print("\n\n melting.sequences \t TmExp \t mct04 \n");

		MainTest.displayResults(lockedAcidNucleicSequences, lockedAcidNucleicMethods, "dnadna", "Na=1", "0.000005", "-lck");
	}

}
