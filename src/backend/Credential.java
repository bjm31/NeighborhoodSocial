package backend;

class Credential {
	
	private String user;
	private char[] pass;
	
	protected Credential(String user, char[] pass) {
		this.user = user;
		this.pass = pass;
	}

	/**
	 * @return the username
	 */
	protected String getUser() {
		String user = this.user;
		return user;
	}

	/**
	 * @param user the username to set
	 */
	protected void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	protected char[] getPass() {
		char[] pass = this.pass;
		
		return pass;
	}

	/**
	 * @param pass the password to set
	 */
	protected void setPass(char[] pass) {
		this.pass = pass;
	}
}
