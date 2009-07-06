package examples;

import java.util.ArrayList;
import java.util.Properties;

public class MainTestSingleBulgeLoops {

	public static void main(String[] args) {
		ArrayList<String> DNASingleBulgeMethods = new ArrayList<String>();
		DNASingleBulgeMethods.add("san04");
		DNASingleBulgeMethods.add("tan04");

		Properties DNAsingleBulgeLoop = MainTest.loadSequencesTest("src/melting/test/DNASingleBulgeLoopSequences.txt");
		
		System.out.print("\n\n Sequences \t TmExp \t san04 \t tan04 \n");

		MainTest.displayResultsWithComplementarySequence(DNAsingleBulgeLoop, DNASingleBulgeMethods, "dnadna", "Na=1", "0.0001", "-sinBU");
		
		ArrayList<String> RNASingleBulgeMethods = new ArrayList<String>();
		RNASingleBulgeMethods.add("ser07");
		RNASingleBulgeMethods.add("tur06");

		Properties RNAsingleBulgeLoop = MainTest.loadSequencesTest("src/melting/test/RNASingleBulgeLoopSequences.txt");
		
		System.out.print("\n\n Sequences \t TmExp \t ser07 \t tur06 \n");

		MainTest.displayResultsWithComplementarySequence(RNAsingleBulgeLoop, RNASingleBulgeMethods, "rnarna", "Na=1", "0.0001", "-sinBU");
	}

}