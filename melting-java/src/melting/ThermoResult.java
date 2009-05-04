package melting;

public class ThermoResult extends Thermodynamics{
	
	private double Tm;
	
	public ThermoResult(double enthalpy, double entropy, double Tm){
		super(enthalpy, entropy);
		this.Tm = Tm;
	}
	
	public double getTm() {
		return Tm;
	}
	public void setTm(double tm) {
		Tm = tm;
	}

}
