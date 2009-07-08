package examples;

import java.util.ArrayList;
import java.util.Properties;

public class MainTestCNGSequences {

	public static void main(String[] args) {

		ArrayList<String> CNGmethods = new ArrayList<String>();
		CNGmethods.add("bro05");
		
		Properties CNGSequences = MainTest.loadSequencesTest("src/examples/test/CNGSequences.txt");
		
		System.out.print("\n\n melting.sequences \t TmExp \t bro05 \n");

		MainTest.displayResultsSelf(CNGSequences, CNGmethods, "rnarna", "Na=1", "0.0001", "-CNG");
	}

}
