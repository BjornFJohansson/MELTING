package examples;

import java.util.ArrayList;
import java.util.Properties;

public class MainTestSingleMismatches {

	public static void main(String[] args) {
		ArrayList<String> DNASingleMismatchesMethods = new ArrayList<String>();
		DNASingleMismatchesMethods.add("allsanpey");
		
		Properties DNASingleMismatches = MainTest.loadSequencesTest("src/melting/test/DNASingleMismatchesSequences.txt");
		
		System.out.print("\n\n Sequences \t TmExp \t allsanpey \n");

		MainTest.displayResultsWithComplementarySequence(DNASingleMismatches, DNASingleMismatchesMethods, "dnadna", "Na=1", "0.0004", "-sinMM");
		
		ArrayList<String> RNASingleMismatchesMethods1 = new ArrayList<String>();
		RNASingleMismatchesMethods1.add("zno07");
		RNASingleMismatchesMethods1.add("tur06");
		
		ArrayList<String> RNASingleMismatchesMethods2 = new ArrayList<String>();
		RNASingleMismatchesMethods2.add("zno07");
		RNASingleMismatchesMethods2.add("zno08");
		RNASingleMismatchesMethods2.add("tur06");
		
		Properties RNASingleMismatches1 = MainTest.loadSequencesTest("src/melting/test/RNASingleMismatchesSequences1.txt");
		Properties RNASingleMismatches2 = MainTest.loadSequencesTest("src/melting/test/RNASingleMismatchesSequences2.txt");

		System.out.print("\n\n Sequences \t TmExp \t zno07 \t tur06 \n");

		MainTest.displayResultsWithComplementarySequence(RNASingleMismatches1, RNASingleMismatchesMethods1, "rnarna", "Na=1", "0.0001", "-sinMM");
		
		System.out.print("\n\n Sequences \t TmExp \t zno07 \t zno08 \t tur06 \n");

		MainTest.displayResultsWithComplementarySequence(RNASingleMismatches2, RNASingleMismatchesMethods2, "rnarna", "Na=1", "0.0001", "-sinMM");

	}

}
