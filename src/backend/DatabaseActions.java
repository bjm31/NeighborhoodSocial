package backend;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;

public class DatabaseActions {

	/**
	 * Validates a user login. Returns true if valid, else false.
	 * @param userToCheck username
	 * @param passToCheck password
	 * @return valid or invalid user
	 */
	public static boolean login(String userToCheck, char[] passToCheck) {
		ObjectId n_id 					= null;
		DatabaseConnection db 			= null;
		DatabaseConnection db2 			= null;
		MongoCollection<Document> coll 	= null;
		String hash						= null;
		boolean isAuthenticated 		= false;
		String user						= null;
		
		user = userToCheck.toLowerCase();
		
					
		db = new DatabaseConnection("read_credentials");
		
		if (db.getDatabase().getCollection("Neighbor").find(eq("user_name", user)).first() == null) {
			return false;

		} else {
			n_id = db.getDatabase().getCollection("Neighbor").find(eq("user_name", user)).first().getObjectId("_id");
		}
		
		hash = PasswordManager.hashPassword(
				passToCheck, db.getDatabase().getCollection("Authentication").find(eq("N_id", n_id)).first().get("salt").toString());
		PasswordManager.clear(passToCheck);
		
		if (!(hash.equals(db.getDatabase().getCollection("Authentication").find(eq("N_id", n_id)).first().get("password").toString()))) {
			isAuthenticated = false;
		} else
			isAuthenticated = true;
		
		db.disconnect();
		
		db2 = new DatabaseConnection("standard_user");
		coll = db2.getDatabase().getCollection("Neighbor");
		coll.findOneAndUpdate(eq("_id", n_id), new Document("$set", new Document("last_login", Instant.now())));
		db2.disconnect();
				
		return isAuthenticated;
	}
	
