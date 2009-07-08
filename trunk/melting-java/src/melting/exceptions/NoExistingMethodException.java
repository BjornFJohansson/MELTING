package melting.exceptions;

public class NoExistingMethodException extends RuntimeException {

	private static final long serialVersionUID = 4480352056557369137L;
	
	public NoExistingMethodException() {
		super();
	}

	public NoExistingMethodException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoExistingMethodException(Throwable cause) {
		super(cause);
	}
	
	public NoExistingMethodException(String message){
		super(message);
	}

}
