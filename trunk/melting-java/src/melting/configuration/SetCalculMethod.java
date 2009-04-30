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
import melting.calculMethodInterfaces.IonCorrectionMethod;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.calculMethodInterfaces.SodiumEquivalentMethod;
import melting.nearestNeighborModel.NearestNeighborMode;
import melting.sodiumEquivalence.Ahsen01_NaEquivalent;
import melting.sodiumEquivalence.Owczarzy08_NaEquivalent;

public class SetCalculMethod {
	
	private HashMap<String, SodiumEquivalentMethod> NaEqMethod = new HashMap<String, SodiumEquivalentMethod>();
	private HashMap<String, ApproximativeMode> approximativeMethod = new HashMap<String, ApproximativeMode>();
	//private HashMap<String, NNeighborMethod> initiationMethod = new HashMap<String, InitiationNeighborMethod>();
	private HashMap<String, IonCorrectionMethod> ionCorrectionMethod = new HashMap<String, IonCorrectionMethod>();
	private HashMap<String, PartialCalculMethod> partialCalculMethod = new HashMap<String, PartialCalculMethod>();
	private HashMap<String, CompletCalculMethod> completCalculMethod = new HashMap<String, CompletCalculMethod>();
	
	public SetCalculMethod(){
		this.completCalculMethod.put("approximate", new ApproximativeMode());
		this.completCalculMethod.put("nearest_neighbor", new NearestNeighborMode());
		this.completCalculMethod.put("default", null);
		
		this.approximativeMethod.put("Ahsen_2001", new Ahsen01());
		this.approximativeMethod.put("Marmur_Chester_1962_1993", new MarmurChester62_93(650));
		this.approximativeMethod.put("Marmur_Chester_1962_1993_corr", new MarmurChester62_93(535));
		this.approximativeMethod.put("Marmur_Schildkraut_Doty", new MarmurSchildkrautDoty());
		this.approximativeMethod.put("Owen_1969", new Owen69());
		this.approximativeMethod.put("Santalucia_1998", new Santalucia98());
		this.approximativeMethod.put("Wetmur_1991", new Wetmur91());
		
		this.NaEqMethod.put("Ahsen_2001", new Ahsen01_NaEquivalent());
		this.NaEqMethod.put("Owczarzy_2008", new Owczarzy08_NaEquivalent());
	}
	
	public SodiumEquivalentMethod setNaEqMethod (HashMap<String, String> optionSet){
		String methodName = optionSet.get(OptionManagement.NaEquivalentMethod);
		SodiumEquivalentMethod method = this.NaEqMethod.get(methodName);
		
		if (method.isApplicable(optionSet)) {
			return method;
		}
		return null;	
	}
	
	public CompletCalculMethod setCompletCalculMethod(HashMap<String, String> optionSet){
		String methodName = optionSet.get(OptionManagement.completMethod);
		CompletCalculMethod method = this.completCalculMethod.get(methodName);
		
		if (method == null){
			int thres = Integer.getInteger(optionSet.get(OptionManagement.threshold));
			String seq = optionSet.get(OptionManagement.sequence);
			String seq2 = optionSet.get(OptionManagement.complementarySequence);
			int duplexLength = Math.min(seq.length(),seq2.length());
			
			if (duplexLength > thres){
				methodName = optionSet.get(OptionManagement.approximativeMode);
				method = this.approximativeMethod.get(methodName);
				
				method.setUpVariable(optionSet);
			}
			else {
				method = new NearestNeighborMode();
				
				method.setUpVariable(optionSet);
			}
		}
		
		if (method.isApplicable()) {
			return method;
		}
		return null;
	}
	
	public PartialCalculMethod setWatsonCrickMethod(HashMap<String, String> optionSet){
		return null;
	}
}
