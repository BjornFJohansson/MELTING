package melting.longBulgeMethod;


import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;

public class Turner99_06LongBulgeLoop extends PartialCalcul{

	/*REF: Douglas M Turner et al (2006). Nucleic Acids Research 34: 4912-4924. 
	REF: Douglas M Turner et al (1999). J.Mol.Biol.  288: 911_940.*/ 
	
	public Turner99_06LongBulgeLoop(){
		loadData("Turner1999_2006longbulge.xml", this.collector);
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		String bulgeSize = Integer.toString(Math.abs(pos2 - pos1) - 1);
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		NucleotidSequences bulgeLoop = new NucleotidSequences(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		int numberAU = bulgeLoop.calculateNumberOfTerminal('A', 'U');
		int numberGU = bulgeLoop.calculateNumberOfTerminal('G', 'U');
		
		if (this.collector.getInitiationBulgevalue(bulgeSize) == null){
			enthalpy += this.collector.getInitiationBulgevalue(">6").getEnthalpy();
			entropy += this.collector.getInitiationBulgevalue("6").getEntropy() / 310.15 * (8.7 - 1085.5 * Math.log( Integer.getInteger(bulgeSize) / 6));
		}
		else{
			enthalpy += this.collector.getInitiationBulgevalue(bulgeSize).getEnthalpy();
			entropy += this.collector.getInitiationBulgevalue(bulgeSize).getEntropy();
		}
		
		if (numberAU > 0){
			
			enthalpy += numberAU * this.collector.getClosureValue("A", "U").getEnthalpy();
			entropy += numberAU * this.collector.getClosureValue("A", "U").getEntropy();
		}
		
		if (numberGU > 0){
			
			enthalpy += numberGU * this.collector.getClosureValue("G", "U").getEnthalpy();
			entropy += numberGU * this.collector.getClosureValue("G", "U").getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("rnarna") == false){
			System.out.println("WARNING : the single bulge loop parameters of " +
					"Turner (1999-2006) are originally established " +
					"for RNA sequences.");
			
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences bulgeLoop = new NucleotidSequences(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		int numberAU = bulgeLoop.calculateNumberOfTerminal('A', 'U');
		int numberGU = bulgeLoop.calculateNumberOfTerminal('G', 'U');
		boolean isMissingParameters = super.isMissingParameters(sequences, pos1, pos2);
		
		if (numberAU > 0){
			if (this.collector.getClosureValue("A", "U") == null){
				return true;
			}
		}
		
		if (numberGU > 0){
			if (this.collector.getClosureValue("G", "U") == null){
				return true;
			}
		}
		
		String bulgeSize = Integer.toString(Math.abs(pos2 - pos1) - 1);
		if (this.collector.getInitiationBulgevalue(bulgeSize) == null){
			if (this.collector.getInitiationBulgevalue("6") == null){
				return true;
			}
		}
		return isMissingParameters;
	}
}
