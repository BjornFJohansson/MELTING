package melting.configuration;

import java.util.HashMap;

import melting.CNGRepeatsMethods.Broda05CNGRepeats;
import melting.InternalLoopMethod.Santalucia04InternalLoop;
import melting.InternalLoopMethod.Turner06InternalLoop;
import melting.InternalLoopMethod.Znosco071x2Loop;
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
import melting.cricksNNMethods.AllawiSantalucia97;
import melting.cricksNNMethods.Breslauer86;
import melting.cricksNNMethods.Freier86;
import melting.cricksNNMethods.Santalucia04;
import melting.cricksNNMethods.Santalucia96;
import melting.cricksNNMethods.Sugimoto95;
import melting.cricksNNMethods.Tanaka04;
import melting.cricksNNMethods.Turner06;
import melting.cricksNNMethods.Xia98;
import melting.longBulgeMethod.Santalucia04LongBulgeLoop;
import melting.longBulgeMethod.Turner99_06LongBulgeLoop;
import melting.longDanglingEndMethod.Sugimoto02DNADanglingEnd;
import melting.longDanglingEndMethod.Sugimoto02RNADanglingEnd;
import melting.modifiedNucleicAcidMethod.Asanuma05Azobenzene;
import melting.modifiedNucleicAcidMethod.McTigue04LockedAcid;
import melting.modifiedNucleicAcidMethod.Santalucia05Inosine;
import melting.modifiedNucleicAcidMethod.Sugimoto01Hydroxyadenine;
import melting.modifiedNucleicAcidMethod.Sugimoto05Deoxyadenosine;
import melting.modifiedNucleicAcidMethod.Znosco07Inosine;
import melting.nearestNeighborModel.NearestNeighborMode;
import melting.secondDanglingEndMethod.Serra05DoubleDanglingEnd;
import melting.secondDanglingEndMethod.Serra06DoubleDanglingEnd;
import melting.singleBulgeMethod.Santalucia04SingleBulgeLoop;
import melting.singleBulgeMethod.Serra07SingleBulgeLoop;
import melting.singleBulgeMethod.Tanaka04SingleBulgeLoop;
import melting.singleBulgeMethod.Turner99_06SingleBulgeLoop;
import melting.singleDanglingEndMethod.Bommarito00SingleDanglingEnd;
import melting.singleDanglingEndMethod.Serra06_08SingleDanglingEnd;
import melting.singleMismatchMethods.AllawiSantaluciaPeyret97_98_99mm;
import melting.singleMismatchMethods.Turner06mm;
import melting.singleMismatchMethods.Znosco07mm;
import melting.singleMismatchMethods.Znosco08mm;
import melting.sodiumEquivalence.Ahsen01_NaEquivalent;
import melting.sodiumEquivalence.Owczarzy08_NaEquivalent;
import melting.tandemMismatchMethod.AllawiSantaluciaPeyret97_98_99tanmm;
import melting.tandemMismatchMethod.Turner99_06tanmm;
import melting.woddleNNMethod.Turner99Woddle;

public class RegisterCalculMethod {
	
	private HashMap<String, Class<? extends SodiumEquivalentMethod>> NaEqMethod = new HashMap<String, Class<? extends SodiumEquivalentMethod>>();
	private HashMap<String, Class<? extends ApproximativeMode>> approximativeMethod = new HashMap<String, Class<? extends ApproximativeMode>>();
	private HashMap<String, Class<? extends PartialCalculMethod>> cricksMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private HashMap<String, Class<? extends CompletCalculMethod>> completCalculMethod = new HashMap<String, Class<? extends CompletCalculMethod>>();
	private HashMap<String, Class<? extends PartialCalculMethod>> singleMismatchMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private HashMap<String, Class<? extends PartialCalculMethod>> tandemMismatchMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private HashMap<String, Class<? extends PartialCalculMethod>> woddleMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private HashMap<String, Class<? extends PartialCalculMethod>> internalLoopMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private HashMap<String, Class<? extends PartialCalculMethod>> singleBulgeLoopMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private HashMap<String, Class<? extends PartialCalculMethod>> longBulgeLoopMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private HashMap<String, Class<? extends PartialCalculMethod>> singleDangingEndMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private HashMap<String, Class<? extends PartialCalculMethod>> doubleDangingEndMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private HashMap<String, Class<? extends PartialCalculMethod>> longDangingEndMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private HashMap<String, Class<? extends PartialCalculMethod>> inosineMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private HashMap<String, Class<? extends PartialCalculMethod>> CNGRepeatsMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private HashMap<String, Class<? extends PartialCalculMethod>> azobenzeneMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private HashMap<String, Class<? extends PartialCalculMethod>> lockedAcidMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private HashMap<String, Class<? extends PartialCalculMethod>> hydroxyadenosineMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private HashMap<String, Class<? extends PartialCalculMethod>> deoxyadenosineMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();

