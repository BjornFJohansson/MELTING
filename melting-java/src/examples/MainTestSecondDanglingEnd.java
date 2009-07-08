package examples;

import java.util.ArrayList;
import java.util.Properties;

public class MainTestSecondDanglingEnd {

	public static void main(String[] args) {
		ArrayList<String> secondDanglingEndMethods = new ArrayList<String>();
		secondDanglingEndMethods.add("ser05");
		secondDanglingEndMethods.add("ser06");

		Properties secondDanglingEnd = MainTest.loadSequencesTest("src/melting/test/RNASecondDanglingEndSequences.txt");
		
		System.out.print("\n\n melting.sequences \t TmExp \t ser05 \t ser06 \n");

		MainTest.displayResults(secondDanglingEnd, secondDanglingEndMethods, "rnarna", "Na=1", "0.0001", "-secDE");
	}

}
