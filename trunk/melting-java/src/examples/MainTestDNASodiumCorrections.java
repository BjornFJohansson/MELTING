package examples;

import java.util.ArrayList;
import java.util.Properties;

public class MainTestDNASodiumCorrections {

	public static void main(String[] args) {

		ArrayList<String> sodiumMethods = new ArrayList<String>();
		sodiumMethods.add("ahs01");
		sodiumMethods.add("kam71");
		sodiumMethods.add("marschdot");
		sodiumMethods.add("owc1904");
		sodiumMethods.add("owc2004");
		sodiumMethods.add("owc2104");
		sodiumMethods.add("owc2204");
		sodiumMethods.add("san96");
		sodiumMethods.add("san04");
		sodiumMethods.add("schlif");
		sodiumMethods.add("tanna06");
		sodiumMethods.add("wet91");

		Properties DNASequences = MainTest.loadSequencesTest("src/melting/test/DNASodiumTestValues.txt");
		System.out.print("\n\n Sequences \t TmExp \t ash01 \t kam71 \t marschdot \t owc1904 \t owc2004 \t owc2104 \t owc2204 \t san96 \t san04 \t schlif \t tanna06 \t wet91 \n");
		MainTest.displayResultsSodium(DNASequences, sodiumMethods, "dnadna", "0.000002", "-ion");
	}

}
