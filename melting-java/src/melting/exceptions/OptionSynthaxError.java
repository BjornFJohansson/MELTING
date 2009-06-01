package melting.exceptions;

public class OptionSynthaxError extends RuntimeException {

	private static final long serialVersionUID = -68893338772781425L;

	public OptionSynthaxError(String message){
		super(message);
	}
}
