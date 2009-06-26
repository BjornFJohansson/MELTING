package examples;

import java.util.ArrayList;

import melting.NucleotidSequences;

public class MainTestSingleBulgeLoops {

	public static void main(String[] args) {
		
		ArrayList<Double> enthalpy = new ArrayList<Double>();
		ArrayList<Double> entropy = new ArrayList<Double>();

		enthalpy.add(-125.0);
		entropy.add(-349.5);
		enthalpy.add(-106.6);
		entropy.add(-295.7);
		enthalpy.add(-117.5);
		entropy.add(-325.2);
		enthalpy.add(-133.9);
		entropy.add(-375.5);
		enthalpy.add(-120.9);
		entropy.add(-338.6);
		enthalpy.add(-145.9);
		entropy.add(-412.8);
		enthalpy.add(-118.5);
		entropy.add(-328.0);
		enthalpy.add(-123.8);
		entropy.add(-346.2);
		enthalpy.add(-123.5);
		entropy.add(-347.5);
		enthalpy.add(-124.0);
		entropy.add(-349.9);
		enthalpy.add(-111.6);
		entropy.add(-306.2);
		enthalpy.add(-115.1);
		entropy.add(-320.3);
		enthalpy.add(-122.5);
		entropy.add(-343.4);
		enthalpy.add(-121.6);
		entropy.add(-341.5);
		enthalpy.add(-111.7);
		entropy.add(-307.7);
		enthalpy.add(-139.9);
		entropy.add(-392.5);
		enthalpy.add(-128.6);
		entropy.add(-357.9);
		enthalpy.add(-116.7);
		entropy.add(-326.7);
		enthalpy.add(-118.1);
		entropy.add(-331.6);
		enthalpy.add(-127.2);
		entropy.add(-356.6);
		enthalpy.add(-126.4);
		entropy.add(-353.0);
		enthalpy.add(-108.4);
		entropy.add(-297.9);
		enthalpy.add(-123.6);
		entropy.add(-344.9);
		enthalpy.add(-106.1);
		entropy.add(-294.2);
		enthalpy.add(-111.5);
		entropy.add(-308.0);
		enthalpy.add(-132.2);
		entropy.add(-368.1);
		enthalpy.add(-125.4);
		entropy.add(-353.3);
		enthalpy.add(-131.6);
		entropy.add(-370.2);
		enthalpy.add(-115.4);
		entropy.add(-318.9);
		enthalpy.add(-128.4);
		entropy.add(-358.4);
		enthalpy.add(-132.9);
		entropy.add(-373.0);
		enthalpy.add(-135.5);
		entropy.add(-383.6);
		enthalpy.add(-119.9);
		entropy.add(-337.4);
		enthalpy.add(-130.7);
		entropy.add(-365.6);
		enthalpy.add(-119.6);
		entropy.add(-331.9);
		enthalpy.add(-121.8);
		entropy.add(-342.2);
		enthalpy.add(-119.7);
		entropy.add(-335.0);
		enthalpy.add(-146.9);
		entropy.add(-414.2);
		enthalpy.add(-129.4);
		entropy.add(-364.2);
		enthalpy.add(-116.9);
		entropy.add(-323.3);
		enthalpy.add(-93.7);
		entropy.add(-258.3);
		enthalpy.add(-103.5);
		entropy.add(-285.7);
		enthalpy.add(-124.1);
		entropy.add(-344.8);
		enthalpy.add(-123.1);
		entropy.add(-344.7);
		enthalpy.add(-117.7);
		entropy.add(-326.4);
		enthalpy.add(-120.0);
		entropy.add(-333.5);
		enthalpy.add(-97.8);
		entropy.add(-267.8);
		enthalpy.add(-118.5);
		entropy.add(-329.9);
		enthalpy.add(-91.0);
		entropy.add(-260.0);
		enthalpy.add(-129.3);
		entropy.add(-363.9);
		enthalpy.add(-132.2);
		entropy.add(-370.7);
		enthalpy.add(-119.2);
		entropy.add(-332.8);
		enthalpy.add(-123.7);
		entropy.add(-346.4);
		enthalpy.add(-124.8);
		entropy.add(-346.9);
		enthalpy.add(-113.7);
		entropy.add(-315.1);
		enthalpy.add(-119.1);
		entropy.add(-330.6);
		enthalpy.add(-104.1);
		entropy.add(-287.0);
		enthalpy.add(-114.8);
		entropy.add(-315.2);
		enthalpy.add(-118.3);
		entropy.add(-330.5);
		enthalpy.add(-122.6);
		entropy.add(-341.6);
		enthalpy.add(-105.2);
		entropy.add(-288.2);
		enthalpy.add(-85.9);
		entropy.add(-245.7);
		enthalpy.add(-84.2);
		entropy.add(-233.2);
		enthalpy.add(-115.2);
		entropy.add(-322.1);
		enthalpy.add(-115.4);
		entropy.add(-323.3);
 		                  
		for (int i = 0; i < enthalpy.size(); i++){
			double Tm = enthalpy.get(i) * 1000 / (entropy.get(i) + 1.99 * Math.log(0.0001 / 4)) - 273.15;

			System.out.println(Tm);

		}
	}

}
