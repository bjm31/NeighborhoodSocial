package backend;

import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.MongoDatabase;

import backend.exception.InviteCodeNotFoundException;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.Random;

public class InviteHandler {

	public static ObjectId AcceptInvite(String inviteCode) throws InviteCodeNotFoundException{
		MongoDatabase database 					= null;
		MongoCollection<Document> collection 	= null;
		Document document						= null;
		ObjectId h_id							= null;
		
		DBConnection.connect("query");
		database = DBConnection.getDatabase();
		collection = database.getCollection("InviteCode");
		document = collection.find(eq("code", inviteCode)).first();
		if (document == null) {
			throw new InviteCodeNotFoundException();
		} else
			
		h_id = document.getObjectId("H_id");
		collection.findOneAndUpdate(eq("code", inviteCode), new Document("$set", new Document("was_used", true)));
		DBConnection.disconnect();
		return h_id;
	}
	
	
	public static String generateInviteCode(ObjectId h_id) {
		MongoDatabase database 					= null;
		MongoCollection<Document> collection 	= null;
		Document document						= null;
		String inviteCode						= null;
		
		inviteCode = _generateInviteCode();
		DBConnection.connect("post"); // fixme: look at creating special login type for local agent actions
		database = DBConnection.getDatabase();
		collection = database.getCollection("InviteCode");
		document = new Document("H_id", h_id)
				.append("code", inviteCode)
				.append("was_used", false);
		collection.insertOne(document);
				
		DBConnection.disconnect();
		return inviteCode;
	}
	
	
	private static String _generateInviteCode() {
		char[] charset = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o',
				'p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F',
				'G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X',
				'Y','Z','0','1','2','3','4','5','6','7','8','9'};
		String inviteCode 	= "";
		Random random		= null;
		int codeLength		= 15;

		random = new Random();
		for (int i = 0; i < codeLength; i++) {
			inviteCode += charset[(char)random.nextInt(61)];
		}
		
		return inviteCode;
	}
	
} // end of class
