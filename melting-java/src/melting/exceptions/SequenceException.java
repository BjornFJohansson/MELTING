package melting.exceptions;

public class SequenceException extends RuntimeException {

	private static final long serialVersionUID = -7494134302550466479L;
	
	public SequenceException() {
		super();
	}

	public SequenceException(String message, Throwable cause) {
		super(message, cause);
	}

	public SequenceException(Throwable cause) {
		super(cause);
	}
	
	public SequenceException(String message){
		super(message);
	}

}
