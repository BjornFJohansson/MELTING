/* This program is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the 
 * License, or (at your option) any later version
                                
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General
 * Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, 
 * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA                                                                  

 *       Marine Dumousseau and Nicolas Lenovere                                                   
 *       EMBL-EBI, neurobiology computational group,                          
*       Cambridge, UK. e-mail: lenov@ebi.ac.uk, marine@ebi.ac.uk        */

package melting.configuration;

import java.util.HashMap;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.approximativeMethods.Ahsen01;
import melting.approximativeMethods.ApproximativeMode;
import melting.approximativeMethods.MarmurChester62_93;
import melting.approximativeMethods.MarmurSchildkrautDoty;
import melting.approximativeMethods.Owen69;
import melting.approximativeMethods.Santalucia98;
import melting.approximativeMethods.WetmurDNA91;
import melting.approximativeMethods.WetmurDNARNA91;
import melting.approximativeMethods.WetmurRNA91;
import melting.exceptions.MethodNotApplicableException;
import melting.exceptions.NoExistingMethodException;
import melting.ionCorrection.magnesiumCorrections.Owczarzy08MagnesiumCorrection;
import melting.ionCorrection.magnesiumCorrections.Tan06MagnesiumCorrection;
import melting.ionCorrection.magnesiumCorrections.Tan07MagnesiumCorrection;
import melting.ionCorrection.mixedNaMgCorrections.Owczarzy08MixedNaMgCorrection;
import melting.ionCorrection.mixedNaMgCorrections.Tan07MixedNaMgCorrection;
import melting.ionCorrection.sodiumCorrections.Ahsen01SodiumCorrection;
import melting.ionCorrection.sodiumCorrections.FrankKamenetskii71SodiumCorrection;
import melting.ionCorrection.sodiumCorrections.MarmurSchildkrautDoty98_62SodiumCorrection;
import melting.ionCorrection.sodiumCorrections.Owczarzy04SodiumCorrection19;
import melting.ionCorrection.sodiumCorrections.Owczarzy04SodiumCorrection20;
import melting.ionCorrection.sodiumCorrections.Owczarzy04SodiumCorrection21;
import melting.ionCorrection.sodiumCorrections.Owczarzy04SodiumCorrection22;
import melting.ionCorrection.sodiumCorrections.Santalucia96SodiumCorrection;
import melting.ionCorrection.sodiumCorrections.Santalucia98_04SodiumCorrection;
import melting.ionCorrection.sodiumCorrections.SchildkrautLifson65SodiumCorrection;
import melting.ionCorrection.sodiumCorrections.Tan06SodiumCorrection;
import melting.ionCorrection.sodiumCorrections.Tan07SodiumCorrection;
import melting.ionCorrection.sodiumCorrections.Wetmur91SodiumCorrection;
import melting.ionCorrection.sodiumEquivalence.Ahsen01_NaEquivalent;
import melting.ionCorrection.sodiumEquivalence.Mitsuhashi96NaEquivalent;
import melting.ionCorrection.sodiumEquivalence.Peyret00_NaEquivalent;
import melting.methodInterfaces.MeltingComputationMethod;
import melting.methodInterfaces.CorrectionMethod;
import melting.methodInterfaces.PatternComputationMethod;
import melting.methodInterfaces.SodiumEquivalentMethod;
import melting.nearestNeighborModel.NearestNeighborMode;
import melting.otherCorrections.dmsoCorrections.Ahsen01DMSOCorrection;
import melting.otherCorrections.dmsoCorrections.Cullen76DMSOCorrection;
import melting.otherCorrections.dmsoCorrections.Escara80DMSOCorrection;
import melting.otherCorrections.dmsoCorrections.Musielski81DMSOCorrection;
import melting.otherCorrections.formamideCorrections.Blake96FormamideCorrection;
import melting.otherCorrections.formamideCorrections.FormamideLinearMethod;
import melting.patternModels.InternalLoops.Santalucia04InternalLoop;
import melting.patternModels.InternalLoops.Turner06InternalLoop;
import melting.patternModels.InternalLoops.Znosko071x2Loop;
import melting.patternModels.cngPatterns.Broda05CNGRepeats;
import melting.patternModels.cricksPair.AllawiSantalucia97;
import melting.patternModels.cricksPair.Breslauer86;
import melting.patternModels.cricksPair.Freier86;
import melting.patternModels.cricksPair.Santalucia04;
import melting.patternModels.cricksPair.Santalucia96;
import melting.patternModels.cricksPair.Sugimoto95;
import melting.patternModels.cricksPair.Sugimoto96;
import melting.patternModels.cricksPair.Tanaka04;
import melting.patternModels.cricksPair.Turner06;
import melting.patternModels.cricksPair.Xia98;
import melting.patternModels.longBulge.Santalucia04LongBulgeLoop;
import melting.patternModels.longBulge.Turner99_06LongBulgeLoop;
import melting.patternModels.longDanglingEnds.Sugimoto02DNADanglingEnd;
import melting.patternModels.longDanglingEnds.Sugimoto02RNADanglingEnd;
import melting.patternModels.secondDanglingEnds.Serra05DoubleDanglingEnd;
import melting.patternModels.secondDanglingEnds.Serra06DoubleDanglingEnd;
import melting.patternModels.singleBulge.Santalucia04SingleBulgeLoop;
import melting.patternModels.singleBulge.Serra07SingleBulgeLoop;
import melting.patternModels.singleBulge.Tanaka04SingleBulgeLoop;
import melting.patternModels.singleBulge.Turner99_06SingleBulgeLoop;
import melting.patternModels.singleDanglingEnds.Bommarito00SingleDanglingEnd;
import melting.patternModels.singleDanglingEnds.Serra06_08SingleDanglingEnd;
import melting.patternModels.singleMismatch.AllawiSantaluciaPeyret97_98_99mm;
import melting.patternModels.singleMismatch.Turner06mm;
import melting.patternModels.singleMismatch.Znosko07mm;
import melting.patternModels.singleMismatch.Znosko08mm;
import melting.patternModels.specificAcids.Asanuma05Azobenzene;
import melting.patternModels.specificAcids.McTigue04LockedAcid;
import melting.patternModels.specificAcids.Sugimoto01Hydroxyadenine;
import melting.patternModels.tandemMismatches.AllawiSantaluciaPeyret97_98_99tanmm;
import melting.patternModels.tandemMismatches.Turner99_06tanmm;
import melting.patternModels.wobble.Santalucia05Inosine;
import melting.patternModels.wobble.Turner99Wobble;
import melting.patternModels.wobble.Znosko07Inosine;

