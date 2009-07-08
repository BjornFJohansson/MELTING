package examples;

import java.util.ArrayList;
import java.util.Properties;

public class MainTestHydroxyadenine {

	public static void main(String[] args) {
		ArrayList<String> hydroxyadenineMethods = new ArrayList<String>();
		hydroxyadenineMethods.add("sug01");

		Properties hydroxyadenineSequences = MainTest.loadSequencesTest("src/melting/test/hydroxyadenineSequences.txt");
		
		System.out.print("\n\n melting.sequences \t TmExp \t sug01 \n");

		MainTest.displayResultsWithComplementarySequence(hydroxyadenineSequences, hydroxyadenineMethods, "dnadna", "Na=1", "0.0001", "-ha");
	}

}
