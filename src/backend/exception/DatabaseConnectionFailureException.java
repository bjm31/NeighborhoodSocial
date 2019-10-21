package backend.exception;

public class DatabaseConnectionFailureException extends Exception {
	static final long serialVersionUID = 2L;
	
	public DatabaseConnectionFailureException (String message) {
		super(message);
	}
}