	public RegisterCalculMethod(){
		initializeApproximativeMethods();
		initializeCompletCalculMethods();
		initializeCricksMethods();
		initializeNaEqMethods();
		initializeSingleMismatchMethods();
		initializeTandemMismatchMethods();
		initializeWoddleMismatchMethods();
		initializeInternalLoopMethods();
		initializeSingleBulgeLoopMethods();
		initializeLongBulgeLoopMethods();
		initializeSingleDanglingEndMethods();
		initializeDoubleDanglingEndMethods();
		initializeLongDanglingEndMethods();
		initializeCNGRepeatsMethods();
		initializeInosineMethods();
		initializeAzobenzeneMethods();
		initializeDeoxyadenosineMethods();
		initializeHydroxyadenosineMethods();
		initializeLockedAcidMethods();
	}
	
	private void initializeNaEqMethods(){
		this.NaEqMethod.put("Ahsen_2001", Ahsen01_NaEquivalent.class);
		this.NaEqMethod.put("Owczarzy_2008", Owczarzy08_NaEquivalent.class);
	}
	
	private void initializeCompletCalculMethods(){
		this.completCalculMethod.put("approximate", ApproximativeMode.class);
		this.completCalculMethod.put("nearest_neighbor", NearestNeighborMode.class);
		this.completCalculMethod.put("default", null);
	}
	
	private void initializeApproximativeMethods(){
		this.approximativeMethod.put("Ahsen_2001", Ahsen01.class);
		this.approximativeMethod.put("Marmur_Chester_1962_1993", MarmurChester62_93.class);//650
		this.approximativeMethod.put("Marmur_Chester_1962_1993_corr", MarmurChester62_93.class);//535
		this.approximativeMethod.put("Marmur_Schildkraut_Doty", MarmurSchildkrautDoty.class);
		this.approximativeMethod.put("Owen_1969", Owen69.class);
		this.approximativeMethod.put("Santalucia_1998", Santalucia98.class);
		this.approximativeMethod.put("Wetmur_1991", Wetmur91.class);
	}
	
