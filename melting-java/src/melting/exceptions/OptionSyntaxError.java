package melting.exceptions;

public class OptionSyntaxError extends RuntimeException {

	private static final long serialVersionUID = -68893338772781425L;
	
	public OptionSyntaxError() {
		super();
	}

	public OptionSyntaxError(String message, Throwable cause) {
		super(message, cause);
	}

	public OptionSyntaxError(Throwable cause) {
		super(cause);
	}

	public OptionSyntaxError(String message){
		super(message);
	}
}