/**
 * This class registers all the methods and models implemented by Melting.
 */
public class RegisterMethods {
	
	// Instance variables
	
	/**
	 * HasMap NaEqMethod : contains all the methods for sodium equivalence.
	 */
	private static HashMap<String, Class<? extends SodiumEquivalentMethod>> NaEqMethod = new HashMap<String, Class<? extends SodiumEquivalentMethod>>();
	
	/**
	 * HasMap approximativeMethod : contains all the methods for approximative computation.
	 */
	private static HashMap<String, Class<? extends ApproximativeMode>> approximativeMethod = new HashMap<String, Class<? extends ApproximativeMode>>();
	
	/**
	 * HasMap cricksMethod : contains all the methods for Crick's pair computation.
	 */
	private static HashMap<String, Class<? extends PatternComputationMethod>> cricksMethod = new HashMap<String, Class<? extends PatternComputationMethod>>();
	
	/**
	 * HasMap meltingComputationMethods : contains all the methods for enthalpy, entropy and melting temperature computation.
	 */
	private static HashMap<String, Class<? extends MeltingComputationMethod>> meltingComputationMethods = new HashMap<String, Class<? extends MeltingComputationMethod>>();
	
	/**
	 * HasMap singleMismatchMethod : contains all the methods for single mismatch computation.
	 */
	private static HashMap<String, Class<? extends PatternComputationMethod>> singleMismatchMethod = new HashMap<String, Class<? extends PatternComputationMethod>>();
	
