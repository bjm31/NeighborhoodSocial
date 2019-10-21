package backend.exception;

public class InvalidLoginException extends Exception{
	static final long serialVersionUID = 3L;
	
	public InvalidLoginException(String message) {
		super(message);
	}
}
