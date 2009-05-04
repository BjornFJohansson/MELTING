package melting;


public class Thermodynamics {
	
	protected double enthalpy;
	protected double entropy;
	
	public Thermodynamics(double H, double S){
		this.enthalpy = H;
		this.entropy = S;
	}
	
	public double getEnthalpy() {
		return enthalpy;
	}
	public void setEnthalpy(double enthalpy) {
		this.enthalpy = enthalpy;
	}
	public double getEntropy() {
		return entropy;
	}
	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}
}
