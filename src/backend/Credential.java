package backend;

class Credential {
	
	private String user;
	private String pass;
	
	public Credential (String user, String pass) {
		setUser(user);
		setPass(pass);
	}
	
	private void setUser (String user) {
		this.user = user;
	}
	
	protected String getUser() {
		String user = this.user;
		return user;
	}
	
	private void setPass(String pass) {
		this.pass = pass;
	}
	
	protected String getPass() {
		String pass = this.pass;
		return pass;
	}

}
