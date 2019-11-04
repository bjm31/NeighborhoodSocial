/**
 *  This class is used by a Servlet to save user info in a session, and it can transfer user info between multiple Servlets
 */
package backend;

public class User {
	
	private String user;
	private String password;
	//private String addr;
	
	public User(String user, char[] password) {
		
		this.user = user;
		this.password = new String(password);
		//this.addr = addr;
		
	}
	public User(String user, String password) {
		
		this.user = user;
		this.password = password;
		//this.addr = addr;
		
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