	/**
	 * HasMap tandemMismatchMethod : contains all the methods for tandem mismatches computation.
	 */
	private static HashMap<String, Class<? extends PatternComputationMethod>> tandemMismatchMethod = new HashMap<String, Class<? extends PatternComputationMethod>>();
	
	/**
	 * HasMap wobbleMethod : contains all the methods for GU base pair computation.
	 */
	private static HashMap<String, Class<? extends PatternComputationMethod>> wobbleMethod = new HashMap<String, Class<? extends PatternComputationMethod>>();
	
	/**
	 * HasMap internalLoopMethod : contains all the methods for internal loop computation.
	 */
	private static HashMap<String, Class<? extends PatternComputationMethod>> internalLoopMethod = new HashMap<String, Class<? extends PatternComputationMethod>>();
	
	/**
	 * HasMap singleBulgeLoopMethod : contains all the methods for single bulge loop computation.
	 */
	private static HashMap<String, Class<? extends PatternComputationMethod>> singleBulgeLoopMethod = new HashMap<String, Class<? extends PatternComputationMethod>>();
	
	/**
	 * HasMap longBulgeLoopMethod : contains all the methods for long bulge loop computation.
	 */
	private static HashMap<String, Class<? extends PatternComputationMethod>> longBulgeLoopMethod = new HashMap<String, Class<? extends PatternComputationMethod>>();
	
	/**
	 * HasMap singleDanglingEndMethod : contains all the methods for single dangling end computation.
	 */
	private static HashMap<String, Class<? extends PatternComputationMethod>> singleDanglingEndMethod = new HashMap<String, Class<? extends PatternComputationMethod>>();
	
	/**
	 * HasMap doubleDanglingEndMethod : contains all the methods for double dangling end computation.
	 */
	private static HashMap<String, Class<? extends PatternComputationMethod>> doubleDanglingEndMethod = new HashMap<String, Class<? extends PatternComputationMethod>>();
	
	/**
	 * HasMap longDanglingEndMethod : contains all the methods for long dangling end computation.
	 */
	private static HashMap<String, Class<? extends PatternComputationMethod>> longDanglingEndMethod = new HashMap<String, Class<? extends PatternComputationMethod>>();
	
	/**
	 * HasMap inosineMethod : contains all the methods for inosine computation.
	 */
	private static HashMap<String, Class<? extends PatternComputationMethod>> inosineMethod = new HashMap<String, Class<? extends PatternComputationMethod>>();
	
	/**
	 * HasMap CNGRepeatsMethod : contains all the methods for CNG repeats computation.
	 */
	private static HashMap<String, Class<? extends PatternComputationMethod>> CNGRepeatsMethod = new HashMap<String, Class<? extends PatternComputationMethod>>();
	
	/**
	 * HasMap azobenzeneMethod : contains all the methods for azobenzene computation.
	 */
	private static HashMap<String, Class<? extends PatternComputationMethod>> azobenzeneMethod = new HashMap<String, Class<? extends PatternComputationMethod>>();
	
	/**
	 * HasMap lockedAcidMethod : contains all the methods for locked nucleic acid computation.
	 */
	private static HashMap<String, Class<? extends PatternComputationMethod>> lockedAcidMethod = new HashMap<String, Class<? extends PatternComputationMethod>>();
	
	/**
	 * HasMap hydroxyadenosineMethod : contains all the methods for hydroxyadenine computation.
	 */
	private static HashMap<String, Class<? extends PatternComputationMethod>> hydroxyadenosineMethod = new HashMap<String, Class<? extends PatternComputationMethod>>();
	
