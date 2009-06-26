package examples;

import java.util.ArrayList;
import java.util.Properties;

public class MainTestTandemMismatches {

	public static void main(String[] args) {
		ArrayList<String> DNATandemMismatchesMethods = new ArrayList<String>();
		DNATandemMismatchesMethods.add("allsanpey");
		
		Properties DNATandemMismatches = MainTest.loadSequencesTest("src/melting/test/DNATandemMismatchesSequences.txt");
		
		System.out.print("\n\n Sequences \t TmExp \t allsanpey \n");

		MainTest.displayResultsWithComplementarySequence(DNATandemMismatches, DNATandemMismatchesMethods, "dnadna", "Na=1", "0.0004", "-tan");
		
		ArrayList<String> RNATandemMismatchesMethods = new ArrayList<String>();
		RNATandemMismatchesMethods.add("tur06");
		
		Properties RNATandemMismatches = MainTest.loadSequencesTest("src/melting/test/RNATandemMismatchesSequences.txt");
		
		System.out.print("\n\n Sequences \t TmExp \t tur06 \n");

		MainTest.displayResultsWithComplementarySequence(RNATandemMismatches, RNATandemMismatchesMethods, "rnarna", "Na=1", "0.0001", "-tan");
	}

}
