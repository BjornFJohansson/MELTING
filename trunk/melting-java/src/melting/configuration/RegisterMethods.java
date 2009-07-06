package melting.configuration;

import java.util.HashMap;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.InternalLoops.Santalucia04InternalLoop;
import melting.InternalLoops.Turner06InternalLoop;
import melting.InternalLoops.Znosko071x2Loop;
import melting.approximativeMethods.Ahsen01;
import melting.approximativeMethods.ApproximativeMode;
import melting.approximativeMethods.MarmurChester62_93;
import melting.approximativeMethods.MarmurSchildkrautDoty;
import melting.approximativeMethods.Owen69;
import melting.approximativeMethods.Santalucia98;
import melting.approximativeMethods.WetmurDNA91;
import melting.approximativeMethods.WetmurDNARNA91;
import melting.approximativeMethods.WetmurRNA91;
import melting.cngPatterns.Broda05CNGRepeats;
import melting.cricksPair.AllawiSantalucia97;
import melting.cricksPair.Breslauer86;
import melting.cricksPair.Freier86;
import melting.cricksPair.Santalucia04;
import melting.cricksPair.Santalucia96;
import melting.cricksPair.Sugimoto95;
import melting.cricksPair.Sugimoto96;
import melting.cricksPair.Tanaka04;
import melting.cricksPair.Turner06;
import melting.cricksPair.Xia98;
import melting.dmsoCorrections.Ahsen01DMSOCorrection;
import melting.dmsoCorrections.Cullen76DMSOCorrection;
import melting.dmsoCorrections.Escara80DMSOCorrection;
import melting.dmsoCorrections.Musielski81DMSOCorrection;
import melting.exceptions.MethodNotApplicableException;
import melting.exceptions.NoExistingMethodException;
import melting.formamideCorrections.Blake96FormamideCorrection;
import melting.formamideCorrections.FormamideLinearMethod;
import melting.longBulge.Santalucia04LongBulgeLoop;
import melting.longBulge.Turner99_06LongBulgeLoop;
import melting.longDanglingEnds.Sugimoto02DNADanglingEnd;
import melting.longDanglingEnds.Sugimoto02RNADanglingEnd;
import melting.magnesiumCorrections.Owczarzy08MagnesiumCorrection;
import melting.magnesiumCorrections.Tan06MagnesiumCorrection;
import melting.magnesiumCorrections.Tan07MagnesiumCorrection;
import melting.methodInterfaces.CompletCalculMethod;
import melting.methodInterfaces.CorrectionMethod;
import melting.methodInterfaces.PartialCalculMethod;
import melting.methodInterfaces.SodiumEquivalentMethod;
import melting.mixedNaMgCorrections.Owczarzy08MixedNaMgCorrection;
import melting.mixedNaMgCorrections.Tan07MixedNaMgCorrection;
import melting.nearestNeighborModel.NearestNeighborMode;
import melting.secondDanglingEnds.Serra05DoubleDanglingEnd;
import melting.secondDanglingEnds.Serra06DoubleDanglingEnd;
import melting.singleBulge.Santalucia04SingleBulgeLoop;
import melting.singleBulge.Serra07SingleBulgeLoop;
import melting.singleBulge.Tanaka04SingleBulgeLoop;
import melting.singleBulge.Turner99_06SingleBulgeLoop;
import melting.singleDanglingEnds.Bommarito00SingleDanglingEnd;
import melting.singleDanglingEnds.Serra06_08SingleDanglingEnd;
import melting.singleMismatch.AllawiSantaluciaPeyret97_98_99mm;
import melting.singleMismatch.Turner06mm;
import melting.singleMismatch.Znosko07mm;
import melting.singleMismatch.Znosko08mm;
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
import melting.sodiumCorrections.Wetmur91SodiumCorrection;
import melting.sodiumEquivalence.Ahsen01_NaEquivalent;
import melting.sodiumEquivalence.Mitsuhashi96NaEquivalent;
import melting.sodiumEquivalence.Peyret00_NaEquivalent;
import melting.specificAcids.Asanuma05Azobenzene;
import melting.specificAcids.McTigue04LockedAcid;
import melting.specificAcids.Sugimoto01Hydroxyadenine;
import melting.tandemMismatches.AllawiSantaluciaPeyret97_98_99tanmm;
import melting.tandemMismatches.Turner99_06tanmm;
import melting.wobble.Santalucia05Inosine;
import melting.wobble.Turner99Wobble;
import melting.wobble.Znosko07Inosine;

