package examples;

import java.util.ArrayList;
import java.util.Properties;

public class MainTestLongDanglingEnd {

	public static void main(String[] args) {

		ArrayList<String> DNALongDanglingEndMethods = new ArrayList<String>();
		DNALongDanglingEndMethods.add("sugdna02");

		Properties DNALongDanglingEnd = MainTest.loadSequencesTest("src/melting/test/DNALongDanglingEndSequences.txt");
		
		System.out.print("\n\n melting.sequences \t TmExp \t sugdna02 \n");

		MainTest.displayResultsLongDanglingEnd(DNALongDanglingEnd, DNALongDanglingEndMethods, "dnadna", "Na=1", "0.0001", "-lonDE");
		
		ArrayList<String> RNALongDanglingEndMethods = new ArrayList<String>();
		RNALongDanglingEndMethods.add("sugrna02");

		Properties RNALongDanglingEnd = MainTest.loadSequencesTest("src/melting/test/RNALongDanglingEndSequences.txt");
		
		System.out.print("\n\n melting.sequences \t TmExp \t sugrna02 \n");

		MainTest.displayResultsLongDanglingEnd(RNALongDanglingEnd, RNALongDanglingEndMethods, "rnarna", "Na=1", "0.0001", "-lonDE");
	}

}