	/**
	 * HasMap PatternModel : contains all the methods for the different pattern computation.
	 */
	private static HashMap<String, HashMap<String, Class<? extends PatternComputationMethod>>> PatternModel = new HashMap<String, HashMap<String, Class<? extends PatternComputationMethod>>>();
	
	/**
	 * HasMap ionCorrection : contains all the methods for the ion correction.
	 */
	private static HashMap<String, Class<? extends CorrectionMethod>> ionCorrection = new HashMap<String, Class<? extends CorrectionMethod>>();
	
	/**
	 * HasMap DMSOCorrection : contains all the methods for the DMSO correction.
	 */
	private static HashMap<String, Class<? extends CorrectionMethod>> DMSOCorrection = new HashMap<String, Class<? extends CorrectionMethod>>();
	
	/**
	 * HasMap formamideCorrection : contains all the methods for the formamide correction.
	 */
	private static HashMap<String, Class<? extends CorrectionMethod>> formamideCorrection = new HashMap<String, Class<? extends CorrectionMethod>>();
	
	/**
	 * HasMap otherCorrection : contains all the methods for the temperature correction.
	 */
	private static HashMap<String, HashMap<String, Class<? extends CorrectionMethod>>> otherCorrection = new HashMap<String, HashMap<String, Class<? extends CorrectionMethod>>>();

	// RegisterMethods constructor
	
	/**
	 * creates a RegisterMethods object with the different initialised HasMaps.
	 */
	public RegisterMethods(){
		initialiseApproximativeMethods();
		initialiseMeltingComputationMethods();
		initialiseCricksMethods();
		initialiseNaEqMethods();
		initialiseSingleMismatchMethods();
		initialiseTandemMismatchMethods();
		initialiseWobbleMismatchMethods();
		initialiseInternalLoopMethods();
		initialiseSingleBulgeLoopMethods();
		initialiseLongBulgeLoopMethods();
		initialiseSingleDanglingEndMethods();
		initialiseDoubleDanglingEndMethods();
		initialiseLongDanglingEndMethods();
		initialiseCNGRepeatsMethods();
		initialiseInosineMethods();
		initialiseAzobenzeneMethods();
		initialiseHydroxyadenosineMethods();
		initialiseLockedAcidMethods();
		initialisePatternModels();
		initialiseIonCorrectionMethod();
		initialiseDMSOCorrectionMethod();
		initialiseFormamideCorrectionMethod();
		initialiseOtherCorrectionMethod();
	}
	
	// private methods
	
	/**
	 * initialises the NaEqMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseNaEqMethods(){
		NaEqMethod.put("ahs01", Ahsen01_NaEquivalent.class);
		NaEqMethod.put("pey00", Peyret00_NaEquivalent.class);
		NaEqMethod.put("mit96", Mitsuhashi96NaEquivalent.class);
	}
	
	/**
	 * initialises the meltingComputationMethods HashMap of the RegisterMethods object.
	 */
	private void initialiseMeltingComputationMethods(){
		meltingComputationMethods.put("A", ApproximativeMode.class);
		meltingComputationMethods.put("NN", NearestNeighborMode.class);
		meltingComputationMethods.put("def", null);
	}
	
