/**
 *  This class is used by a Servlet to save user info in a session, and it can transfer user info between multiple Servlets
 */
package backend;

public class User {
	
	private String user;

	//private String addr;

	public User(String user) {
		
		this.user = user;
		//this.addr = addr;
		
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}


}