public class RegisterMethods {
	
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
	private static HashMap<String, HashMap<String, Class<? extends PartialCalculMethod>>> PartialCalculMethods = new HashMap<String, HashMap<String, Class<? extends PartialCalculMethod>>>();
	private static HashMap<String, Class<? extends CorrectionMethod>> ionCorrection = new HashMap<String, Class<? extends CorrectionMethod>>();
	private static HashMap<String, Class<? extends CorrectionMethod>> DMSOCorrection = new HashMap<String, Class<? extends CorrectionMethod>>();
	private static HashMap<String, Class<? extends CorrectionMethod>> formamideCorrection = new HashMap<String, Class<? extends CorrectionMethod>>();
	private static HashMap<String, HashMap<String, Class<? extends CorrectionMethod>>> otherCorrection = new HashMap<String, HashMap<String, Class<? extends CorrectionMethod>>>();

	public RegisterMethods(){
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
		initializeHydroxyadenosineMethods();
		initializeLockedAcidMethods();
		
		initializePartialCalculMethods();
		
		initializeIonCorrectionMethod();
		initializeDMSOCorrectionMethod();
		initializeFormamideCorrectionMethod();
		initializeOtherCorrectionMethod();
	}
	
	private void initializeNaEqMethods(){
		NaEqMethod.put("ahs01", Ahsen01_NaEquivalent.class);
		NaEqMethod.put("pey00", Peyret00_NaEquivalent.class);
		NaEqMethod.put("mit96", Mitsuhashi96NaEquivalent.class);
	}
	
	private void initializeCompletCalculMethods(){
		completCalculMethod.put("A", ApproximativeMode.class);
		completCalculMethod.put("NN", NearestNeighborMode.class);
		completCalculMethod.put("def", null);
	}
	
	private void initializeApproximativeMethods(){
		approximativeMethod.put("ahs01", Ahsen01.class);
		approximativeMethod.put("che93", MarmurChester62_93.class);//650
		approximativeMethod.put("che93corr", MarmurChester62_93.class);//535
		approximativeMethod.put("schdot", MarmurSchildkrautDoty.class);
		approximativeMethod.put("owe69", Owen69.class);
		approximativeMethod.put("san98", Santalucia98.class);
		approximativeMethod.put("wetdna91", WetmurDNA91.class);
		approximativeMethod.put("wetrna91", WetmurRNA91.class);
		approximativeMethod.put("wetdnarna91", WetmurDNARNA91.class);
	}
	
	private void initializeCricksMethods(){
		cricksMethod.put("all97", AllawiSantalucia97.class);
		cricksMethod.put("bre86", Breslauer86.class);
		cricksMethod.put("fre86", Freier86.class);
		cricksMethod.put("san04", Santalucia04.class);
		cricksMethod.put("san96", Santalucia96.class);
		cricksMethod.put("sug95", Sugimoto95.class);
		cricksMethod.put("sug96", Sugimoto96.class);
		cricksMethod.put("tan04", Tanaka04.class);
		cricksMethod.put("tur06", Turner06.class);
		cricksMethod.put("xia98", Xia98.class);
	}
	
	private void initializeSingleMismatchMethods(){
		singleMismatchMethod.put("allsanpey", AllawiSantaluciaPeyret97_98_99mm.class);
		singleMismatchMethod.put("zno07", Znosko07mm.class);
		singleMismatchMethod.put("zno08", Znosko08mm.class);
		singleMismatchMethod.put("tur06", Turner06mm.class);
	}
	
	private void initializeTandemMismatchMethods(){
		tandemMismatchMethod.put("allsanpey", AllawiSantaluciaPeyret97_98_99tanmm.class);
		tandemMismatchMethod.put("tur06", Turner99_06tanmm.class);
	}
	
	private void initializeWoddleMismatchMethods(){
		woddleMethod.put("tur99", Turner99Wobble.class);
	}
	
