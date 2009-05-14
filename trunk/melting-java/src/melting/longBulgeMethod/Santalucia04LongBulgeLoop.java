package melting.longBulgeMethod;


import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;

public class Santalucia04LongBulgeLoop extends PartialCalcul{

/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */
	
	public static String defaultFileName = "Santalucia2004longbulge.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		NucleotidSequences bulgeLoop = new NucleotidSequences(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		int numberAT = bulgeLoop.calculateNumberOfTerminal('A', 'T');
		String bulgeSize = Integer.toString(Math.abs(pos2 - pos1) - 1);
		
		if (this.collector.getBulgeLoopvalue(bulgeSize) == null){
			entropy += this.collector.getBulgeLoopvalue("30").getEntropy() + 2.44 * 1.99 * 310.15 * Math.log(Integer.getInteger(bulgeSize) / 30);
		}
		else {
			entropy += this.collector.getBulgeLoopvalue(bulgeSize).getEntropy();
		}
		
		if (numberAT> 0){
			enthalpy += numberAT * this.collector.getClosureValue("A", "T").getEnthalpy();
			enthalpy += numberAT * this.collector.getClosureValue("A", "T").getEntropy();
		
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("dnadna") == false){
			System.out.println("WARNING : the single bulge loop parameters of " +
					"Santalucia (2004) are originally established " +
					"for DNA sequences.");
			
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		boolean isMissingParameters = super.isMissingParameters(sequences, pos1, pos2);
		NucleotidSequences bulgeLoop = new NucleotidSequences(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		int numberAT = bulgeLoop.calculateNumberOfTerminal('A', 'T');
		String bulgeSize = Integer.toString(Math.abs(pos2 - pos1) - 1);
		
		if (numberAT > 0){
			if (this.collector.getClosureValue("A", "T") == null){
				return true;
			}
		}

		if (this.collector.getBulgeLoopvalue(bulgeSize) == null){
			if (this.collector.getBulgeLoopvalue("30") == null){
			return true;
			}
		}
		return isMissingParameters;
	}
}
