package melting.configuration;

import java.util.HashMap;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.CNGRepeatsMethods.Broda05CNGRepeats;
import melting.DMSOCorrections.Ahsen01DMSOCorrection;
import melting.DMSOCorrections.Cullen76DMSOCorrection;
import melting.DMSOCorrections.Escara80DMSOCorrection;
import melting.DMSOCorrections.Musielski81DMSOCorrection;
import melting.InternalLoopMethod.Santalucia04InternalLoop;
import melting.InternalLoopMethod.Turner06InternalLoop;
import melting.InternalLoopMethod.Znosko071x2Loop;
import melting.MagnesiumCorrections.Owczarzy08MagnesiumCorrection;
import melting.MagnesiumCorrections.Tan06MagnesiumCorrection;
import melting.MagnesiumCorrections.Tan07MagnesiumCorrection;
import melting.MixedNaMgCorrections.Owczarzy08MixedNaMgCorrection;
import melting.MixedNaMgCorrections.Tan07MixedNaMgCorrection;
import melting.approximativeMethods.Ahsen01;
import melting.approximativeMethods.ApproximativeMode;
import melting.approximativeMethods.MarmurChester62_93;
import melting.approximativeMethods.MarmurSchildkrautDoty;
import melting.approximativeMethods.Owen69;
import melting.approximativeMethods.Santalucia98;
import melting.approximativeMethods.WetmurDNA91;
import melting.approximativeMethods.WetmurDNARNA91;
import melting.approximativeMethods.WetmurRNA91;
import melting.calculMethodInterfaces.CompletCalculMethod;
import melting.calculMethodInterfaces.CorrectionMethod;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.calculMethodInterfaces.SodiumEquivalentMethod;
import melting.cricksNNMethods.AllawiSantalucia97;
import melting.cricksNNMethods.Breslauer86;
import melting.cricksNNMethods.Freier86;
import melting.cricksNNMethods.Santalucia04;
import melting.cricksNNMethods.Santalucia96;
import melting.cricksNNMethods.Sugimoto95;
import melting.cricksNNMethods.Sugimoto96;
import melting.cricksNNMethods.Tanaka04;
import melting.cricksNNMethods.Turner06;
import melting.cricksNNMethods.Xia98;
import melting.exceptions.MethodNotApplicableException;
import melting.exceptions.NoExistingMethodException;
import melting.formamideCorrections.Blake96FormamideCorrection;
import melting.formamideCorrections.FormamideLinearMethod;
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
import melting.singleMismatchMethods.Znosko07mm;
import melting.singleMismatchMethods.Znosko08mm;
import melting.sodiumCorrections.Ahsen01SodiumCorrection;
import melting.sodiumCorrections.FrankKamenetskii71SodiumCorrection;
import melting.sodiumCorrections.MarmurSchildkrautDoty98_62SodiumCorrection;
import melting.sodiumCorrections.Owczarzy04SodiumCorrection19;
import melting.sodiumCorrections.Owczarzy04SodiumCorrection20;
import melting.sodiumCorrections.Owczarzy04SodiumCorrection21;
import melting.sodiumCorrections.Owczarzy04SodiumCorrection22;
import melting.sodiumCorrections.Santalucia96SodiumCorrection;
import melting.sodiumCorrections.Santalucia98_04SodiumCorrection;
import melting.sodiumCorrections.SchildkrautLifson65SodiumCorrection;
import melting.sodiumCorrections.Tan06SodiumCorrection;
import melting.sodiumCorrections.Tan07SodiumCorrection;
import melting.sodiumCorrections.Wetmur91SodiumCarrection;
import melting.sodiumEquivalence.Ahsen01_NaEquivalent;
import melting.sodiumEquivalence.Mitsuhashi96NaEquivalent;
import melting.sodiumEquivalence.Peyret00_NaEquivalent;
import melting.tandemMismatchMethod.AllawiSantaluciaPeyret97_98_99tanmm;
import melting.tandemMismatchMethod.Turner99_06tanmm;
import melting.wobbleNNMethod.Santalucia05Inosine;
import melting.wobbleNNMethod.Turner99Wobble;
import melting.wobbleNNMethod.Znosko07Inosine;

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
	private static HashMap<String, Class<? extends CorrectionMethod>> ionCorrection = new HashMap<String, Class<? extends CorrectionMethod>>();
	private static HashMap<String, Class<? extends CorrectionMethod>> DMSOCorrection = new HashMap<String, Class<? extends CorrectionMethod>>();
	private static HashMap<String, Class<? extends CorrectionMethod>> formamideCorrection = new HashMap<String, Class<? extends CorrectionMethod>>();
	private static HashMap<String, HashMap<String, Class<? extends CorrectionMethod>>> otherCorrection = new HashMap<String, HashMap<String, Class<? extends CorrectionMethod>>>();

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
		
		initializeIonCorrectionMethod();
		initializeDMSOCorrectionMethod();
		initializeFormamideCorrectionMethod();
		initializeOtherCorrectionMethod();
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
		approximativeMethod.put("Wetmurdna_1991", WetmurDNA91.class);
		approximativeMethod.put("Wetmurrna_1991", WetmurRNA91.class);
		approximativeMethod.put("Wetmurdnarna_1991", WetmurDNARNA91.class);
	}
	
	private void initializeCricksMethods(){
		cricksMethod.put("Allawi_Santalucia_1997", AllawiSantalucia97.class);
		cricksMethod.put("Breslauer_1986", Breslauer86.class);
		cricksMethod.put("Freier_1986", Freier86.class);
		cricksMethod.put("Santalucia_2004", Santalucia04.class);
		cricksMethod.put("Santalucia_1996", Santalucia96.class);
		cricksMethod.put("Sugimoto_1995", Sugimoto95.class);
		cricksMethod.put("Sugimoto_1996", Sugimoto96.class);
		cricksMethod.put("Tanaka_2004", Tanaka04.class);
		cricksMethod.put("Turner_2006", Turner06.class);
		cricksMethod.put("Xia_1998", Xia98.class);
	}
	
	private void initializeSingleMismatchMethods(){
		singleMismatchMethod.put("Allawi_Santalucia_Peyret_1997_1998_1999", AllawiSantaluciaPeyret97_98_99mm.class);
		singleMismatchMethod.put("Znosko_2007", Znosko07mm.class);
		singleMismatchMethod.put("Znosko_2008", Znosko08mm.class);
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
		internalLoopMethod.put("Znosko_2007", Znosko071x2Loop.class);
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
		singleDangingEndMethod.put("Serra_2006_2008", Serra06_08SingleDanglingEnd.class);
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
		CNGRepeatsMethod.put("Broda_2005", Broda05CNGRepeats.class);
	}
	
	private void initializeInosineMethods(){
		inosineMethod.put("Santalucia_2005", Santalucia05Inosine.class);
		inosineMethod.put("Znosko_2007", Znosko07Inosine.class);
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
	
	private void initializeIonCorrectionMethod(){
		ionCorrection.put("Ahsen_2001", Ahsen01SodiumCorrection.class);
		ionCorrection.put("Kamenetskii_1971", FrankKamenetskii71SodiumCorrection.class);
		ionCorrection.put("Marmur_Schildkraut_Doty_1962_1998", MarmurSchildkrautDoty98_62SodiumCorrection.class);
		ionCorrection.put("Owczarzy_19_2004", Owczarzy04SodiumCorrection19.class);
		ionCorrection.put("Owczarzy_20_2004", Owczarzy04SodiumCorrection20.class);
		ionCorrection.put("Owczarzy_21_2004", Owczarzy04SodiumCorrection21.class);
		ionCorrection.put("Owczarzy_22_2004", Owczarzy04SodiumCorrection22.class);
		ionCorrection.put("Santalucia_1996", Santalucia96SodiumCorrection.class);
		ionCorrection.put("Santalucia_1998_2004", Santalucia98_04SodiumCorrection.class);
		ionCorrection.put("Schildkraut_Lifson_1965", SchildkrautLifson65SodiumCorrection.class);
		ionCorrection.put("Tan_2006_Na", Tan06SodiumCorrection.class);
		ionCorrection.put("Tan_2007_Na", Tan07SodiumCorrection.class);
		ionCorrection.put("Wetmur_1991", Wetmur91SodiumCarrection.class);
		ionCorrection.put("Owczarzy_2008_Mg", Owczarzy08MagnesiumCorrection.class);
		ionCorrection.put("Tan_2006_Mg", Tan06MagnesiumCorrection.class);
		ionCorrection.put("Tan_2007_Mg", Tan07MagnesiumCorrection.class);
		ionCorrection.put("Owczarzy_2008_mixed", Owczarzy08MixedNaMgCorrection.class);
		ionCorrection.put("Tan_2007_mixed", Tan07MixedNaMgCorrection.class);
	}
	
	private void initializeDMSOCorrectionMethod(){
		DMSOCorrection.put("Ahsen_2001", Ahsen01DMSOCorrection.class);
		DMSOCorrection.put("Cullen_1976", Cullen76DMSOCorrection.class);
		DMSOCorrection.put("Escara_1980", Escara80DMSOCorrection.class);
		DMSOCorrection.put("Musielski_1981", Musielski81DMSOCorrection.class);
	}
	
	private void initializeFormamideCorrectionMethod(){
		formamideCorrection.put("linear_correction", FormamideLinearMethod.class);
		formamideCorrection.put("Blake_1996", Blake96FormamideCorrection.class);
	}
	
	private void initializeOtherCorrectionMethod(){
		otherCorrection.put(OptionManagement.DMSOCorrection, DMSOCorrection);
		otherCorrection.put(OptionManagement.formamideCorrection, formamideCorrection);
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
		if (PartialCalculMethods.get(optionName) == null){
			throw new NoExistingMethodException("No method is implemented for the option " + optionName + ".");
		}
		return PartialCalculMethods.get(optionName);
	}
	
	public PartialCalculMethod getPartialCalculMethod(String optionName, String methodName){
		
		if (methodName != null){
			PartialCalculMethod method;
			try {
				if (Helper.useOtherDataFile(methodName)){
					methodName = Helper.getOptionFileName(methodName);
				}
				if (getPartialCalculMethodHashMap(optionName).get(methodName) == null){
					throw new NoExistingMethodException("We don't know the method " + methodName);
				}

				method = getPartialCalculMethodHashMap(optionName).get(methodName).newInstance();
				return method;
			} catch (InstantiationException e) {
				throw new NoExistingMethodException("The calcul method is not implemented yet. Check the option " + optionName);
			} catch (IllegalAccessException e) {
				throw new NoExistingMethodException("The calcul method is not implemented yet. Check the option " + optionName);
			}
		}
		return null;
	}
	
	public SodiumEquivalentMethod getNaEqMethod (HashMap<String, String> optionSet){
		String methodName = optionSet.get(OptionManagement.NaEquivalentMethod);
		
		if (methodName == null){
		throw new NoExistingMethodException("No method is implemented for the option " + OptionManagement.NaEquivalentMethod + ".");
		}
		SodiumEquivalentMethod method;
		try {
			if (NaEqMethod.get(methodName) == null){
				throw new NoExistingMethodException("We don't know the method " + methodName);
			}
			method = NaEqMethod.get(methodName).newInstance();
			if (method.isApplicable(optionSet)) {
				return method;
			}
			else {
				throw new MethodNotApplicableException("The sodium equivalent method (option " + OptionManagement.NaEquivalentMethod + ") is not applicable with this environment.");
			}
		} catch (InstantiationException e) {
			throw new NoExistingMethodException("The sodium equivalence method is not implemented yet. Check the option " + OptionManagement.NaEquivalentMethod);
		} catch (IllegalAccessException e) {
			throw new NoExistingMethodException("The sodium equivalence method is not implemented yet. Check the option " + OptionManagement.NaEquivalentMethod);
		}
	}
	
	public CorrectionMethod getIonCorrectionMethod (Environment environment){
		if (environment.getOptions().containsKey(OptionManagement.ionCorrection)){
			String methodName = environment.getOptions().get(OptionManagement.ionCorrection);
			
			if (methodName == null){
				throw new NoExistingMethodException("No method is implemented for the option " + OptionManagement.ionCorrection + ".");
			}
			CorrectionMethod method;
			try {
				if (ionCorrection.get(methodName) == null){
					throw new NoExistingMethodException("We don't know the method " + methodName);
				}
				method = ionCorrection.get(methodName).newInstance();
				if (method.isApplicable(environment)) {
					return method;
				}
				else {
					throw new MethodNotApplicableException("The ion correction method (option " + OptionManagement.ionCorrection + ") is not applicable with this environment.");
				}
			} catch (InstantiationException e) {
				throw new NoExistingMethodException("The ion correction method is not implemented yet. Check the option " + OptionManagement.ionCorrection);
			} catch (IllegalAccessException e) {
				throw new NoExistingMethodException("The ion correction method is not implemented yet. Check the option " + OptionManagement.ionCorrection);
			}
		}
		else{
			double monovalent = environment.getNa() + environment.getK() + environment.getTris() / 2;
			
			if (environment.getHybridization().equals("dnarna") || environment.getHybridization().equals("rnadna")){
				return new Wetmur91SodiumCarrection();
			}
			else if (environment.getHybridization().equals("dnadna") == false && environment.getHybridization().equals("rnarna") == false && environment.getHybridization().equals("mrnarna") == false){
				throw new NoExistingMethodException("There is no existing ion correction method (option " + OptionManagement.ionCorrection + ") for this type of hybridization.");
			}
			
			if (monovalent == 0){
				if (environment.getHybridization().equals("dnadna")){
					return new Owczarzy08MagnesiumCorrection();
				}
				else if (environment.getHybridization().equals("rnarna") || environment.getHybridization().equals("mrnarna")){
					return new Tan07MagnesiumCorrection();
				}
			}
			else {
				double Mg = environment.getMg() - environment.getDNTP();
				double ratio = Math.sqrt(Mg) / monovalent;
				
				if (ratio < 0.22){
					if (environment.getHybridization().equals("dnadna")){
						return new Owczarzy04SodiumCorrection22();
					}
					else if (environment.getHybridization().equals("rnarna") || environment.getHybridization().equals("mrnarna")){
						return new Tan07SodiumCorrection();
					}
				}
				else{
					if (ratio < 6.0){
						if (environment.getHybridization().equals("dnadna")){
							return new Owczarzy08MixedNaMgCorrection();
						}
						else if (environment.getHybridization().equals("rnarna") || environment.getHybridization().equals("mrnarna")){
							return new Tan07MixedNaMgCorrection();
						}
					}
					else {
						if (environment.getHybridization().equals("dnadna")){
							return new Owczarzy08MagnesiumCorrection();
						}
						else if (environment.getHybridization().equals("rnarna") || environment.getHybridization().equals("mrnarna")){
							return new Tan07MagnesiumCorrection();
						}
					}
				}
			}
			return null;
		}
	}
	
	public CompletCalculMethod getCompletCalculMethod(HashMap<String, String> optionSet){
		
		String methodName = optionSet.get(OptionManagement.completMethod);
		if (methodName == null){
			throw new NoExistingMethodException("No method is implemented for the option " + OptionManagement.completMethod + ".");
		}
	
		CompletCalculMethod method = null;

			if (completCalculMethod.get(methodName) == null){
				int thres = Integer.parseInt(optionSet.get(OptionManagement.threshold));
				String seq = optionSet.get(OptionManagement.sequence);
				String seq2 = optionSet.get(OptionManagement.complementarySequence);
				int duplexLength = Math.min(seq.length(),seq2.length());

				if (duplexLength > thres){
					methodName = optionSet.get(OptionManagement.approximativeMode);

					try {
						if (approximativeMethod.get(methodName) == null){
							throw new NoExistingMethodException("We don't know the method " + methodName);
						}
						method = approximativeMethod.get(methodName).newInstance();
						method.setUpVariable(optionSet);
					} catch (InstantiationException e) {
						throw new NoExistingMethodException("The approximative method is not implemented yet. Check the option " + OptionManagement.approximativeMode);
					} catch (IllegalAccessException e) {
						throw new NoExistingMethodException("The approximative method is not implemented yet. Check the option " + OptionManagement.approximativeMode);
					}
				}
				else {

					method = new NearestNeighborMode();
					
					method.setUpVariable(optionSet);
				}
			}
			else if (methodName.equals("approximate")){
				methodName = optionSet.get(OptionManagement.approximativeMode);

				try {
					if (approximativeMethod.get(methodName) == null){
						throw new NoExistingMethodException("We don't know the method " + methodName);
					}
					method = approximativeMethod.get(methodName).newInstance();
					method.setUpVariable(optionSet);
				} catch (InstantiationException e) {
					throw new NoExistingMethodException("The approximative method is not implemented yet. Check the option " + OptionManagement.approximativeMode);
				} catch (IllegalAccessException e) {
					throw new NoExistingMethodException("The approximative method is not implemented yet. Check the option " + OptionManagement.approximativeMode);
				}
			}
			else {
				method = new NearestNeighborMode();
				
				method.setUpVariable(optionSet);
			}
			if (method.isApplicable() && method != null) {

				return method;
			}
			else {
				throw new MethodNotApplicableException("The melting temperature calcul method (option " + OptionManagement.completMethod + ") is not applicable with this environment.");
			}
	}
	
	public CorrectionMethod getCorrectionMethod (String optionName, String methodName){
		
		if (methodName == null){
			throw new NoExistingMethodException("No method is implemented for the option " + OptionManagement.DMSOCorrection + "or" + OptionManagement.formamideCorrection + ".");
		}
		CorrectionMethod method;
		try {
			if (otherCorrection.get(optionName).get(methodName) == null){
			throw new NoExistingMethodException("We don't know the method " + methodName);
		}
			method = otherCorrection.get(optionName).get(methodName).newInstance();
			return method;
			
		} catch (InstantiationException e) {
			throw new NoExistingMethodException("The ion correction method is not implemented yet. Check the option " + OptionManagement.ionCorrection);
		} catch (IllegalAccessException e) {
			throw new NoExistingMethodException("The ion correction method is not implemented yet. Check the option " + OptionManagement.ionCorrection);
		}
	}
	
	public ThermoResult computeOtherMeltingCorrections(Environment environment){
		if (environment.getDMSO() > 0){
			CorrectionMethod DMSOCorrection = getCorrectionMethod(OptionManagement.DMSOCorrection, environment.getOptions().get(OptionManagement.DMSOCorrection));
			
			if (DMSOCorrection == null){
				throw new NoExistingMethodException("There is no implemented DMSO correction.");
			}
			else if (DMSOCorrection.isApplicable(environment)){
				environment.setResult(DMSOCorrection.correctMeltingResult(environment));
			}
			else {
				throw new MethodNotApplicableException("The DMSO correction is not applicable with this environment (option " + OptionManagement.DMSOCorrection + ").");
			}
		}
		if (environment.getFormamide() > 0){
			CorrectionMethod formamideCorrection = getCorrectionMethod(OptionManagement.formamideCorrection, environment.getOptions().get(OptionManagement.formamideCorrection));
			
			if (formamideCorrection == null){
				throw new NoExistingMethodException("There is no implemented formamide correction.");
			}
			else if (formamideCorrection.isApplicable(environment)){
				environment.setResult(formamideCorrection.correctMeltingResult(environment));
			}
			else {
				throw new MethodNotApplicableException("The formamide correction is not applicable with this environment (option " + OptionManagement.formamideCorrection + ").");
			}
		}
		
		return environment.getResult();
	}
}