	/**
	 * initialises the approximativeMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseApproximativeMethods(){
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
	
	/**
	 * initialises the cricksMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseCricksMethods(){
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
	
	/**
	 * initialises the singleMismatchMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseSingleMismatchMethods(){
		singleMismatchMethod.put("allsanpey", AllawiSantaluciaPeyret97_98_99mm.class);
		singleMismatchMethod.put("zno07", Znosko07mm.class);
		singleMismatchMethod.put("zno08", Znosko08mm.class);
		singleMismatchMethod.put("tur06", Turner06mm.class);
	}
	
	/**
	 * initialises the tandemMismatchMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseTandemMismatchMethods(){
		tandemMismatchMethod.put("allsanpey", AllawiSantaluciaPeyret97_98_99tanmm.class);
		tandemMismatchMethod.put("tur06", Turner99_06tanmm.class);
	}
	
	/**
	 * initialises the wobbleMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseWobbleMismatchMethods(){
		wobbleMethod.put("tur99", Turner99Wobble.class);
	}
	
	/**
	 * initialises the internalLoopMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseInternalLoopMethods(){
		internalLoopMethod.put("tur06", Turner06InternalLoop.class);
		internalLoopMethod.put("san04", Santalucia04InternalLoop.class);
		internalLoopMethod.put("zno07", Znosko071x2Loop.class);
	}
	
	/**
	 * initialises the singleBulgeLoopMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseSingleBulgeLoopMethods(){
		singleBulgeLoopMethod.put("tur06", Turner99_06SingleBulgeLoop.class);
		singleBulgeLoopMethod.put("san04", Santalucia04SingleBulgeLoop.class);
		singleBulgeLoopMethod.put("ser07", Serra07SingleBulgeLoop.class);
		singleBulgeLoopMethod.put("tan04", Tanaka04SingleBulgeLoop.class);

	}
	
	/**
	 * initialises the longBulgeLoopMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseLongBulgeLoopMethods(){
		longBulgeLoopMethod.put("tur06", Turner99_06LongBulgeLoop.class);
		longBulgeLoopMethod.put("san04", Santalucia04LongBulgeLoop.class);
	}
	
	/**
	 * initialises the singleDanglingEndMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseSingleDanglingEndMethods(){
		singleDanglingEndMethod.put("bom00", Bommarito00SingleDanglingEnd.class);
		singleDanglingEndMethod.put("ser08", Serra06_08SingleDanglingEnd.class);
		singleDanglingEndMethod.put("sugdna02", Sugimoto02DNADanglingEnd.class);
		singleDanglingEndMethod.put("sugrna02", Sugimoto02RNADanglingEnd.class);
	}
	
	/**
	 * initialises the doubleDanglingEndMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseDoubleDanglingEndMethods(){
		doubleDanglingEndMethod.put("ser05", Serra05DoubleDanglingEnd.class);
		doubleDanglingEndMethod.put("ser06", Serra06DoubleDanglingEnd.class);
		doubleDanglingEndMethod.put("sugdna02", Sugimoto02DNADanglingEnd.class);
		doubleDanglingEndMethod.put("sugrna02", Sugimoto02RNADanglingEnd.class);

	}
	
	/**
	 * initialises the longDanglingEndMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseLongDanglingEndMethods(){
		longDanglingEndMethod.put("sugdna02", Sugimoto02DNADanglingEnd.class);
		longDanglingEndMethod.put("sugrna02", Sugimoto02RNADanglingEnd.class);
	}
	
	/**
	 * initialises the CNGRepeatsMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseCNGRepeatsMethods(){
		CNGRepeatsMethod.put("bro05", Broda05CNGRepeats.class);
	}
	
	/**
	 * initialises the inosineMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseInosineMethods(){
		inosineMethod.put("san05", Santalucia05Inosine.class);
		inosineMethod.put("zno07", Znosko07Inosine.class);
	}
	
	/**
	 * initialises the azobenzeneMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseAzobenzeneMethods(){
		azobenzeneMethod.put("asa05", Asanuma05Azobenzene.class);
	}
	
	/**
	 * initialises the lockedAcidMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseLockedAcidMethods(){
		lockedAcidMethod.put("mct04", McTigue04LockedAcid.class);
	}
	
	/**
	 * initialises the hydroxyadenineMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseHydroxyadenosineMethods(){
		hydroxyadenosineMethod.put("sug01", Sugimoto01Hydroxyadenine.class);
	}
	
	/**
	 * initialises the ionCorrectionMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseIonCorrectionMethod(){
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
	
	/**
	 * initialises the DMSOCorrectionMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseDMSOCorrectionMethod(){
		DMSOCorrection.put("ahs01", Ahsen01DMSOCorrection.class);
		DMSOCorrection.put("cul76", Cullen76DMSOCorrection.class);
		DMSOCorrection.put("esc80", Escara80DMSOCorrection.class);
		DMSOCorrection.put("mus81", Musielski81DMSOCorrection.class);
	}
	
	/**
	 * initialises the formamideCorrectionMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseFormamideCorrectionMethod(){
		formamideCorrection.put("lincorr", FormamideLinearMethod.class);
		formamideCorrection.put("bla96", Blake96FormamideCorrection.class);
	}
	
	/**
	 * initialises the otherCorrectionMethod HashMap of the RegisterMethods object.
	 */
	private void initialiseOtherCorrectionMethod(){
		otherCorrection.put(OptionManagement.DMSOCorrection, DMSOCorrection);
		otherCorrection.put(OptionManagement.formamideCorrection, formamideCorrection);
	}
	
