package melting;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class MainTestPerfectMatching{

	public static void main(String[] args) {
		NumberFormat format = NumberFormat.getInstance(); 
		format.setMaximumFractionDigits(2);
		
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
		
		ArrayList<String> RNADNAmethods = new ArrayList<String>();
		RNADNAmethods.add("sug95");
		
		ArrayList<String> mRNAmethods = new ArrayList<String>();
		mRNAmethods.add("tu06");

		Properties DNANoSelfComplementarySequences = MainTest.loadSequencesTest("src/melting/test/DNANoSelfComplementary.txt");
		Properties DNASelfComplementarySequences = MainTest.loadSequencesTest("src/melting/test/DNASelfComplementary.txt");
	
		Properties RNANoSelfComplementarySequences = MainTest.loadSequencesTest("src/melting/test/RNANoSelfComplementary.txt");
		Properties RNASelfComplementarySequences = MainTest.loadSequencesTest("src/melting/test/RNASelfComplementary.txt"); 
		
		Properties DNARNASequences = MainTest.loadSequencesTest("src/melting/test/DNARNADuplexes.txt");
		
		Properties mRNASequences = MainTest.loadSequencesTest("src/melting/test/2OMethylRNADuplexes.txt");
		
		Iterator<Map.Entry<Object, Object>> DNAEntry1 = DNANoSelfComplementarySequences.entrySet().iterator();
		System.out.println("Sequences \t TmExp \t all97 \t bre86 \t san04 \t san96 \t sug96 \t tan04 \n");
		while (DNAEntry1.hasNext()){
			Map.Entry<Object, Object> pairs = DNAEntry1.next();
			System.out.println( pairs.getKey() + "\t" + pairs.getValue());

			for (int i=0; i < DNAmethods.size(); i++){
				String [] argsOption = {"-H", "dnadna", "-E", "Na=1", "-P", "0.0004", "-S", pairs.getKey().toString(), "-nn", DNAmethods.get(i)}; 
				//double Tm = MainTest.getMeltingTest(argsOption);
				//System.out.println(Tm + DNAmethods.get(i));
				//System.out.println("\t" + format.format(Tm));
			}
		}
	}

}
