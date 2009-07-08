package examples;

import java.util.ArrayList;
import java.util.Properties;

public class MainTestInosineBasePairs {

	public static void main(String[] args) {
		ArrayList<String> DNAInosineMethods = new ArrayList<String>();
		DNAInosineMethods.add("san05");
		
		Properties DNAInosineSequences = MainTest.loadSequencesTest("src/melting/test/DNAInosineSequences.txt");
		
		System.out.print("\n\n melting.sequences \t TmExp \t san05 \n");

		MainTest.displayResultsWithComplementarySequence(DNAInosineSequences, DNAInosineMethods, "dnadna", "Na=1", "0.0001", "-ino");
		
		ArrayList<String> RNAInosineMethods = new ArrayList<String>();
		RNAInosineMethods.add("zno07");
		
		Properties RNAInosineSequences = MainTest.loadSequencesTest("src/melting/test/RNAInosineSequences.txt");
		
		System.out.print("\n\n melting.sequences \t TmExp \t zno07 \n");

		MainTest.displayResultsWithComplementarySequence(RNAInosineSequences, RNAInosineMethods, "rnarna", "Na=1", "0.0001", "-ino");
		
	}

}
