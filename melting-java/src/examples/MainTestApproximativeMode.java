package examples;

import java.util.ArrayList;
import java.util.Properties;

public class MainTestApproximativeMode {

	public static void main(String[] args) {
		ArrayList<String> DNAmethods = new ArrayList<String>();
		DNAmethods.add("ahs01");
		DNAmethods.add("che93");
		DNAmethods.add("che93corr");
		DNAmethods.add("schdot");
		DNAmethods.add("owe69");
		DNAmethods.add("san98");
		DNAmethods.add("wetdna91");
		
		ArrayList<String> RNAmethods = new ArrayList<String>();
		RNAmethods.add("wetrna91");
		
		ArrayList<String> DNARNAmethods = new ArrayList<String>();
		DNARNAmethods.add("wetdnarna91");
		
		Properties DNANoSelfComplementarySequences = MainTest.loadSequencesTest("src/melting/test/DNANoSelfComplementary.txt");
		Properties DNASelfComplementarySequences = MainTest.loadSequencesTest("src/melting/test/DNASelfComplementary.txt");
	
		Properties RNANoSelfComplementarySequences = MainTest.loadSequencesTest("src/melting/test/RNANoSelfComplementary.txt");
		Properties RNASelfComplementarySequences = MainTest.loadSequencesTest("src/melting/test/RNASelfComplementary.txt"); 
		
		Properties DNARNASequences = MainTest.loadSequencesTest("src/melting/test/DNARNADuplexes.txt");
				
		System.out.print("\n\n Sequences \t TmExp \t ahs01 \t che93 \t che93corr \t shdot \t ow69 \t san98 \t wetdna91 \n");

		MainTest.displayResults(DNANoSelfComplementarySequences, DNAmethods, "dnadna", "Na=1", "0.0004", "-am");
		
		System.out.print("\n\n Sequences \t TmExp \t ahs01 \t che93 \t che93corr \t shdot \t ow69 \t san98 \t wetdna91 \n");

		MainTest.displayResults(DNASelfComplementarySequences, DNAmethods, "dnadna", "Na=1", "0.0001", "-am");

		System.out.print("\n\n Sequences \t TmExp \t wetrna91 \n");
		
		MainTest.displayResults(RNANoSelfComplementarySequences, RNAmethods, "rnarna", "Na=1", "0.0002", "-am");

		System.out.print("\n\n Sequences \t TmExp \t wetrna91 \n");
		
		MainTest.displayResults(RNASelfComplementarySequences, RNAmethods, "rnarna", "Na=1", "0.0001", "-am");

		System.out.print("\n\n Sequences \t TmExp \t wetrna91 \n");

		MainTest.displayResults(DNARNASequences, DNARNAmethods, "rnadna", "Na=1", "0.0001", "-am");
	}

}
