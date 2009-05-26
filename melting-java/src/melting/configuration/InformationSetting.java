package melting.configuration;

import melting.Environment;
import melting.approximativeMethods.ApproximativeMode;
import melting.calculMethodInterfaces.CompletCalculMethod;
import melting.nearestNeighborModel.NearestNeighborMode;

public class InformationSetting {
	
	CompletCalculMethod calculMethod;
	Environment environment;
	StringBuffer verbose = new StringBuffer();
	
	public InformationSetting(CompletCalculMethod method, Environment environment){
		this.calculMethod = calculMethod;
		this.environment = environment;
	}
	
	private void writeVerbose(){

		verbose.append("******************************************************************************\n");
		verbose.append(OptionManagement.getVersion() + "\n");
		verbose.append( "This program   computes for a nucleotide probe, the enthalpy, the entropy \n");
		verbose.append("and the melting temperature of the binding to its complementary template. \n");
		verbose.append("Four types of hybridisation are possible: DNA/DNA, DNA/RNA, RNA/RNA and 2-O-methyl RNA/RNA. \n");
		verbose.append("Copyright (C) Nicolas Le Novère and Marine Dumousseau 2009 \n \n");
		verbose.append("******************************************************************************\n");
		
		if (this.calculMethod instanceof ApproximativeMode){
			String methodName = OptionManagement.approximativeMode;
			
			verbose.append(environment.getOptions().get(OptionManagement.completMethod) + " : " + methodName + "\n");
		}
		else if (this.calculMethod instanceof NearestNeighborMode){
			String methodName = OptionManagement.NNMethod;
			
			verbose.append(environment.getOptions().get(OptionManagement.completMethod) + " : " + methodName + "\n");
		}
		
            
	}
	
	public void DisplayVerbose(){
		writeVerbose();
		System.out.println(verbose);
	}
}
