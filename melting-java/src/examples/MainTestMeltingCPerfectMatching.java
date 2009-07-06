package examples;

import java.util.ArrayList;
import java.util.Properties;

public class MainTestMeltingCPerfectMatching {

	public static void main(String[] args) {
		ArrayList<String> DNAmethods = new ArrayList<String>();
		DNAmethods.add("all97a.nn");
		DNAmethods.add("bre86a.nn");
		DNAmethods.add("san96a.nn");
		DNAmethods.add("sug96a.nn");
		
		ArrayList<String> RNAmethods = new ArrayList<String>();
		RNAmethods.add("fre86a.nn");
		RNAmethods.add("xia98a.nn");
		
		ArrayList<String> DNARNAmethods = new ArrayList<String>();
		DNARNAmethods.add("sug95a.nn");

		Properties DNANoSelfComplementarySequences = MainTest.loadSequencesTest("src/melting/test/DNANoSelfComplementary.txt");
		Properties DNASelfComplementarySequences = MainTest.loadSequencesTest("src/melting/test/DNASelfComplementary.txt");
	
		Properties RNANoSelfComplementarySequences = MainTest.loadSequencesTest("src/melting/test/RNANoSelfComplementary.txt");
		Properties RNASelfComplementarySequences = MainTest.loadSequencesTest("src/melting/test/RNASelfComplementary.txt"); 
		
		Properties DNARNASequences = MainTest.loadSequencesTest("src/melting/test/DNARNADuplexes.txt");
				
		System.out.print("\n\n Sequences \t TmExp \t all97a.nn \t bre86a.nn \t san96a.nn \t sug96a.nn \n");

		MainTest.displayResultsMeltingC(DNANoSelfComplementarySequences, DNAmethods, "dnadna", "1", "0.0004");
		
		System.out.print("\n\n Sequences \t TmExp \t all97a.nn \t bre86a.nn \t san96a.nn \t sug96a.nn \n");

		MainTest.displayResultsMeltingCSelfComplementary(DNASelfComplementarySequences, DNAmethods, "dnadna", "1", "0.0001");

		System.out.print("\n\n Sequences \t TmExp \t fre86a.nn \t xia98a.nn \n");
		
		MainTest.displayResultsMeltingC(RNANoSelfComplementarySequences, RNAmethods, "rnarna", "1", "0.0002");

		System.out.print("\n\n Sequences \t TmExp \t fre86a.nn \t xia98a.nn \n");
		
		MainTest.displayResultsMeltingCSelfComplementary(RNASelfComplementarySequences, RNAmethods, "rnarna", "1", "0.0001");

		System.out.print("\n\n Sequences \t TmExp \t sug95a.nn \n");

		MainTest.displayResultsMeltingC(DNARNASequences, DNARNAmethods, "rnadna", "1", "0.0001");

	}

}