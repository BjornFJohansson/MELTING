package examples;

import java.util.ArrayList;
import java.util.Properties;


public class MainTestPerfectMatching{

	public static void main(String[] args) {
		
		ArrayList<String> DNAmethods = new ArrayList<String>();
		DNAmethods.add("all97");
		DNAmethods.add("bre86");
		DNAmethods.add("san04");
		DNAmethods.add("san96");
		DNAmethods.add("sug96");
		DNAmethods.add("tan04");
		
		ArrayList<String> RNAmethods = new ArrayList<String>();
		RNAmethods.add("fre86");
		RNAmethods.add("xia98");
		
		ArrayList<String> DNARNAmethods = new ArrayList<String>();
		DNARNAmethods.add("sug95");
		
		ArrayList<String> mRNAmethods = new ArrayList<String>();
		mRNAmethods.add("tur06");

		Properties DNANoSelfComplementarySequences = MainTest.loadSequencesTest("src/melting/test/DNANoSelfComplementary.txt");
		Properties DNASelfComplementarySequences = MainTest.loadSequencesTest("src/melting/test/DNASelfComplementary.txt");
	
		Properties RNANoSelfComplementarySequences = MainTest.loadSequencesTest("src/melting/test/RNANoSelfComplementary.txt");
		Properties RNASelfComplementarySequences = MainTest.loadSequencesTest("src/melting/test/RNASelfComplementary.txt"); 
		
		Properties DNARNASequences = MainTest.loadSequencesTest("src/melting/test/DNARNADuplexes.txt");
		
		Properties mRNASequences = MainTest.loadSequencesTest("src/melting/test/2OMethylRNADuplexes.txt");
		
		System.out.print("\n\n Sequences \t TmExp \t all97 \t bre86 \t san04 \t san96 \t sug96 \t tan04 \n");

		MainTest.displayResults(DNANoSelfComplementarySequences, DNAmethods, "dnadna", "Na=1", "0.0004", "-nn");
		
		System.out.print("\n\n Sequences \t TmExp \t all97 \t bre86 \t san04 \t san96 \t sug96 \t tan04 \n");

		MainTest.displayResults(DNASelfComplementarySequences, DNAmethods, "dnadna", "Na=1", "0.0001", "-nn");

		System.out.print("\n\n Sequences \t TmExp \t fre86 \t xia98 \n");
		
		MainTest.displayResults(RNANoSelfComplementarySequences, RNAmethods, "rnarna", "Na=1", "0.0002", "-nn");

		System.out.print("\n\n Sequences \t TmExp \t fre86 \t xia98 \n");
		
		MainTest.displayResults(RNASelfComplementarySequences, RNAmethods, "rnarna", "Na=1", "0.0001", "-nn");

		System.out.print("\n\n Sequences \t TmExp \t sug95 \n");

		MainTest.displayResults(DNARNASequences, DNARNAmethods, "rnadna", "Na=1", "0.0001", "-nn");

		System.out.print("\n\n Sequences \t TmExp \t tur06 \n");
		
		MainTest.displayResults(mRNASequences, mRNAmethods, "mrnarna", "Na=0.1", "0.0001", "-nn");
	}

}
