package backend;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.*;
import java.time.Instant;


public class DatabaseActions {

	/**
	 * Validates a user login. Returns true if valid, else false.
	 * @param userToCheck username
	 * @param passToCheck password
	 * @return valid or invalid user
	 */
	public static boolean login(String userToCheck, char[] passToCheck) {
		ObjectId n_id 			= null;
		DatabaseConnection db 	= null;
		String hash				= null;
		boolean isAuthenticated = false;
		String user				= null;
		
		user = userToCheck.toLowerCase();
		
					
		db = new DatabaseConnection("read_credentials");
		
		if (db.getDatabase().getCollection("Neighbor").find(eq("user_name", user)).first() == null) {
			return false;

		} else {
			n_id = db.getDatabase().getCollection("Neighbor").find(eq("user_name", user)).first().getObjectId("_id");
		}
		
		hash = PasswordManager.hashPassword(
				passToCheck, db.getDatabase().getCollection("Authentication").find(eq("N_id", n_id)).first().get("salt").toString());
		
		if (!(hash.equals(db.getDatabase().getCollection("Authentication").find(eq("N_id", n_id)).first().get("password").toString()))) {
			isAuthenticated = false;
		} else
			isAuthenticated = true;
		
		db.disconnect();
		
		return isAuthenticated;
	}
	
	/**
	 * Checks for validity of an invite string. If valid, returns true and sets "was_used" db flag to true.
	 * @param inviteCode invite code to check
	 * @return valid or invalid invite code
	 */
	public static boolean validInvite(String inviteCode) {
		DatabaseConnection db 			= null;
		MongoCollection<Document> coll	= null;
		Document doc					= null;
		boolean isValid					= false;
		
		db = new DatabaseConnection("invite");
		coll = db.getDatabase().getCollection("InviteCode");
		doc = coll.find(eq("code", inviteCode)).first();
				
		if (doc == null) {
			isValid = false;
		} else {
			isValid = true;
			coll.findOneAndUpdate(eq("code", inviteCode), new Document("$set", new Document("was_used", true)));
		}
		db.disconnect();	
		
		return isValid;
	}
	
	/**
	 * Creates new entry in "Neighbor" collection
	 * @param inviteCode
	 * @param username
	 * @param fname
	 * @param lname
	 * @param email
	 * @param photo
	 */
	public static void createNewUser(String inviteCode, String username, String fname, String lname, String email, String photo) {
		DatabaseConnection db1			= null;
		DatabaseConnection db2			= null;
		DatabaseConnection db3			= null;
		MongoCollection<Document> coll1	= null;
		MongoCollection<Document> coll2	= null;
		MongoCollection<Document> coll3	= null;
		Document doc1					= null;
		Document doc2					= null;
		String displayName				= null;
		StringBuilder sb				= null;
		ObjectId h_id					= null;
		ObjectId n_id					= null;
		
		sb = new StringBuilder();
		sb.append(fname).append(" ").append(lname);
		displayName = sb.toString();
		username = username.toLowerCase();
		
		/* fetch associated household id */
		db1 = new DatabaseConnection("invite");
		coll1 = db1.getDatabase().getCollection("InviteCode");
		doc1 = coll1.find(eq("code", inviteCode)).first();
		h_id = (ObjectId) doc1.get("H_id");
		db1.disconnect();
		
		/* store new neighbor account data */
		doc2 = new Document();
		doc2.append("H_id", h_id);
		doc2.append("user_name", username);
		doc2.append("display_name", displayName);
		doc2.append("email", email);
		doc2.append("photo", photo);			// TODO modify to either: save photo bit data or reference to local server storage
		doc2.append("reward_pts",  0);
		doc2.append("local_agent", false);
		doc2.append("last_login", Instant.now());
		db2 = new DatabaseConnection("standard");
		coll2 = db2.getDatabase().getCollection("Neighbor");
		coll2.insertOne(doc2);
		n_id = (ObjectId) doc2.getObjectId("_id");
		db2.disconnect();
		
		/* add new neighbor to household list */
		db3 = new DatabaseConnection("local_agent");
		coll3 = db3.getDatabase().getCollection("Household");
		coll3.findOneAndUpdate(eq("_id", h_id), new Document("$push", new Document("members", n_id)));
		db3.disconnect();
	}
	
	/**
	 * takes user password and stores to Authentication collection (hashed pass and salt)
	 * @param username
	 * @param passToSet
	 */
	public static void setNewPassword(String username, char[] passToSet) {
		DatabaseConnection db1			= null;
		DatabaseConnection db2			= null;
		ObjectId n_id					= null;
		MongoCollection<Document> coll1	= null;
		MongoCollection<Document> coll2	= null;
		Document doc1					= null;
		Document doc2					= null;
		String salt						= null;
		String hashed					= null;
		
		/* fetch associated neighbor id */
		db1 = new DatabaseConnection("standard");
		coll1 = db1.getDatabase().getCollection("Neighbor");
		doc1 = coll1.find(eq("user_name", username)).first();
		n_id = (ObjectId) doc1.getObjectId("_id");
		db1.disconnect();
		
		/* generate salted, hashed pasword */
		salt = PasswordManager.generateSalt();
		hashed = PasswordManager.hashPassword(passToSet, salt);
		PasswordManager.clear(passToSet);
		
		/* store credentials to database */
		db2 = new DatabaseConnection("write_credentials");
		coll2 = db2.getDatabase().getCollection("Authentication");
		doc2 = new Document();
		doc2.append("N_id", n_id);
		doc2.append("password", hashed);
		doc2.append("salt", salt);
		coll2.insertOne(doc2);
		db2.disconnect();
	}
}
