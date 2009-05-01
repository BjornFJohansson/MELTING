package melting.configuration;

import java.util.HashMap;

import melting.ThermoResult;
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
import melting.cricksNNMethods.AllawiSantalucia97;
import melting.cricksNNMethods.Breslauer86;
import melting.cricksNNMethods.CricksNNMethod;
import melting.cricksNNMethods.Freier86;
import melting.cricksNNMethods.Santalucia04;
import melting.cricksNNMethods.Santalucia96;
import melting.cricksNNMethods.Sugimoto95;
import melting.cricksNNMethods.Tanaka04;
import melting.cricksNNMethods.Turner06;
import melting.cricksNNMethods.Xia98;
import melting.nearestNeighborModel.NearestNeighborMode;
import melting.sodiumEquivalence.Ahsen01_NaEquivalent;
import melting.sodiumEquivalence.Owczarzy08_NaEquivalent;

public class RegisterCalculMethod {
	
	private HashMap<String, Class<? extends SodiumEquivalentMethod>> NaEqMethod = new HashMap<String, Class<? extends SodiumEquivalentMethod>>();
	private HashMap<String, Class<? extends ApproximativeMode>> approximativeMethod = new HashMap<String, Class<? extends ApproximativeMode>>();
	private HashMap<String, Class<? extends CricksNNMethod>> cricksMethod = new HashMap<String, Class<? extends CricksNNMethod>>();
	private HashMap<String, Class<? extends IonCorrectionMethod>> ionCorrectionMethod = new HashMap<String, Class<? extends IonCorrectionMethod>>();
	private HashMap<String, Class<? extends PartialCalculMethod>> partialCalculMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private HashMap<String, Class<? extends CompletCalculMethod>> completCalculMethod = new HashMap<String, Class<? extends CompletCalculMethod>>();
	
	private void setAllNaEqMethods(){
		this.NaEqMethod.put("Ahsen_2001", Ahsen01_NaEquivalent.class);
		this.NaEqMethod.put("Owczarzy_2008", Owczarzy08_NaEquivalent.class);
	}
	
	private void setAllCompletCalculMethods(){
		this.completCalculMethod.put("approximate", ApproximativeMode.class);
		this.completCalculMethod.put("nearest_neighbor", NearestNeighborMode.class);
		this.completCalculMethod.put("default", null);
	}
	
	private void setAllApproximativeMethods(){
		this.approximativeMethod.put("Ahsen_2001", Ahsen01.class);
		this.approximativeMethod.put("Marmur_Chester_1962_1993", MarmurChester62_93.class);//650
		this.approximativeMethod.put("Marmur_Chester_1962_1993_corr", MarmurChester62_93.class);//535
		this.approximativeMethod.put("Marmur_Schildkraut_Doty", MarmurSchildkrautDoty.class);
		this.approximativeMethod.put("Owen_1969", Owen69.class);
		this.approximativeMethod.put("Santalucia_1998", Santalucia98.class);
		this.approximativeMethod.put("Wetmur_1991", Wetmur91.class);
	}
	
	private void setAllCricksMethods(){
		this.cricksMethod.put("Allawi_Santalucia_1997", AllawiSantalucia97.class);
		this.cricksMethod.put("Beslauer_1986", Breslauer86.class);
		this.cricksMethod.put("Freier_1986", Freier86.class);
		this.cricksMethod.put("Santalucia_2004", Santalucia04.class);
		this.cricksMethod.put("Santalucia_1996", Santalucia96.class);
		this.cricksMethod.put("Sugimoto_1995", Sugimoto95.class);
		this.cricksMethod.put("Tanaka_2004", Tanaka04.class);
		this.cricksMethod.put("Turner_2006", Turner06.class);
		this.cricksMethod.put("Xia_1998", Xia98.class);
	}
	
	public SodiumEquivalentMethod getNaEqMethod (HashMap<String, String> optionSet){
		setAllNaEqMethods();
		
		String methodName = optionSet.get(OptionManagement.NaEquivalentMethod);
		SodiumEquivalentMethod method = null;
		try {
			method = this.NaEqMethod.get(methodName).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		if (method.isApplicable(optionSet) && method != null) {
			return method;
		}
		return null;	
	}
	
	public CompletCalculMethod getCompletCalculMethod(HashMap<String, String> optionSet){
		setAllCompletCalculMethods();
		
		String methodName = optionSet.get(OptionManagement.completMethod);
		CompletCalculMethod method;
		try {
			method = this.completCalculMethod.get(methodName).newInstance();
			
			if (method == null){
				int thres = Integer.getInteger(optionSet.get(OptionManagement.threshold));
				String seq = optionSet.get(OptionManagement.sequence);
				String seq2 = optionSet.get(OptionManagement.complementarySequence);
				int duplexLength = Math.min(seq.length(),seq2.length());
				
				if (duplexLength > thres){
					setAllApproximativeMethods();
					
					methodName = optionSet.get(OptionManagement.approximativeMode);
					try {
						method = this.approximativeMethod.get(methodName).newInstance();
						method.setUpVariable(optionSet);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				else {
					method = new NearestNeighborMode();
					
					method.setUpVariable(optionSet);
				}
			}
			
			if (method.isApplicable() && method != null) {
				return method;
			}
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public CricksNNMethod getWatsonCrickMethod(HashMap<String, String> optionSet, ThermoResult result, int pos1, int pos2){
		setAllCricksMethods();
		
		String methodName = optionSet.get(OptionManagement.NNMethod);
		CricksNNMethod method;
		try {
			method = this.cricksMethod.get(methodName).newInstance();
			if (method.isApplicable(optionSet, pos1, pos2)) {
				return method;
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
