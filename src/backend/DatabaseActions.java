package backend;

//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.MongoCollection;
import org.bson.types.ObjectId;
//import org.bson.Document;
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
	

}
