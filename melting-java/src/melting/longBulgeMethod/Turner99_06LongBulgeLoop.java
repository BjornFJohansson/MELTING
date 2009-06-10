package melting.longBulgeMethod;


import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Turner99_06LongBulgeLoop extends PartialCalcul{

	/*REF: Douglas M Turner et al (2006). Nucleic Acids Research 34: 4912-4924. 
	REF: Douglas M Turner et al (1999). J.Mol.Biol.  288: 911_940.*/ 
	
	public static String defaultFileName = "Turner1999_2006longbulge.xml";
	
	protected static String formulaEnthalpy = "delat H = H(bulge of n initiation) + number AU closing x H(AU closing) + number GU closing x H(GU closing)";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	protected boolean isClosingPenaltyNecessary(){
		return true;
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		NucleotidSequences bulgeLoop = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));
		
		OptionManagement.meltingLogger.log(Level.FINE, "The long bulge loop formulas from Turner et al. (1999, 2006) : " + formulaEnthalpy + " (entropy formula is similar)");
		
		String bulgeSize = Integer.toString(Math.abs(pos2 - pos1) - 1);
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		Thermodynamics initiationBulge = this.collector.getInitiationBulgevalue(bulgeSize);
		if (initiationBulge == null){
			initiationBulge = this.collector.getInitiationBulgevalue(">6");
			
			OptionManagement.meltingLogger.log(Level.FINE, "bulge loop of " + bulgeSize + " :  enthalpy = " + initiationBulge.getEnthalpy() + "  entropy = " + initiationBulge.getEntropy() + " / 310.15 x (8.7 - 1085.5 x ln( bulgeSize / 6)");

			enthalpy += initiationBulge.getEnthalpy();
			entropy += initiationBulge.getEntropy() / 310.15 * (8.7 - 1085.5 * Math.log( Integer.parseInt(bulgeSize) / 6));
		}
		else{
			OptionManagement.meltingLogger.log(Level.FINE, "bulge loop of " + bulgeSize + " :  enthalpy = " + initiationBulge.getEnthalpy() + "  entropy = " + initiationBulge.getEntropy());

			enthalpy += initiationBulge.getEnthalpy();
			entropy += initiationBulge.getEntropy();
		}
		
		if (isClosingPenaltyNecessary()){
			int numberAU = bulgeLoop.calculateNumberOfTerminal('A', 'U');
			int numberGU = bulgeLoop.calculateNumberOfTerminal('G', 'U');
			if (numberAU > 0){
				Thermodynamics closingAU = this.collector.getClosureValue("A", "U");
				
				OptionManagement.meltingLogger.log(Level.FINE, numberAU + " x AU closing : enthalpy = " + closingAU.getEnthalpy() + "  entropy = " + closingAU.getEntropy());

				enthalpy += numberAU * closingAU.getEnthalpy();
				entropy += numberAU * closingAU.getEntropy();
			}
			
			if (numberGU > 0){
				Thermodynamics closingGU = this.collector.getClosureValue("G", "U");
				
				OptionManagement.meltingLogger.log(Level.FINE, numberGU + " x GU closing : enthalpy = " + closingGU.getEnthalpy() + "  entropy = " + closingGU.getEntropy());
				
				enthalpy += numberGU * closingGU.getEnthalpy();
				entropy += numberGU * closingGU.getEntropy();
			}
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		
		if (environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The single bulge loop parameters of " +
					"Turner (1999-2006) are originally established " +
					"for RNA sequences.");
			
			environment.modifieSequences(environment.getSequences().getSequence(pos1, pos2, "rna"), environment.getSequences().getSequence(pos1, pos2, "rna"));
		}
		
		return super.isApplicable(environment, pos1, pos2);
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