	/**
	 * initialises the patternModels HashMap of the RegisterMethods object.
	 */
	private void initialisePatternModels(){
		PatternModel.put(OptionManagement.azobenzeneMethod, azobenzeneMethod);
		PatternModel.put(OptionManagement.CNGMethod, CNGRepeatsMethod);
		PatternModel.put(OptionManagement.doubleDanglingEndMethod, doubleDanglingEndMethod);
		PatternModel.put(OptionManagement.hydroxyadenineMethod, hydroxyadenosineMethod);
		PatternModel.put(OptionManagement.inosineMethod, inosineMethod);
		PatternModel.put(OptionManagement.internalLoopMethod, internalLoopMethod);
		PatternModel.put(OptionManagement.lockedAcidMethod, lockedAcidMethod);
		PatternModel.put(OptionManagement.longBulgeLoopMethod, longBulgeLoopMethod);
		PatternModel.put(OptionManagement.longDanglingEndMethod, longDanglingEndMethod);
		PatternModel.put(OptionManagement.NNMethod, cricksMethod);
		PatternModel.put(OptionManagement.singleBulgeLoopMethod, singleBulgeLoopMethod);
		PatternModel.put(OptionManagement.singleDanglingEndMethod, singleDanglingEndMethod);
		PatternModel.put(OptionManagement.singleMismatchMethod, singleMismatchMethod);
		PatternModel.put(OptionManagement.tandemMismatchMethod, tandemMismatchMethod);
		PatternModel.put(OptionManagement.wobbleBaseMethod, wobbleMethod);

	}
	
	/**
	 * This method is called to get the appropriate HashMap of the RegisterMethods object. It registers all
	 * the possible method names for the model 'optionName'.
	 * @param String optionName : method or model name
	 * @return the appropriate HashMap of the RegisterMethods object. It registers all
	 * the possible method names for the model 'optionName'.
	 * If the option name doesn't exist, a NoExistingMethodException is thrown.
	 * Ex : "-nn" => it is a crick's pair model => return the cricksMethod HasMap of the RegisteredMethods object.
	 */
	private HashMap<String , Class<? extends PatternComputationMethod>> getPartialCalculMethodHashMap(String optionName){
		if (PatternModel.get(optionName) == null){
			throw new NoExistingMethodException("No method is implemented for the option " + optionName + ".");
		}
		return PatternModel.get(optionName);
	}
	
	// public methods
	
