package melting;

public class ThermoResult extends Thermodynamics{
	
	private double Tm;
	private double saltIndependentEntropy;
	
	public ThermoResult(double enthalpy, double entropy, double Tm){
		super(enthalpy, entropy);
		this.Tm = Tm;
		this.saltIndependentEntropy = 0;
	}
	
	public double getTm() {
		return Tm;
	}
	public void setTm(double tm) {
		Tm = tm;
	}
	public double getSaltIndependentEntropy() {
		return saltIndependentEntropy;
	}

	public void setSaltIndependentEntropy(double saltIndependentEntropy) {
		this.saltIndependentEntropy = saltIndependentEntropy;
	}

	public double getEnergyValueInJ(double energyValue){
		return energyValue * 4.18;
	}

}
