/**
 * 
 */
package backend.exception;

/**
 * @author Tim Morris
 *
 */
public class DatabaseConnectionFailureException extends Exception{
	static final long serialVersionUID = 2L;
	
	public DatabaseConnectionFailureException (String message) {
		super(message);
	}
}
