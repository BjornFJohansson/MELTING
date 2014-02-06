package melting;

import melting.configuration.OptionManagement;
import melting.configuration.RegisterMethods;
import melting.methodInterfaces.MeltingComputationMethod;
import melting.nearestNeighborModel.NearestNeighborMode;

import java.io.*;
import java.text.NumberFormat;
import java.util.Arrays;

public class BatchMain {

	public static void main(String[] args) {
		/**
		 * normal options
		 * 
		 * unnamed optional arg 1 is the filename to read sequences from
		 * 
		 * output is written in csv format sequences DeltaH DeltaS Tm (deg C)
		 */

		if (args.length < 1) {
			System.err.print("Usage: melting-batch [OPTIONS] sequencefile");
			System.exit(1);
		} else {
			// remove file from list of options, pass options to
			// optionManager.isMeltingInformationOption(args)
			String[] commonArgs = Arrays.copyOfRange(args, 0, args.length - 2);
			String filename = args[args.length - 1];

			InputStream is = null;
			try {
				is = new FileInputStream(filename);
				readFile(is, commonArgs);
			} catch (FileNotFoundException e) {
				System.err.print("File " + filename + " could not be opened");
			} catch (IOException e) {
				System.err.print("Error reading file " + filename);
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	public static void readFile(InputStream is, String[] defaultArgs)
			throws IOException {
		BufferedReader bis = new BufferedReader(new InputStreamReader(is));

		String line = null;
		do {
			line = bis.readLine();
			String[] newArgs = line.split("[ \t]+");
			if (newArgs.length == 1) {
				// sequence is alone, prefix with -S
				String[] newNewArgs = new String[2];
				newNewArgs[0] = "-S";
				newNewArgs[1] = newArgs[0];
				newArgs = newNewArgs;
			} else if (newArgs.length == 2) {
				// 2nd arg must be the complementary sequence
				String[] newNewArgs = new String[4];
				newNewArgs[0] = "-S";
				newNewArgs[1] = newArgs[0];
				newNewArgs[2] = "-C";
				newNewArgs[3] = newArgs[1];
				newArgs = newNewArgs;
			} else if (newArgs.length == 0) {
				// skip empty lines
				newArgs = null;
			} else {
				System.err.print("Invalid line [" + line + "]");
			}

			if (newArgs != null) {
				String[] completeArgs = new String[defaultArgs.length
						+ newArgs.length];
				int i = 0;
				for (String arg : defaultArgs) {
					completeArgs[i++] = arg;
				}
				for (String arg : newArgs) {
					completeArgs[i++] = arg;
				}

				runMelting(completeArgs);
			}
		} while (line != null);
	}

	public static void runMelting(String[] args) {
		try {
			OptionManagement manager = new OptionManagement();
			Environment environment = manager.createEnvironment(args);
			RegisterMethods register = new RegisterMethods();
			MeltingComputationMethod calculMethod = register
					.getMeltingComputationMethod(environment.getOptions());
			ThermoResult results = calculMethod.computesThermodynamics();
			environment.setResult(results);

			results = calculMethod.getRegister()
					.computeOtherMeltingCorrections(environment);

			environment.setResult(results);
			displaysMeltingResults(environment, environment.getResult(),
					calculMethod);

		} catch (Exception e) {
			OptionManagement.logError(e.getMessage());
		}
	}

	private static void displaysMeltingResults(Environment environment,
			ThermoResult results, MeltingComputationMethod calculMethod) {
		displayColumnHeaders(calculMethod);

		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(2);

		double enthalpy = results.getEnthalpy();
		double entropy = results.getEntropy();

		if (calculMethod instanceof NearestNeighborMode) {
			OptionManagement.logInfo(environment
					.getSequences().getSequence()
					+ "\t"
					+ environment.getSequences().getComplementary()
					+ "\t"
					+ format.format(results.getEnergyValueInJ(enthalpy))
					+ "\t"
					+ format.format(results.getEnergyValueInJ(entropy))
					+ "\t"
					+ format.format(results.getTm()));
		}
		OptionManagement.logInfo(
				environment.getSequences().getSequence() + "\t"
						+ environment.getSequences().getComplementary() + "\t"
						+ format.format(results.getTm()));
	}
	
	private static boolean areHeadersDisplayed = false;

	private static void displayColumnHeaders(
			MeltingComputationMethod calculMethod) {
		if (!areHeadersDisplayed) {
			if (calculMethod instanceof NearestNeighborMode) {
				OptionManagement.logInfo(
						"Sequence\tComplementary\tDeltaH\rDeltaS\tTm (deg C)");
			} else {
				OptionManagement.logInfo(
						"Sequence\tComplementary\tTm (deg C)");
			}
			areHeadersDisplayed = true;
		}
	}
}
