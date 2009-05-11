package melting.singleMismatchMethods;


import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;

public class AllawiSantaluciaPeyret97_98_99mm extends PartialCalcul{

	/*REF: Allawi and SantaLucia (1997). Biochemistry 36: 10581-10594. 
	REF: Allawi and SantaLucia (1998). Biochemistry 37: 2170-2179.
	REF: Allawi and SantaLucia (1998). Nuc Acids Res 26: 2694-2701. 
	REF: Allawi and SantaLucia (1998). Biochemistry 37: 9435-9444.
	REF: Peyret et al. (1999). Biochemistry 38: 3468-3477*/
	
	public AllawiSantaluciaPeyret97_98_99mm(){
		loadData("AllawiSantaluciaPeyret1997_1998_1999mm.xml", this.collector);
	}

	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {

		double enthalpy = result.getEnthalpy() + collector.getMismatchvalue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)).getEnthalpy();
		double entropy = result.getEntropy() + collector.getMismatchvalue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)).getEnthalpy();
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1, int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("dnadna") == false){
			System.out.println("WARNING : the single mismatch parameters of " +
					"Allawi, Santalucia and Peyret are originally established " +
					"for DNA duplexes.");
			
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
			
		if (this.collector.getMismatchvalue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)) == null){
			return true;
		}
		return super.isMissingParameters(sequences, pos1, pos2);
	}
	
}
