package melting.exceptions;

public class NoExistingOutputFileException extends RuntimeException {

	private static final long serialVersionUID = 8580334876179097236L;
	
	public NoExistingOutputFileException() {
		super();
	}

	public NoExistingOutputFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoExistingOutputFileException(Throwable cause) {
		super(cause);
	}
	
	public NoExistingOutputFileException(String message){
		super(message);
	}

}
