package melting.exceptions;

public class OptionSyntaxError extends RuntimeException {

	private static final long serialVersionUID = -68893338772781425L;

	public OptionSyntaxError(String message){
		super(message);
	}
}
