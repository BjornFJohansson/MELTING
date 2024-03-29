// This program is free software; you can redistribute it and/or modify it
// under the terms of the GNU General Public Licence as published by the Free
// Software Foundation; either verison 2 of the Licence, or (at your option)
// any later version.
//
// This program is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public Licence for
// more details.  
//
// You should have received a copy of the GNU General Public Licence along with
// this program; if not, write to the Free Software Foundation, Inc., 
// 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
//
// Marine Dumousseau, Nicolas Le Novere
// EMBL-EBI, neurobiology computational group,             
// Cambridge, UK. e-mail: melting-forum@googlegroups.com

package melting.patternModels.wobble;

import melting.Environment;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.patternModels.PatternComputation;
import melting.sequences.NucleotidSequences;
import melting.methodInterfaces.NamedMethod;

/**
 * Represents the GU base pair model ser12. It extends PatternComputation.
 *
 * Serra et al (2012) Biochemistry 51(16): 3508–3522
 *
 * @author Marine Dumousseau
 * @since <pre>10/02/13</pre>
 */

public class Serra12Wobble extends PatternComputation
  implements NamedMethod
{
    // Instance variables

    /**
     * String defaultFileName : default name for the xml file containing the thermodynamic parameters for GU base pairs
     */
    public static String defaultFileName = "Serra2012wobble.xml";

    /**
     * Full name of the method.
     */
    private static String methodName = "Serra et al. (2012)";

    // PatternComputationMethod interface implementation

    @Override
    public boolean isApplicable(Environment environment, int pos1,
                                int pos2) {
        if (environment.getHybridization().equals("rnarna") == false){
            OptionManagement.logWarning("\n The model of " +
                    "Serra (2012) is only established " +
                    "for RNA sequences.");
        }
        return super.isApplicable(environment, pos1, pos2);
    }

    @Override
    public ThermoResult computeThermodynamics(NucleotidSequences sequences,
                                              int pos1, int pos2, ThermoResult result) {
        OptionManagement.logMessage("\n The nearest neighbor model for GU" +
                                    " base pairs is");
        OptionManagement.logMethodName(methodName);
        OptionManagement.logFileName(this.fileName);

        return getGUThermoResult(sequences, pos1, pos2, result);
    }

    protected ThermoResult getGUThermoResult(NucleotidSequences sequences, int pos1, int pos2, ThermoResult result) {
        int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength(), sequences);
        pos1 = positions[0];
        pos2 = positions[1];

        NucleotidSequences newSequence = sequences.getEquivalentSequences("rna");

        double enthalpy = result.getEnthalpy();
        double entropy = result.getEntropy();

        Thermodynamics mismatchValue;
        String closing = "not_G/C";

        if (newSequence.getDuplexLength() - 1 == 4 && newSequence.getSequence(pos1,pos2).equals("GGUC") && newSequence.getComplementary(pos1,pos2).equals("CUGG")){
            closing = "G/C";
            mismatchValue = this.collector.getMismatchValue(newSequence.getSequence(pos1, pos2), newSequence.getComplementary(pos1, pos2), closing);
            OptionManagement.logMessage("\n" + newSequence.getSequence(pos1, pos2) + "/" + newSequence.getComplementary(pos1, pos2) + " : enthalpy = " + mismatchValue.getEnthalpy() + "  entropy = " + mismatchValue.getEntropy());

            enthalpy += mismatchValue.getEnthalpy();
            entropy += mismatchValue.getEntropy();
        }
        else{
            for (int i = pos1; i < pos2; i++){
                if (newSequence.getSequenceNNPair(i).equals("GU") && newSequence.getComplementaryNNPair(i).equals("UG")){
                    mismatchValue = this.collector.getMismatchValue(newSequence.getSequenceNNPair(i), newSequence.getComplementaryNNPair(i), closing);
                }
                else {
                    mismatchValue = this.collector.getMismatchValue(newSequence.getSequenceNNPair(i), newSequence.getComplementaryNNPair(i));
                }
                OptionManagement.logMessage("\n" + newSequence.getSequenceNNPair(i) + "/" + newSequence.getComplementaryNNPair(i) + " : enthalpy = " + mismatchValue.getEnthalpy() + "  entropy = " + mismatchValue.getEntropy());

                enthalpy += mismatchValue.getEnthalpy();
                entropy += mismatchValue.getEntropy();
            }
        }

        result.setEnthalpy(enthalpy);
        result.setEntropy(entropy);
        return result;
    }

    @Override
    public void initialiseFileName(String methodName){
        super.initialiseFileName(methodName);

        if (this.fileName == null){
            this.fileName = defaultFileName;
        }
    }

    @Override
    public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
                                       int pos2) {
        int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength(), sequences);
        pos1 = positions[0];
        pos2 = positions[1];

        NucleotidSequences newSequences = sequences.getEquivalentSequences("rna");

        String closing = "not_G/C";

        if (pos2-pos1 == 4 && newSequences.getSequence(pos1, pos2).equals("GGUC") && newSequences.getComplementary(pos1, pos2).equals("CUGG")){
            closing = "G/C";
            if (this.collector.getMismatchValue(newSequences.getSequence(pos1, pos2), newSequences.getComplementary(pos1, pos2), closing) == null){
                OptionManagement.logWarning("\n The thermodynamic parameter for " + newSequences.getSequence(pos1, pos2) + "/" + newSequences.getComplementary(pos1, pos2) + " is missing. Check the parameters for wobble base pairs.");
                return true;
            }
            return super.isMissingParameters(newSequences, pos1, pos2);
        }
        for (int i = pos1; i < pos2; i++){
            if (newSequences.getSequenceNNPair(i).equals("GU") && newSequences.getComplementaryNNPair(i).equals("UG")){
                if (this.collector.getMismatchValue(newSequences.getSequenceNNPair(i), newSequences.getComplementaryNNPair(i), closing) == null){
                    OptionManagement.logWarning("\n The thermodynamic parameter for " + newSequences.getSequenceNNPair(i) + "/" + newSequences.getComplementaryNNPair(i) + " is missing. Check the parameters for wobble base pairs.");
                    return true;
                }
            }
            else{
                if (this.collector.getMismatchValue(newSequences.getSequenceNNPair(i), newSequences.getComplementaryNNPair(i)) == null){
                    OptionManagement.logWarning("\n The thermodynamic parameter for " + newSequences.getSequenceNNPair(i) + "/" + newSequences.getComplementaryNNPair(i) + " is missing. Check the parameters for wobble base pairs.");
                    return true;
                }
            }
        }

        return super.isMissingParameters(newSequences, pos1, pos2);
    }

    // private method
    /**
     * corrects the pattern positions in the duplex to have the adjacent
     * base pair of the pattern included in the subsequence between the positions pos1 and pos2.
     * If one of the adjacent base pair is not a complementary base pair, this adjacent base pair is not
     * taken in account in the subsequence.
     * @param pos1 : starting position of the internal loop
     * @param pos2 : ending position of the internal loop
     * @param duplexLength : total length of the duplex
     * @return int [] positions : new positions of the subsequence to have the pattern surrounded by the
     * adjacent base pairs in the duplex.
     * If one of the adjacent base pair is not a complementary base pair, this adjacent base pair is not
     * taken in account in the subsequence.
     *
     * Ex :
     * The middle base pair is a GU base pair.
     * AGC/UUG => pos1 = 0 and pos2 = 2
     * AGC/UUU => pos1 = 0 and pos2 = 1
     * AGC/GUU => pos1 = 1 and pos2 = 2
     */
    private int[] correctPositions(int pos1, int pos2, int duplexLength, NucleotidSequences sequences){

        if (pos1 > 0){
            if (sequences.getDuplex().get(pos1 - 1).isComplementaryBasePair()){
                pos1 --;
            }
        }
        if (pos2 < duplexLength - 1){
            if (sequences.getDuplex().get(pos2 + 1).isComplementaryBasePair()){
                pos2 ++;
            }
        }
        int [] positions = {pos1, pos2};
        return positions;
    }

    /**
     * Gets the full name of the method.
     * @return The full name of the method.
     */
    @Override
    public String getName()
    {
      return methodName;
    }
}
