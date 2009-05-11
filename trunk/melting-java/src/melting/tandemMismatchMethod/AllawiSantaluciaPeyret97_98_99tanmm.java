package melting.tandemMismatchMethod;


import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;

public class AllawiSantaluciaPeyret97_98_99tanmm extends PartialCalcul{

	/*REF: Allawi and SantaLucia (1997). Biochemistry 36: 10581-10594. 
	REF: Allawi and SantaLucia (1998). Biochemistry 37: 2170-2179.
	REF: Allawi and SantaLucia (1998). Nuc Acids Res 26: 2694-2701. 
	REF: Allawi and SantaLucia (1998). Biochemistry 37: 9435-9444.
	REF: Peyret et al. (1999). Biochemistry 38: 3468-3477*/
	
	public AllawiSantaluciaPeyret97_98_99tanmm(){
		loadData("AllawiSantaluciaPeyret1997_1998_1999tanmm.xml", this.collector);
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		for (int i = pos1; i < pos2; i++){
		
			enthalpy += this.collector.getMismatchvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)).getEnthalpy();
			entropy += this.collector.getMismatchvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)).getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("dnadna") == false){
			System.out.println("WARNING : the tandem mismatch parameters of " +
					"Allawi, Santalucia and Peyret are originally established " +
					"for DNA duplexes.");
			
			isApplicable = false;
		}
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {

		for (int i = pos1; i < pos2; i++){
			if (this.collector.getMismatchvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)) == null){
				return true;
			}
		}
		return super.isMissingParameters(sequences, pos1, pos2);
	}
}
