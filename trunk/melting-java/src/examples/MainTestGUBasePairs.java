package examples;

import java.util.ArrayList;
import java.util.Properties;

public class MainTestGUBasePairs {

	public static void main(String[] args) {
		ArrayList<String> GUmethods = new ArrayList<String>();
		GUmethods.add("tur99");
		
		Properties GUSequences = MainTest.loadSequencesTest("src/melting/test/RNAGUSequences.txt");
		
		System.out.print("\n\n Sequences \t TmExp \t tur99 \n");

		MainTest.displayResultsWithComplementarySequence(GUSequences, GUmethods, "rnarna", "Na=1", "0.0001", "-GU");
		
	}

}
