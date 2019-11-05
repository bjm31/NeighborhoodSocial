/**
 *  This class is used by a Servlet to save user info in a session, and it can transfer user info between multiple Servlets
 */
package backend;

import org.bson.types.ObjectId;

public class User {
	
	private String user;
	private ObjectId n_id;
	private String inviteCode;


	//private String n_id;
	//private String inviteCode;
	//private String addr;
	
	public User(String inviteCode) {
		this.user = null;
		this.n_id = null;
		this.inviteCode = inviteCode;
	}

	public User(String user, ObjectId n_id) {
		
		this.user = user;
		this.n_id = n_id;
		this.inviteCode = null;
		//this.addr = addr;
	}
	

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the n_id
	 */
	public ObjectId getN_id() {
		return n_id;
	}

	/**
	 * @param n_id the n_id to set
	 */
	public void setN_id(ObjectId n_id) {
		this.n_id = n_id;
	}


	/**
	 * @return the inviteCode
	 */
	public String getInviteCode() {
		return inviteCode;
	}


	/**
	 * @param inviteCode the inviteCode to set
	 */
	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
}
