package melting.exceptions;

public class ThermodynamicParameterError extends RuntimeException {

	private static final long serialVersionUID = -8458644289304464405L;
	
	public ThermodynamicParameterError(String message){
		super(message);
	}

}
