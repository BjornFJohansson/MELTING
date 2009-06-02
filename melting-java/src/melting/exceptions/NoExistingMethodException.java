package melting.exceptions;

public class NoExistingMethodException extends RuntimeException {

	private static final long serialVersionUID = 4480352056557369137L;
	
	public NoExistingMethodException(String message){
		super(message);
	}

}
