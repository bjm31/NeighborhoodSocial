/**
 * The only purpose of this class is to make the PasswordManager.clear method accessible outside of the backend package.
 * All other PasswordManager methods are to remain protected.
 */

package backend;

public class _PasswordManager {

	public static void clear(char[] charArray) {
		PasswordManager.clear(charArray);
	}
	
}
