package backend;

public class LogonAgent {
	
	public boolean login(String user, String pass) {
		
		DBConnection.connect("read_credentials");
		return false;
	}

} // end of class
