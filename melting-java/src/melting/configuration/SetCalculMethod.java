package melting.configuration;

import java.util.HashMap;

import melting.approximativeMethods.Ahsen01;
import melting.approximativeMethods.ApproximativeMode;
import melting.approximativeMethods.MarmurChester62_93;
import melting.approximativeMethods.MarmurSchildkrautDoty;
import melting.approximativeMethods.Owen69;
import melting.approximativeMethods.Santalucia98;
import melting.approximativeMethods.Wetmur91;
import melting.calculMethodInterfaces.CompletCalculMethod;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.calculMethodInterfaces.SodiumEquivalentMethod;
import melting.nearestNeighborModel.NearestNeighborMode;
import melting.sodiumEquivalence.Ahsen01_NaEquivalent;
import melting.sodiumEquivalence.Owczarzy08_NaEquivalent;

public class SetCalculMethod {

	public static SodiumEquivalentMethod setNaEqMethod (HashMap<String, String> optionSet){
		String methodName = optionSet.get(OptionManagement.NaEquivalentMethod);
		
		if (methodName.equals("Ahsen_2001")){
			Ahsen01_NaEquivalent method = new Ahsen01_NaEquivalent();
			if (method.isApplicable(optionSet)) {
				return method;
			}
		}
		else if (methodName.equals("Owczarzy_2008")){
			Owczarzy08_NaEquivalent method = new Owczarzy08_NaEquivalent();
			if (method.isApplicable(optionSet)) {
				return method;
			}
		}
		return null;
	}
	
	public CompletCalculMethod setCompletCalculMethod(HashMap<String, String> optionSet){
		
		String methodName = optionSet.get(OptionManagement.completMethod);
		String approximativeMethodName = optionSet.get(OptionManagement.approximativeMode);
		
		if (methodName.equals("default")){
			int thres = Integer.getInteger(optionSet.get(OptionManagement.threshold));
			String seq = optionSet.get(OptionManagement.sequence);
			String seq2 = optionSet.get(OptionManagement.complementarySequence);
			int duplexLength = Math.min(seq.length(),seq2.length());
			
			if (duplexLength > thres){
				ApproximativeMode method = new ApproximativeMode();
				
				if (approximativeMethodName.equals("Ahsen_2001")) {
					method = new Ahsen01();
				}
				
				else if (approximativeMethodName.equals("Marmur_Chester_1962_1963")) {
					method = new MarmurChester62_93(650);
				}
				
				else if (approximativeMethodName.equals("Marmur_Chester_1962_1963_corr")) {
					method = new MarmurChester62_93(535);
				}
				
				else if (approximativeMethodName.equals("Marmur_Schildkraut_Doty")) {
					method = new MarmurSchildkrautDoty();
				}
				else if (approximativeMethodName.equals("Owen_1969")) {
					method = new Owen69();
				}
				else if (approximativeMethodName.equals("Santalucia_1998")) {
					method = new Santalucia98();
				}
				else if (approximativeMethodName.equals("Wetmur_1991")) {
					method = new Wetmur91();
				}
				method.setUpVariable(optionSet);
				if (method.isApplicable()) {
					return method;
				}
			}
			
			else {
				NearestNeighborMode method = new NearestNeighborMode();
				method.setUpVariable(optionSet);
				return method;
			}
		}
		
		return null;
	}
	
	public PartialCalculMethod setWatsonCrickMethod(HashMap<String, String> optionSet){
		return null;
	}
}
