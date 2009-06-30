package examples;

import java.util.ArrayList;
import java.util.Properties;

public class MainTestInternalLoop {

	public static void main(String[] args) {
		ArrayList<String> RNAInternalLoopMethods = new ArrayList<String>();
		RNAInternalLoopMethods.add("tur06");
		RNAInternalLoopMethods.add("zno07");

		Properties internalLoopSequences = MainTest.loadSequencesTest("src/melting/test/RNAInternalLoopSequences.txt");
		
		System.out.print("\n\n Sequences \t TmExp \t tur06 \t zno07 \n");

		MainTest.displayResultsWithComplementarySequence(internalLoopSequences, RNAInternalLoopMethods, "rnarna", "Na=1", "0.0001", "-intLP");
	}

}
