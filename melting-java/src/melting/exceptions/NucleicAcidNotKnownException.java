package melting.exceptions;

public class NucleicAcidNotKnownException extends RuntimeException {
	
	private static final long serialVersionUID = 8913295672203816812L;
	
	public NucleicAcidNotKnownException() {
		super();
	}

	public NucleicAcidNotKnownException(String message, Throwable cause) {
		super(message, cause);
	}

	public NucleicAcidNotKnownException(Throwable cause) {
		super(cause);
	}
	
	public NucleicAcidNotKnownException(String message){
		super(message);
	}

}
