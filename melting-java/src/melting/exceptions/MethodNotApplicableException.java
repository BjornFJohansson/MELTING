package melting.exceptions;

public class MethodNotApplicableException extends RuntimeException {

	public MethodNotApplicableException(String message){
		super(message);
	}
	
	public MethodNotApplicableException() {
		super();
	}

	public MethodNotApplicableException(String message, Throwable cause) {
		super(message, cause);
	}

	public MethodNotApplicableException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = 7901928613998187436L;
}
