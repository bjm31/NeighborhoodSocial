package backend;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.*;


public class DatabaseActions {

	public static boolean login(String user, char[] passToCheck) {
		ObjectId n_id 			= null;
		DatabaseConnection db 	= null;
		String hash				= null;
		boolean isAuthenticated = false;
					
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
	

}
