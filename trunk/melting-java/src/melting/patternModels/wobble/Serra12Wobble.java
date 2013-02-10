package melting.patternModels.wobble;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.sequences.NucleotidSequences;

import java.util.logging.Level;

/**
 * This class represents the GU base pair model ser12. It extends PatternComputation.
 *
 * Serra et al (2012) Biochemistry 51(16): 3508–3522
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>10/02/13</pre>
 */

public class Serra12Wobble extends Turner99Wobble {

    // Instance variables

    /**
     * String defaultFileName : default name for the xml file containing the thermodynamic parameters for GU base pairs
     */
    public static String defaultSerraFileName = "Serra2012wobble.xml";

    // PatternComputationMethod interface implementation

    @Override
    public boolean isApplicable(Environment environment, int pos1,
                                int pos2) {
        if (environment.getHybridization().equals("rnarna") == false){
            OptionManagement.meltingLogger.log(Level.WARNING, "\n The model of " +
                    "Serra (2012) is only established " +
                    "for RNA sequences.");
        }
        return super.isApplicable(environment, pos1, pos2);
    }

    @Override
    public ThermoResult computeThermodynamics(NucleotidSequences sequences,
                                              int pos1, int pos2, ThermoResult result) {
        OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model for GU base pairs is from Serra et al. (2012) : ");
        OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

        return getGUThermoResult(sequences, pos1, pos2, result);
    }

    @Override
    public void initialiseFileName(String methodName){
        if (Helper.useOtherDataFile(methodName)){
            this.fileName = Helper.extractsOptionFileName(methodName);
        }
        else{
            this.fileName = defaultSerraFileName;
        }
    }

    // private method
}