	/**
	 * This method is called to get the PatternComputationMethod object which represents the method 'methodName'
	 * entered with the option 'optionName'.
	 * @param String optionName : option name
	 * @param String methodName : method or model name
	 * @return PatternComputationMethod object which represents the method 'methodName'
	 * entered with the option 'optionName'.
	 * If there is no PatternComputationMethod for the method 'methodName' entered with the option 'optionName', 
	 * a NoExistingMethodException is thrown.
	 * If a InstantiationException or a IllegalAccessException is catch, a NoExistingMethodException is thrown.
	 */
	public PatternComputationMethod getPatternComputationMethod(String optionName, String methodName){
		
		if (methodName != null){
			PatternComputationMethod method;
			try {
				if (Helper.useOtherDataFile(methodName)){
					methodName = Helper.extractsOptionMethodName(methodName);

				}
				if (getPartialCalculMethodHashMap(optionName).get(methodName) == null){
					throw new NoExistingMethodException("We don't know the method " + methodName);
				}
				method = getPartialCalculMethodHashMap(optionName).get(methodName).newInstance();
				return method;
			} catch (InstantiationException e) {
				throw new NoExistingMethodException("The calcul method is not implemented yet. Check the option " + optionName, e);
			} catch (IllegalAccessException e) {
				throw new NoExistingMethodException("The calcul method is not implemented yet. Check the option " + optionName, e);
			}
		}
		return null;
	}
	
