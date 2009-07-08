package melting.exceptions;

public class FileException extends RuntimeException {

	private static final long serialVersionUID = -8477108314961282300L;
	
	public FileException() {
		super();
	}

	public FileException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileException(Throwable cause) {
		super(cause);
	}
	
	public FileException(String message){
		super(message);
	}

}