	private void initializeCricksMethods(){
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
	
	private void initializeSingleMismatchMethods(){
		this.singleMismatchMethod.put("Allawi_Santalucia_Peyret_1997_1998_1999", AllawiSantaluciaPeyret97_98_99mm.class);
		this.singleMismatchMethod.put("Znosco_2007", Znosco07mm.class);
		this.singleMismatchMethod.put("Znosco_2008", Znosco08mm.class);
		this.singleMismatchMethod.put("Turner_2006", Turner06mm.class);
	}
	
	private void initializeTandemMismatchMethods(){
		this.tandemMismatchMethod.put("Allawi_Santalucia_Peyret_1997_1998_1999", AllawiSantaluciaPeyret97_98_99tanmm.class);
		this.tandemMismatchMethod.put("Turner_1999_2006", Turner99_06tanmm.class);
	}
	
	private void initializeWoddleMismatchMethods(){
		this.woddleMethod.put("Turner_1999", Turner99Woddle.class);
	}
	
	private void initializeInternalLoopMethods(){
		this.internalLoopMethod.put("Turner_2006", Turner06InternalLoop.class);
		this.internalLoopMethod.put("Santalucia_2004", Santalucia04InternalLoop.class);
		this.internalLoopMethod.put("Znosco_2007", Znosco071x2Loop.class);
	}
	
	private void initializeSingleBulgeLoopMethods(){
		this.singleBulgeLoopMethod.put("Turner_2006", Turner99_06SingleBulgeLoop.class);
		this.singleBulgeLoopMethod.put("Santalucia_2004", Santalucia04SingleBulgeLoop.class);
		this.singleBulgeLoopMethod.put("Serra_2007", Serra07SingleBulgeLoop.class);
		this.singleBulgeLoopMethod.put("Tanaka_2004", Tanaka04SingleBulgeLoop.class);

	}
	
	private void initializeLongBulgeLoopMethods(){
		this.longBulgeLoopMethod.put("Turner_2006", Turner99_06LongBulgeLoop.class);
		this.longBulgeLoopMethod.put("Santalucia_2004", Santalucia04LongBulgeLoop.class);
	}
	
	private void initializeSingleDanglingEndMethods(){
		this.singleDangingEndMethod.put("Bommarito_2000", Bommarito00SingleDanglingEnd.class);
		this.longBulgeLoopMethod.put("Serra_2006_2008", Serra06_08SingleDanglingEnd.class);
	}
	
	private void initializeDoubleDanglingEndMethods(){
		this.doubleDangingEndMethod.put("Serra_2005", Serra05DoubleDanglingEnd.class);
		this.doubleDangingEndMethod.put("Serra_2006", Serra06DoubleDanglingEnd.class);
	}
	
	private void initializeLongDanglingEndMethods(){
		this.longDangingEndMethod.put("Sugimoto_2002_dna", Sugimoto02DNADanglingEnd.class);
		this.longDangingEndMethod.put("Sugimoto_2002_rna", Sugimoto02RNADanglingEnd.class);
	}
	
	private void initializeCNGRepeatsMethods(){
		this.CNGRepeatsMethod.put("Sugimoto_2002_dna", Broda05CNGRepeats.class);
	}
	
	private void initializeInosineMethods(){
		this.inosineMethod.put("Santalucia_2005", Santalucia05Inosine.class);
		this.inosineMethod.put("Znosco_2007", Znosco07Inosine.class);
	}
	
	private void initializeAzobenzeneMethods(){
		this.azobenzeneMethod.put("Asanuma_2005", Asanuma05Azobenzene.class);
	}
	
	private void initializeLockedAcidMethods(){
		this.lockedAcidMethod.put("McTigue_2004", McTigue04LockedAcid.class);
	}
	
	private void initializeHydroxyadenosineMethods(){
		this.hydroxyadenosineMethod.put("Sugimoto_2001", Sugimoto01Hydroxyadenine.class);
	}
	
	private void initializeDeoxyadenosineMethods(){
		this.deoxyadenosineMethod.put("Sugimoto_2005", Sugimoto05Deoxyadenosine.class);
	}
	
	private HashMap<String , Class<? extends PartialCalculMethod>> getCalculMethodHashMap(String optionName){
		if (optionName.equals(OptionManagement.azobenzeneMethod)){
			return this.azobenzeneMethod;
		}
		else if(optionName.equals(OptionManagement.CNGMethod)){
			return this.CNGRepeatsMethod;
		}
		else if(optionName.equals(OptionManagement.deoxyadenosineMethod)){
			return this.deoxyadenosineMethod;
		}
		else if(optionName.equals(OptionManagement.doubleDanglingEndMethod)){
			return this.doubleDangingEndMethod;
		}
		else if(optionName.equals(OptionManagement.hydroxyadenineMethod)){
			return this.hydroxyadenosineMethod;
		}
		else if(optionName.equals(OptionManagement.inosineMethod)){
			return this.inosineMethod;
		}
		else if(optionName.equals(OptionManagement.internalLoopMethod)){
			return this.internalLoopMethod;
		}
		else if(optionName.equals(OptionManagement.lockedAcidMethod)){
			return this.lockedAcidMethod;
		}
		else if(optionName.equals(OptionManagement.longBulgeLoopMethod)){
			return this.longBulgeLoopMethod;
		}
		else if(optionName.equals(OptionManagement.longDanglingEndMethod)){
			return this.longDangingEndMethod;
		}
		else if(optionName.equals(OptionManagement.NNMethod)){
			return this.cricksMethod;
		}
		else if(optionName.equals(OptionManagement.singleBulgeLoopMethod)){
			return this.singleBulgeLoopMethod;
		}
		else if(optionName.equals(OptionManagement.singleDanglingEndMethod)){
			return this.singleDangingEndMethod;
		}
		else if(optionName.equals(OptionManagement.singleMismatchMethod)){
			return this.singleMismatchMethod;
		}
		else if(optionName.equals(OptionManagement.tandemMismatchMethod)){
			return this.tandemMismatchMethod;
		}
		else if(optionName.equals(OptionManagement.woddleBaseMethod)){
			return this.tandemMismatchMethod;
		}
		return null;
	}
	
	public PartialCalculMethod getPartialCalculMethod(String optionName, HashMap<String, String> optionSet){
		
		String methodName = optionSet.get(optionName);
		
		if (methodName != null){
			PartialCalculMethod method;
			try {
				method = getCalculMethodHashMap(optionName).get(methodName).newInstance();
				
				return method;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public SodiumEquivalentMethod getNaEqMethod (HashMap<String, String> optionSet){
		String methodName = optionSet.get(OptionManagement.NaEquivalentMethod);
		
		if (methodName == null){
			return null;
		}
		SodiumEquivalentMethod method;
		try {
			method = this.NaEqMethod.get(methodName).newInstance();
			if (method.isApplicable(optionSet)) {
				return method;
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;	
	}
	
	public CompletCalculMethod getCompletCalculMethod(HashMap<String, String> optionSet){
		
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
					initializeApproximativeMethods();
					
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
}
