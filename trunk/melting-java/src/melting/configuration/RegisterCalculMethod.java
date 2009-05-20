package melting.configuration;

import java.util.HashMap;

import melting.Helper;
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
import melting.modifiedNucleicAcidMethod.Sugimoto01Hydroxyadenine;
import melting.modifiedNucleicAcidMethod.Sugimoto05Deoxyadenosine;
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
import melting.sodiumEquivalence.Mitsuhashi96NaEquivalent;
import melting.sodiumEquivalence.Peyret00_NaEquivalent;
import melting.tandemMismatchMethod.AllawiSantaluciaPeyret97_98_99tanmm;
import melting.tandemMismatchMethod.Turner99_06tanmm;
import melting.woddleNNMethod.Santalucia05Inosine;
import melting.woddleNNMethod.Turner99Wobble;
import melting.woddleNNMethod.Znosco07Inosine;

public class RegisterCalculMethod {
	
	private static HashMap<String, Class<? extends SodiumEquivalentMethod>> NaEqMethod = new HashMap<String, Class<? extends SodiumEquivalentMethod>>();
	private static HashMap<String, Class<? extends ApproximativeMode>> approximativeMethod = new HashMap<String, Class<? extends ApproximativeMode>>();
	private static HashMap<String, Class<? extends PartialCalculMethod>> cricksMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private static HashMap<String, Class<? extends CompletCalculMethod>> completCalculMethod = new HashMap<String, Class<? extends CompletCalculMethod>>();
	private static HashMap<String, Class<? extends PartialCalculMethod>> singleMismatchMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private static HashMap<String, Class<? extends PartialCalculMethod>> tandemMismatchMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private static HashMap<String, Class<? extends PartialCalculMethod>> woddleMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private static HashMap<String, Class<? extends PartialCalculMethod>> internalLoopMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private static HashMap<String, Class<? extends PartialCalculMethod>> singleBulgeLoopMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private static HashMap<String, Class<? extends PartialCalculMethod>> longBulgeLoopMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private static HashMap<String, Class<? extends PartialCalculMethod>> singleDangingEndMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private static HashMap<String, Class<? extends PartialCalculMethod>> doubleDangingEndMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private static HashMap<String, Class<? extends PartialCalculMethod>> longDangingEndMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private static HashMap<String, Class<? extends PartialCalculMethod>> inosineMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private static HashMap<String, Class<? extends PartialCalculMethod>> CNGRepeatsMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private static HashMap<String, Class<? extends PartialCalculMethod>> azobenzeneMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private static HashMap<String, Class<? extends PartialCalculMethod>> lockedAcidMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private static HashMap<String, Class<? extends PartialCalculMethod>> hydroxyadenosineMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private static HashMap<String, Class<? extends PartialCalculMethod>> deoxyadenosineMethod = new HashMap<String, Class<? extends PartialCalculMethod>>();
	private static HashMap<String, HashMap<String, Class<? extends PartialCalculMethod>>> PartialCalculMethods = new HashMap<String, HashMap<String, Class<? extends PartialCalculMethod>>>();
	
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
		
