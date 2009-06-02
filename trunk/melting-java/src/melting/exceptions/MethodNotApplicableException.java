package melting.exceptions;

public class MethodNotApplicableException extends RuntimeException {

	private static final long serialVersionUID = 7901928613998187436L;

	public MethodNotApplicableException(String message){
		super(message);
	}
}