	/**
	 * This method is called to get the SodiumEquivalentMethod object which represents the method or model
	 * for the sodium equivalence in the HashMap 'optionSet'.
	 * @param HashMap<String, String> optionSet : contains the options (default options and options entered by the user)
	 * @return SodiumEquivalentMethod object which represents the method or model
	 * for the sodium equivalence in the HashMap 'optionSet'.
	 * If there is no SodiumEquivalentMethod for the method 'methodName' entered with the option 'optionName', 
	 * a NoExistingMethodException is thrown.
	 * If a InstantiationException or a IllegalAccessException is catch, a NoExistingMethodException is thrown.
	 * If the method is not applicable with the options present in 'optionSet', a MethodNotApplicableException

	 */
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
			throw new NoExistingMethodException("The sodium equivalence method is not implemented yet. Check the option " + OptionManagement.NaEquivalentMethod, e);
		} catch (IllegalAccessException e) {
			throw new NoExistingMethodException("The sodium equivalence method is not implemented yet. Check the option " + OptionManagement.NaEquivalentMethod, e);
		}
	}
	
	/**
	 * This method is called to get the CorrectionMethod object which represents the method or model
	 * used for the ion correction in the environment 'environment'.
	 * If no ion correction method is specified by the user, the ion correction will be selected
	 * depending on the algorithm from Owczarzy et al. (2008)
	 * @param Environment environment
	 * @return CorrectionMethod object which represents the method or model
	 * used for the ion correction in the environment 'environment'.
	 * If there is no SodiumEquivalentMethod for the method 'methodName' entered with the option 'optionName', 
	 * a NoExistingMethodException is thrown.
	 * If a InstantiationException or a IllegalAccessException is catch, a NoExistingMethodException is thrown.
	 */
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
				throw new NoExistingMethodException("The ion correction method is not implemented yet. Check the option " + OptionManagement.ionCorrection, e);
			} catch (IllegalAccessException e) {
				throw new NoExistingMethodException("The ion correction method is not implemented yet. Check the option " + OptionManagement.ionCorrection, e);
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
	
	/**
	 * This method is called to get the MeltingComputationMethod object which represents the method or model
	 * for the enthalpy, entropy and melting temperature computation in the HashMap 'optionSet'.
	 * @param HashMap<String, String> optionSet : contains the options (default options and options entered by the user)
	 * @return MeltingComputationMethod object which represents the method or model
	 * for the enthalpy, entropy and melting temperature computation in the HashMap 'optionSet'.
	 * If there is no SodiumEquivalentMethod for the method 'methodName' entered with the option 'optionName', 
	 * a NoExistingMethodException is thrown.
	 * If a InstantiationException or a IllegalAccessException is catch, a NoExistingMethodException is thrown.
	 * If the method is not applicable with the options present in 'optionSet', a MethodNotApplicableException
	 */
	public MeltingComputationMethod getMeltingComputationMethod(HashMap<String, String> optionSet){
		
		String methodName = optionSet.get(OptionManagement.globalMethod);
		if (methodName == null){
			throw new NoExistingMethodException("No method is implemented for the option " + OptionManagement.globalMethod + ".");
		}
	
		MeltingComputationMethod method = null;

			if (meltingComputationMethods.get(methodName) == null){
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
						method.setUpVariables(optionSet);
					} catch (InstantiationException e) {
						throw new NoExistingMethodException("The approximative method is not implemented yet. Check the option " + OptionManagement.approximativeMode, e);
					} catch (IllegalAccessException e) {
						throw new NoExistingMethodException("The approximative method is not implemented yet. Check the option " + OptionManagement.approximativeMode, e);
					}
				}
				else {

					method = new NearestNeighborMode();
					
					method.setUpVariables(optionSet);
				}
			}
			else if (methodName.equals("A")){
				methodName = optionSet.get(OptionManagement.approximativeMode);

				try {
					if (approximativeMethod.get(methodName) == null){
						throw new NoExistingMethodException("We don't know the method " + methodName);
					}
					method = approximativeMethod.get(methodName).newInstance();
					method.setUpVariables(optionSet);
				} catch (InstantiationException e) {
					throw new NoExistingMethodException("The approximative method is not implemented yet. Check the option " + OptionManagement.approximativeMode, e);
				} catch (IllegalAccessException e) {
					throw new NoExistingMethodException("The approximative method is not implemented yet. Check the option " + OptionManagement.approximativeMode, e);
				}
			}
			else {
				method = new NearestNeighborMode();
				
				method.setUpVariables(optionSet);
			}
			if (method.isApplicable() && method != null) {

				return method;
			}
			else {
				throw new MethodNotApplicableException("The melting temperature calcul method (option " + OptionManagement.globalMethod + ") is not applicable with this environment.");
			}
	}
	
	/**
	 * This method is called to get the CorrectionMethod object which represents the method 'methodName'
	 * entered with the option 'optionName'.
	 * @param String optionName : option name
	 * @param String methodName : method or model name
	 * @return CorrectionMethod object which represents the method 'methodName'
	 * entered with the option 'optionName'.
	 * If there is no PatternComputationMethod for the method 'methodName' entered with the option 'optionName', 
	 * a NoExistingMethodException is thrown.
	 * If a InstantiationException or a IllegalAccessException is catch, a NoExistingMethodException is thrown.
	 */
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
			throw new NoExistingMethodException("The ion correction method is not implemented yet. Check the option " + OptionManagement.ionCorrection, e);
		} catch (IllegalAccessException e) {
			throw new NoExistingMethodException("The ion correction method is not implemented yet. Check the option " + OptionManagement.ionCorrection, e);
		}
	}
	
	/**
	 * corrects the melting temperature if other agents are present in the solution (formamide, DMSO, ...)
	 * @param Environment environment
	 * @return ThermoResult which contains the corrected melting temperature.
	 */
	public ThermoResult computeOtherMeltingCorrections(Environment environment){
		if (environment.getDMSO() > 0){
			CorrectionMethod DMSOCorrection = getCorrectionMethod(OptionManagement.DMSOCorrection, environment.getOptions().get(OptionManagement.DMSOCorrection));
			
			if (DMSOCorrection == null){
				throw new NoExistingMethodException("There is no implemented DMSO correction.");
			}
			else if (DMSOCorrection.isApplicable(environment)){
				environment.setResult(DMSOCorrection.correctMeltingResults(environment));
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
				environment.setResult(formamideCorrection.correctMeltingResults(environment));
			}
			else {
				throw new MethodNotApplicableException("The formamide correction is not applicable with this environment (option " + OptionManagement.formamideCorrection + ").");
			}
		}
		
		return environment.getResult();
	}
}