		initializePartialCalculMethods();
	}
	
	private void initializeNaEqMethods(){
		NaEqMethod.put("Ahsen_2001", Ahsen01_NaEquivalent.class);
		NaEqMethod.put("Peyret_2000", Peyret00_NaEquivalent.class);
		NaEqMethod.put("Mitsuhashi_1996", Mitsuhashi96NaEquivalent.class);
	}
	
	private void initializeCompletCalculMethods(){
		completCalculMethod.put("approximate", ApproximativeMode.class);
		completCalculMethod.put("nearest_neighbor", NearestNeighborMode.class);
		completCalculMethod.put("default", null);
	}
	
	private void initializeApproximativeMethods(){
		approximativeMethod.put("Ahsen_2001", Ahsen01.class);
		approximativeMethod.put("Marmur_Chester_1962_1993", MarmurChester62_93.class);//650
		approximativeMethod.put("Marmur_Chester_1962_1993_corr", MarmurChester62_93.class);//535
		approximativeMethod.put("Marmur_Schildkraut_Doty", MarmurSchildkrautDoty.class);
		approximativeMethod.put("Owen_1969", Owen69.class);
		approximativeMethod.put("Santalucia_1998", Santalucia98.class);
		approximativeMethod.put("Wetmur_1991", Wetmur91.class);
	}
	
	private void initializeCricksMethods(){
		cricksMethod.put("Allawi_Santalucia_1997", AllawiSantalucia97.class);
		cricksMethod.put("Beslauer_1986", Breslauer86.class);
		cricksMethod.put("Freier_1986", Freier86.class);
		cricksMethod.put("Santalucia_2004", Santalucia04.class);
		cricksMethod.put("Santalucia_1996", Santalucia96.class);
		cricksMethod.put("Sugimoto_1995", Sugimoto95.class);
		cricksMethod.put("Tanaka_2004", Tanaka04.class);
		cricksMethod.put("Turner_2006", Turner06.class);
		cricksMethod.put("Xia_1998", Xia98.class);
	}
	
	private void initializeSingleMismatchMethods(){
		singleMismatchMethod.put("Allawi_Santalucia_Peyret_1997_1998_1999", AllawiSantaluciaPeyret97_98_99mm.class);
		singleMismatchMethod.put("Znosco_2007", Znosco07mm.class);
		singleMismatchMethod.put("Znosco_2008", Znosco08mm.class);
		singleMismatchMethod.put("Turner_2006", Turner06mm.class);
	}
	
	private void initializeTandemMismatchMethods(){
		tandemMismatchMethod.put("Allawi_Santalucia_Peyret_1997_1998_1999", AllawiSantaluciaPeyret97_98_99tanmm.class);
		tandemMismatchMethod.put("Turner_1999_2006", Turner99_06tanmm.class);
	}
	
	private void initializeWoddleMismatchMethods(){
		woddleMethod.put("Turner_1999", Turner99Wobble.class);
	}
	
	private void initializeInternalLoopMethods(){
		internalLoopMethod.put("Turner_2006", Turner06InternalLoop.class);
		internalLoopMethod.put("Santalucia_2004", Santalucia04InternalLoop.class);
		internalLoopMethod.put("Znosco_2007", Znosco071x2Loop.class);
	}
	
	private void initializeSingleBulgeLoopMethods(){
		singleBulgeLoopMethod.put("Turner_2006", Turner99_06SingleBulgeLoop.class);
		singleBulgeLoopMethod.put("Santalucia_2004", Santalucia04SingleBulgeLoop.class);
		singleBulgeLoopMethod.put("Serra_2007", Serra07SingleBulgeLoop.class);
		singleBulgeLoopMethod.put("Tanaka_2004", Tanaka04SingleBulgeLoop.class);

	}
	
	private void initializeLongBulgeLoopMethods(){
		longBulgeLoopMethod.put("Turner_2006", Turner99_06LongBulgeLoop.class);
		longBulgeLoopMethod.put("Santalucia_2004", Santalucia04LongBulgeLoop.class);
	}
	
	private void initializeSingleDanglingEndMethods(){
		singleDangingEndMethod.put("Bommarito_2000", Bommarito00SingleDanglingEnd.class);
		longBulgeLoopMethod.put("Serra_2006_2008", Serra06_08SingleDanglingEnd.class);
	}
	
	private void initializeDoubleDanglingEndMethods(){
		doubleDangingEndMethod.put("Serra_2005", Serra05DoubleDanglingEnd.class);
		doubleDangingEndMethod.put("Serra_2006", Serra06DoubleDanglingEnd.class);
	}
	
	private void initializeLongDanglingEndMethods(){
		longDangingEndMethod.put("Sugimoto_2002_dna", Sugimoto02DNADanglingEnd.class);
		longDangingEndMethod.put("Sugimoto_2002_rna", Sugimoto02RNADanglingEnd.class);
	}
	
	private void initializeCNGRepeatsMethods(){
		CNGRepeatsMethod.put("Sugimoto_2002_dna", Broda05CNGRepeats.class);
	}
	
	private void initializeInosineMethods(){
		inosineMethod.put("Santalucia_2005", Santalucia05Inosine.class);
		inosineMethod.put("Znosco_2007", Znosco07Inosine.class);
	}
	
	private void initializeAzobenzeneMethods(){
		azobenzeneMethod.put("Asanuma_2005", Asanuma05Azobenzene.class);
	}
	
	private void initializeLockedAcidMethods(){
		lockedAcidMethod.put("McTigue_2004", McTigue04LockedAcid.class);
	}
	
	private void initializeHydroxyadenosineMethods(){
		hydroxyadenosineMethod.put("Sugimoto_2001", Sugimoto01Hydroxyadenine.class);
	}
	
	private void initializeDeoxyadenosineMethods(){
		deoxyadenosineMethod.put("Sugimoto_2005", Sugimoto05Deoxyadenosine.class);
	}
	
	private void initializePartialCalculMethods(){
		PartialCalculMethods.put(OptionManagement.azobenzeneMethod, azobenzeneMethod);
		PartialCalculMethods.put(OptionManagement.CNGMethod, CNGRepeatsMethod);
		PartialCalculMethods.put(OptionManagement.deoxyadenineMethod, deoxyadenosineMethod);
		PartialCalculMethods.put(OptionManagement.doubleDanglingEndMethod, doubleDangingEndMethod);
		PartialCalculMethods.put(OptionManagement.hydroxyadenineMethod, hydroxyadenosineMethod);
		PartialCalculMethods.put(OptionManagement.inosineMethod, inosineMethod);
		PartialCalculMethods.put(OptionManagement.internalLoopMethod, internalLoopMethod);
		PartialCalculMethods.put(OptionManagement.lockedAcidMethod, lockedAcidMethod);
		PartialCalculMethods.put(OptionManagement.longBulgeLoopMethod, longBulgeLoopMethod);
		PartialCalculMethods.put(OptionManagement.longDanglingEndMethod, longDangingEndMethod);
		PartialCalculMethods.put(OptionManagement.NNMethod, cricksMethod);
		PartialCalculMethods.put(OptionManagement.singleBulgeLoopMethod, singleBulgeLoopMethod);
		PartialCalculMethods.put(OptionManagement.singleDanglingEndMethod, singleDangingEndMethod);
		PartialCalculMethods.put(OptionManagement.singleMismatchMethod, singleMismatchMethod);
		PartialCalculMethods.put(OptionManagement.tandemMismatchMethod, tandemMismatchMethod);
		PartialCalculMethods.put(OptionManagement.wobbleBaseMethod, woddleMethod);

	}
	
	private HashMap<String , Class<? extends PartialCalculMethod>> getPartialCalculMethodHashMap(String optionName){
		
		return PartialCalculMethods.get(optionName);
	}
	
	public PartialCalculMethod getPartialCalculMethod(String optionName, String methodName){
		
		if (methodName != null){
			PartialCalculMethod method;
			try {
				if (Helper.useOtherDataFile(methodName)){
					methodName = Helper.getOptionFileName(methodName);
				}
				method = getPartialCalculMethodHashMap(optionName).get(methodName).newInstance();
				
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
			method = NaEqMethod.get(methodName).newInstance();
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
			method = completCalculMethod.get(methodName).newInstance();
			
			if (method == null){
				int thres = Integer.getInteger(optionSet.get(OptionManagement.threshold));
				String seq = optionSet.get(OptionManagement.sequence);
				String seq2 = optionSet.get(OptionManagement.complementarySequence);
				int duplexLength = Math.min(seq.length(),seq2.length());
				
				if (duplexLength > thres){
					initializeApproximativeMethods();
					
					methodName = optionSet.get(OptionManagement.approximativeMode);
					try {
						method = approximativeMethod.get(methodName).newInstance();
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
