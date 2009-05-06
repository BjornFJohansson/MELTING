package melting;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;

import melting.configuration.OptionManagement;

import org.xml.sax.SAXException;


public class Helper {

	public static int calculatePercentGC(String seq1, String seq2){
		int numberGC = 0;
		for (int i = 0; i < Math.min(seq1.length(), seq2.length());i++){
			if (seq1.charAt(i) == 'G' || seq1.charAt(i) == 'C'){
				if (isComplementaryBasePair(seq1.charAt(i), seq2.charAt(i))){
					numberGC++;
				}
			}
		}
		return numberGC / Math.min(seq1.length(), seq2.length()) * 100;
	}
	
	public static int getPercentMismatching(String seq1, String seq2){
		int numberMismatching = 0;
		for (int i = 0; i < Math.min(seq1.length(), seq2.length()); i++){
			if (isComplementaryBasePair(seq1.charAt(i), seq2.charAt(i)) == false){
				numberMismatching++;
			}
		}
		return numberMismatching / Math.min(seq1.length(), seq2.length()) * 100;
	}
	
	public static boolean isComplementaryBasePair(char acid1, char acid2){
		switch(acid1){
		
		case 'A': 
			if (acid2 == 'T' || acid2 == 'U'){
				return true;
			}
			return false;
			
		case 'T':
			if (acid2 == 'A'){
				return true;
			}
			return false;
			
		case 'G':
			if (acid2 == 'C'){
				return true;
			}
			return false;
		
		case 'C':
			if (acid2 == 'G'){
				return true;
			}
			return false;
			
		case 'U':
			if (acid2 == 'A'){
				return true;
			}
			return false;
			
		default:
			return false;
		}
	}
	
	public static String getComplementarySequence(String sequence, String hybridization){
		StringBuffer complementary = new StringBuffer(sequence.length());
		for (int i = 0; i < sequence.length(); i++){
			switch(sequence.charAt(i)){
			case 'A':
				if (hybridization.equals("dnadna") || hybridization.equals("rnadna")){
					complementary.append('T');
				}
				else if (hybridization.equals("rnarna") || hybridization.equals("mrnarna") || hybridization.equals("dnarna")){
					complementary.append('U');
				}
				break;
			case 'T':
				complementary.append('A');
				break;
			case 'C':
				complementary.append('G');
				break;
			case 'G':
				complementary.append('C');
				break;
			case 'U':
				complementary.append('A');
				break;
			}
		}
		return complementary.toString();
	}
	
	public static void loadData(String fileName, DataCollect collector){
		File dataFile = new File(OptionManagement.dataPathway + "/" + fileName);
		FileReader reader = new FileReader();
		try {
			reader.readFile(dataFile, collector.getDatas());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	public static String getInversedSequence(String sequence){
		StringBuffer newSequence = new StringBuffer(sequence.length());
		for (int i = 0; i < sequence.length(); i++){
			newSequence.append(sequence.charAt(sequence.length() - i)); 
		}
		return newSequence.toString();
	}
	
	public static String convertToPyr_Pur(String sequence){
		StringBuffer newSeq = new StringBuffer(sequence.length());
		
		for (int i = 0; i < sequence.length(); i++){
			switch (sequence.charAt(i)) {
			case 'A':
				newSeq.append('R');
				break;
			case 'G':
				newSeq.append('R');
				break;
			case 'U':
				newSeq.append('Y');
				break;
			case 'C':
				newSeq.append('Y');
				break;

			default:
				System.err.println("There are non watson crick bases in the sequence.");
				break;
			}
		}
		return newSeq.toString();
	}
	
	public static int calculateLoopLength (String seq1, String seq2){
		int loop = seq1.length() - 2 + seq2.length() - 2;
		
		for (int i = 1; i < seq1.length() - 1; i++){
			if (seq1.charAt(i) == '-'){
				loop--;
			}
			if (seq2.charAt(i) == '-'){
				loop--;
			}
		}
		return loop;
	}
	
public static boolean isAsymetricLoop(String seq1, String seq2){
		
		for (int i= 1; i < seq1.length() - 1; i++){
			if (seq1.charAt(i) == '-' || seq2.charAt(i) == '-'){
				return true;
			}
		}
		return false;
	}
}
