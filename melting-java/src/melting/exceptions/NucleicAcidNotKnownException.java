package melting.exceptions;

public class NucleicAcidNotKnownException extends RuntimeException {

	private static final long serialVersionUID = 8913295672203816812L;
	
	public NucleicAcidNotKnownException(String message){
		super(message);
	}

}
