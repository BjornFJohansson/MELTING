package melting.exceptions;

public class ThermodynamicParameterError extends RuntimeException {

	private static final long serialVersionUID = -8458644289304464405L;

	public ThermodynamicParameterError() {
		super();
	}

	public ThermodynamicParameterError(String message, Throwable cause) {
		super(message, cause);
	}

	public ThermodynamicParameterError(Throwable cause) {
		super(cause);
	}
	
	public ThermodynamicParameterError(String message){
		super(message);
	}

}