	private void initializeInternalLoopMethods(){
		internalLoopMethod.put("tur06", Turner06InternalLoop.class);
		internalLoopMethod.put("san04", Santalucia04InternalLoop.class);
		internalLoopMethod.put("zno07", Znosko071x2Loop.class);
	}
	
	private void initializeSingleBulgeLoopMethods(){
		singleBulgeLoopMethod.put("tur06", Turner99_06SingleBulgeLoop.class);
		singleBulgeLoopMethod.put("san04", Santalucia04SingleBulgeLoop.class);
		singleBulgeLoopMethod.put("ser07", Serra07SingleBulgeLoop.class);
		singleBulgeLoopMethod.put("tan04", Tanaka04SingleBulgeLoop.class);

	}
	
	private void initializeLongBulgeLoopMethods(){
		longBulgeLoopMethod.put("tur06", Turner99_06LongBulgeLoop.class);
		longBulgeLoopMethod.put("san04", Santalucia04LongBulgeLoop.class);
	}
	
	private void initializeSingleDanglingEndMethods(){
		singleDangingEndMethod.put("bom00", Bommarito00SingleDanglingEnd.class);
		singleDangingEndMethod.put("ser08", Serra06_08SingleDanglingEnd.class);
		singleDangingEndMethod.put("sugdna02", Sugimoto02DNADanglingEnd.class);
		singleDangingEndMethod.put("sugrna02", Sugimoto02RNADanglingEnd.class);
	}
	
	private void initializeDoubleDanglingEndMethods(){
		doubleDangingEndMethod.put("ser05", Serra05DoubleDanglingEnd.class);
		doubleDangingEndMethod.put("ser06", Serra06DoubleDanglingEnd.class);
		doubleDangingEndMethod.put("sugdna02", Sugimoto02DNADanglingEnd.class);
		doubleDangingEndMethod.put("sugrna02", Sugimoto02RNADanglingEnd.class);

	}
	
	private void initializeLongDanglingEndMethods(){
		longDangingEndMethod.put("sugdna02", Sugimoto02DNADanglingEnd.class);
		longDangingEndMethod.put("sugrna02", Sugimoto02RNADanglingEnd.class);
	}
	
	private void initializeCNGRepeatsMethods(){
		CNGRepeatsMethod.put("bro05", Broda05CNGRepeats.class);
	}
	
	private void initializeInosineMethods(){
		inosineMethod.put("san05", Santalucia05Inosine.class);
		inosineMethod.put("zno07", Znosko07Inosine.class);
	}
	
	private void initializeAzobenzeneMethods(){
		azobenzeneMethod.put("asa05", Asanuma05Azobenzene.class);
	}
	
	private void initializeLockedAcidMethods(){
		lockedAcidMethod.put("mct04", McTigue04LockedAcid.class);
	}
	
	private void initializeHydroxyadenosineMethods(){
		hydroxyadenosineMethod.put("sug01", Sugimoto01Hydroxyadenine.class);
	}
	
	private void initializeIonCorrectionMethod(){
		ionCorrection.put("ahs01", Ahsen01SodiumCorrection.class);
		ionCorrection.put("kam71", FrankKamenetskii71SodiumCorrection.class);
		ionCorrection.put("marschdot", MarmurSchildkrautDoty98_62SodiumCorrection.class);
		ionCorrection.put("owc1904", Owczarzy04SodiumCorrection19.class);
		ionCorrection.put("owc2004", Owczarzy04SodiumCorrection20.class);
		ionCorrection.put("owc2104", Owczarzy04SodiumCorrection21.class);
		ionCorrection.put("owc2204", Owczarzy04SodiumCorrection22.class);
		ionCorrection.put("san96", Santalucia96SodiumCorrection.class);
		ionCorrection.put("san04", Santalucia98_04SodiumCorrection.class);
		ionCorrection.put("schlif", SchildkrautLifson65SodiumCorrection.class);
		ionCorrection.put("tanna06", Tan06SodiumCorrection.class);
		ionCorrection.put("tanna07", Tan07SodiumCorrection.class);
		ionCorrection.put("wet91", Wetmur91SodiumCorrection.class);
		ionCorrection.put("owcmg08", Owczarzy08MagnesiumCorrection.class);
		ionCorrection.put("tanmg06", Tan06MagnesiumCorrection.class);
		ionCorrection.put("tanmg07", Tan07MagnesiumCorrection.class);
		ionCorrection.put("owcmix08", Owczarzy08MixedNaMgCorrection.class);
		ionCorrection.put("tanmix07", Tan07MixedNaMgCorrection.class);
	}
	
