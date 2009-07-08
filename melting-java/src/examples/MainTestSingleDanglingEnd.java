package examples;

import java.util.ArrayList;
import java.util.Properties;

public class MainTestSingleDanglingEnd {

	public static void main(String[] args) {
		ArrayList<String> DNASingleDanglingEndMethods = new ArrayList<String>();
		DNASingleDanglingEndMethods.add("bom00");
		
		Properties DNASingleDanglingEnd = MainTest.loadSequencesTest("src/examples/test/DNASingleDanglingEndSequences.txt");
		
		System.out.print("\n\n melting.sequences \t TmExp \t bom00 \n");

		MainTest.displayResults(DNASingleDanglingEnd, DNASingleDanglingEndMethods, "dnadna", "Na=1", "0.0001", "-sinDE");
		
		ArrayList<String> RNASingleDanglingEndMethods = new ArrayList<String>();
		RNASingleDanglingEndMethods.add("ser08");
		
		Properties RNASingleDanglingEnd = MainTest.loadSequencesTest("src/examples/test/RNASingleDanglingEndSequences.txt");

		System.out.print("\n\n melting.sequences \t TmExp \t ser08 \n");

		MainTest.displayResults(RNASingleDanglingEnd, RNASingleDanglingEndMethods, "rnarna", "Na=1", "0.0001", "-sinDE");

	}

}