	/**
	 * Retrieves neighbor document id, given a valid username
	 * @param username
	 * @return
	 */
	public static ObjectId getN_id(String username) {
		DatabaseConnection db 			= null;
		MongoCollection<Document> coll 	= null;
		ObjectId n_id					= null;
		
		db = new DatabaseConnection("standard_user");
		coll = db.getDatabase().getCollection("Neighbor");
		
		
		n_id = (ObjectId) coll.find(eq("user_name", username)).first().get("_id");
		db.disconnect();
		return n_id;
	}
	//Get N_id with display name
	public static ObjectId getN_id2(String display_name) {
		DatabaseConnection db 			= null;
		MongoCollection<Document> coll 	= null;
		ObjectId n_id					= null;
		
		db = new DatabaseConnection("standard_user");
		coll = db.getDatabase().getCollection("Neighbor");
		
		n_id = (ObjectId) coll.find(eq("display_name", display_name)).first().get("_id");
		
		db.disconnect();
		return n_id;
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
	public static void createNewUser(String inviteCode, String username, String fname, String lname, String email, String photo, byte[] file) {
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
		doc2.append("photo", photo);			
		doc2.append("fileData", file);
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
		
		/* generate salted, hashed password */
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
	
	/**
	 * Finds user in DB with their username and then saves a post the user made to the DB
	 * @param username
	 * @param postType
	 * @param postInfo
	 */
	public static void savePost(ObjectId n_id, String postType, String postInfo) {
		
		DatabaseConnection db			= null;
		MongoCollection<Document> coll	= null;
		Document doc					= null;
		String displayName 				= null;
		
		//Get neighbor ID, display name to add to post collection
		db = new DatabaseConnection("standard");
		coll = db.getDatabase().getCollection("Neighbor");
		doc = coll.find(eq("_id", n_id)).first();
		displayName = doc.getString("display_name");
		db.disconnect();
		
		//store post to DB
		db = new DatabaseConnection("standard");
		coll = db.getDatabase().getCollection("Post");

		doc = new Document();
		doc.append("N_id", n_id);
		doc.append("display_name", displayName);
		doc.append("type", postType);
		doc.append("description", postInfo);
		doc.append("time_posted", Instant.now());

		coll.insertOne(doc);
		db.disconnect();
		

	}
	public static String[] viewPosts() {
		
		DatabaseConnection db			= null;
		MongoCollection<Document> coll	= null;
		String[] dbPosts				= null;
		String postFormat				= null;
		StringBuilder sb				= null;
		DateFormat dateString			= null;
		int postNum = 0;
		int i = 0;
		
		dateString = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		db = new DatabaseConnection("standard");
		coll = db.getDatabase().getCollection("Post");

		FindIterable<Document> posts = coll.find();
		
		//makes array with size of number of posts
		postNum = (int) db.getDatabase().getCollection("Post").countDocuments();
		dbPosts = new String[postNum];
		
		
		for(Document doc : posts) {
			
			//Make post string break in certain places, so it is not too long on the page
			postFormat = doc.getString("description");
			
			sb = new StringBuilder(postFormat);
			
			for(int j = 70; j < postFormat.length(); j += 70) {
				
				sb.insert(j, "-</br>");
			}
			
			//Creates String to fill post array
			dbPosts[i] = "<p hidden>" + doc.getObjectId("N_id") + "</p>"
					    + "<b><p>\n" + doc.getString("display_name") 
						+ "\n at \n" +  dateString.format(doc.getDate("time_posted")) + "\n</b></p>"
						+ "<p><b>Type: [\n" + doc.getString("type") + "\n]</b></p></br>"
						+ "<p>\n" + sb + "\n</p>";
						 
			i++;
		}
		
		db.disconnect();
		return dbPosts;
	}

	
	public static boolean doesUsernameExist(String username) {
		DatabaseConnection db			= null;
		MongoCollection<Document> coll	= null;
		Document doc					= null;
		boolean exists = false;
		
		db = new DatabaseConnection("standard");
		coll = db.getDatabase().getCollection("Neighbor");
		doc = coll.find(eq("user_name", username)).first();
		if (!(doc == null)) {
			exists = true;
		}
		db.disconnect();
		return exists;
	}
	
	public static String[] getProfile(ObjectId n_id) {
		
		
		DatabaseConnection db			= null;
		MongoCollection<Document> coll	= null;
		Document doc					= null;		
		int numAttributes				= 6;	//Number of keys taken from Neighbor cluster
		String profileInfo[];
		
		//Connect to DB and find specific user
		db = new DatabaseConnection("standard");
		coll = db.getDatabase().getCollection("Neighbor");
		doc = coll.find(eq("_id", n_id)).first();
		
		profileInfo = new String[numAttributes];
		
		//Add all needed user info into a string array

		profileInfo[0] = "<b>Name: </b>" + doc.getString("display_name");
		profileInfo[1] = "<b>Email: </b>" + doc.getString("email");
		profileInfo[2] = doc.getString("photo");
		profileInfo[3] = "<b>Reward Points: </b>" + String.valueOf(doc.getInteger("reward_pts"));
		profileInfo[4] = "<b>Local Agent: </b>" + String.valueOf(doc.getBoolean("local_agent"));
		profileInfo[5] = "<b>Last Online: </b>" + String.valueOf(doc.getDate("last_login"));
		


		db.disconnect();
		return profileInfo;
		
	}
	public static byte[] getPicture(ObjectId n_id) {
		DatabaseConnection db			= null;
		MongoCollection<Document> coll	= null;
		Document doc					= null;	
		
		db = new DatabaseConnection("standard");
		coll = db.getDatabase().getCollection("Neighbor");
		doc = coll.find(eq("_id", n_id)).first();
		
		Binary data = doc.get("fileData", Binary.class);
		byte[] fileData = data.getData();
		

		db.disconnect();
		return fileData;
	}
	public static void setPicture(ObjectId n_id, byte[] file) {
		
		DatabaseConnection db			= null;
		MongoCollection<Document> coll	= null;
		Document doc					= null;	
		
		db = new DatabaseConnection("standard");
		coll = db.getDatabase().getCollection("Neighbor");
		doc = coll.find(eq("_id", n_id)).first();
		
		BasicDBObject newDoc = new BasicDBObject();
		newDoc.append("$set",  new BasicDBObject().append("fileData", file));
		
		
		coll.updateOne(doc, newDoc);
		db.disconnect();
	}
	//returns list of Neighbor Ids
	public static ObjectId[] getNeighborList() {
		
		DatabaseConnection db			= null;
		MongoCollection<Document> coll	= null;
		Document doc					= null;	
		int num;
		int i = 0;
		db = new DatabaseConnection("standard");
		coll = db.getDatabase().getCollection("Neighbor");
		
		FindIterable<Document> list = coll.find();
		
		//makes array with size of number of posts
		num = (int) db.getDatabase().getCollection("Neighbor").countDocuments();
		ObjectId[] n_ids = new ObjectId[num];
		
		for(Document d : list) {
			
			n_ids[i] = d.getObjectId("_id");
			i++;
		}
		
		db.disconnect();
		return n_ids; 
	}
	
	public static String[] getAllNames() {
		
		DatabaseConnection db			= null;
		MongoCollection<Document> coll	= null;
		Document doc					= null;	
		int num;
		db = new DatabaseConnection("standard");
		coll = db.getDatabase().getCollection("Neighbor");
		
		FindIterable<Document> list = coll.find();
		
		//makes array with size of number of posts
		num = (int) db.getDatabase().getCollection("Neighbor").countDocuments();
		String[] names = new String[num];
		int i = 0;
		for(Document d : list) {
			
			names[i] = d.getString("display_name");
			i++;
		}
		
		db.disconnect();
		return names;
	}
	
	public static String getDisplayName(ObjectId n_id) {
		
		DatabaseConnection db			= null;
		MongoCollection<Document> coll	= null;
		Document doc					= null;	
		String name						= null;
		
		db = new DatabaseConnection("standard");
		coll = db.getDatabase().getCollection("Neighbor");

		doc = coll.find(eq("_id", n_id)).first();

		name = doc.getString("display_name");
		db.disconnect();

		return name;
	}
	
	
	//store message to DB
	public static void saveMessage(ObjectId from, ObjectId to, String message) {
		
		DatabaseConnection db			= null;
		MongoCollection<Document> coll	= null;
		Document doc					= null;
	
		
		db = new DatabaseConnection("standard");
		coll = db.getDatabase().getCollection("Message");

		doc = new Document();
		doc.append("from_id", from);
		doc.append("to_id", to);
		doc.append("message", message);
		doc.append("time_posted", Instant.now());

		coll.insertOne(doc);
		db.disconnect();
	}
	

	
	public static String[][] getMessages(ObjectId n_id) {
		
		DatabaseConnection db			= null;
		MongoCollection<Document> coll	= null;
		String[][] messages				= null;
		String msgFormat				= null;
		StringBuilder sb				= null;
		int msgNum = 0;
		int i = 0;
		
		DateFormat dateString = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		db = new DatabaseConnection("standard");
		coll = db.getDatabase().getCollection("Message");

		FindIterable<Document> msgDocs = coll.find();
		
		//makes array with size of number of msgs
		//msgNum = (int) db.getDatabase().getCollection("Message").countDocuments();
		
		for(Document doc : msgDocs) {
			if(doc.getObjectId("to_id").compareTo(n_id) == 0 || doc.getObjectId("from_id").compareTo(n_id) == 0) {
				
				msgNum++;
			}
		}

		messages = new String[msgNum][4];
		
		
		for(Document doc : msgDocs) {
			if(doc.getObjectId("to_id").compareTo(n_id) == 0 || doc.getObjectId("from_id").compareTo(n_id) == 0) {
				
				//Make msg string break in certain places, so it is not too long on the page
				msgFormat = doc.getString("message");
				sb = new StringBuilder(msgFormat);
				
				for(int k = 70; k < msgFormat.length(); k += 70) {
					
					sb.insert(k, "-</br>");
				}
			
			
					
				messages[i][0] = getDisplayName(doc.getObjectId("from_id"));
				messages[i][1] = getDisplayName(doc.getObjectId("to_id"));
				messages[i][2] = "" + sb;
				messages[i][3] = dateString.format(doc.getDate("time_posted"));

				i++;
			}
			
		}
			
		
		db.disconnect();
		return messages;
	}
}