	private void initializeDMSOCorrectionMethod(){
		DMSOCorrection.put("ahs01", Ahsen01DMSOCorrection.class);
		DMSOCorrection.put("cul76", Cullen76DMSOCorrection.class);
		DMSOCorrection.put("esc80", Escara80DMSOCorrection.class);
		DMSOCorrection.put("mus81", Musielski81DMSOCorrection.class);
	}
	
	private void initializeFormamideCorrectionMethod(){
		formamideCorrection.put("lincorr", FormamideLinearMethod.class);
		formamideCorrection.put("bla96", Blake96FormamideCorrection.class);
	}
	
	private void initializeOtherCorrectionMethod(){
		otherCorrection.put(OptionManagement.DMSOCorrection, DMSOCorrection);
		otherCorrection.put(OptionManagement.formamideCorrection, formamideCorrection);
	}
	
	private void initializePartialCalculMethods(){
		PartialCalculMethods.put(OptionManagement.azobenzeneMethod, azobenzeneMethod);
		PartialCalculMethods.put(OptionManagement.CNGMethod, CNGRepeatsMethod);
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
					methodName = Helper.getOptionMethodName(methodName);

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
				return new Wetmur91SodiumCorrection();
			}
			else if (environment.getHybridization().equals("dnadna") == false && environment.getHybridization().equals("rnarna") == false && environment.getHybridization().equals("mrnarna") == false && environment.getHybridization().equals("rnamrna") == false){
				throw new NoExistingMethodException("There is no existing ion correction method (option " + OptionManagement.ionCorrection + ") for this type of hybridization.");
			}
			
			if (monovalent == 0){
				if (environment.getHybridization().equals("dnadna")){
					return new Owczarzy08MagnesiumCorrection();
				}
				else if (environment.getHybridization().equals("rnarna") || environment.getHybridization().equals("mrnarna") || environment.getHybridization().equals("rnamrna")){
					return new Tan07MagnesiumCorrection();
				}
			}
			else {
				double Mg = environment.getMg() - environment.getDNTP();
				double ratio = Math.sqrt(Mg) / monovalent;
				
				if (ratio < 0.22){
					environment.setMg(0.0);
					if (environment.getHybridization().equals("dnadna")){
						return new Owczarzy04SodiumCorrection22();
					}
					else if (environment.getHybridization().equals("rnarna") || environment.getHybridization().equals("mrnarna") || environment.getHybridization().equals("rnamrna")){
						return new Tan07SodiumCorrection();
					}
				}
				else{
					if (ratio < 6.0){
						if (environment.getHybridization().equals("dnadna")){
							return new Owczarzy08MixedNaMgCorrection();
						}
						else if (environment.getHybridization().equals("rnarna") || environment.getHybridization().equals("mrnarna") || environment.getHybridization().equals("rnamrna")){
							return new Tan07MixedNaMgCorrection();
						}
					}
					else {
						if (environment.getHybridization().equals("dnadna")){
							return new Owczarzy08MagnesiumCorrection();
						}
						else if (environment.getHybridization().equals("rnarna") || environment.getHybridization().equals("mrnarna") || environment.getHybridization().equals("rnamrna")){
							return new Tan07MagnesiumCorrection();
						}
					}
				}
			}
			return null;
		}
	}
	
	public CompletCalculMethod getCompletCalculMethod(HashMap<String, String> optionSet){
		
		String methodName = optionSet.get(OptionManagement.globalMethod);
		if (methodName == null){
			throw new NoExistingMethodException("No method is implemented for the option " + OptionManagement.globalMethod + ".");
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
			else if (methodName.equals("A")){
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
				throw new MethodNotApplicableException("The melting temperature calcul method (option " + OptionManagement.globalMethod + ") is not applicable with this environment.");
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
