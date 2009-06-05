package melting.exceptions;

public class NoExistingOutputFileException extends RuntimeException {

	private static final long serialVersionUID = 8580334876179097236L;
	
	public NoExistingOutputFileException(String message){
		super(message);
	}

}
