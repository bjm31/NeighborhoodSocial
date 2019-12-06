/**
 *  Use this to initialize your new MongoDB Atlas database
 */
package initDatabase;

import java.util.Scanner;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


/**
 * @author Tim Morris
 *
 */
public class InitializeDatabase {
	
	public static void main(String[] args) {
		
		Scanner scan;
		String connectionString;
		String dbName;
		String initInviteCode = "afgTHS346723sdf";
		String initAddress;
		MongoClientURI uri;
		MongoClient client;
		MongoDatabase db;
		Document emptyDocument;
		Document inviteDoc;
		Document householdDoc;
		
		scan = new Scanner(System.in);
		
		System.out.println("Enter MongoDB Atlas Connection String\n:");
		connectionString = scan.next().trim();
		
		System.out.println("Enter the Database Name\n:");
		dbName = scan.next().trim();
		
		System.out.println("Enter the address for your initial household\n: ");
		initAddress = scan.next().trim();
		
		scan.close();
		
		System.out.println("The Required Invite Code to Add Your First Neighbor Will be:\n"
				+ initInviteCode);
		
		uri = new MongoClientURI(connectionString);
		client = new MongoClient(uri);
		
		db = client.getDatabase(dbName);
		
		emptyDocument = new Document();
		emptyDocument.append("DocType", "Placeholder");
		
		householdDoc = new Document();
		householdDoc.append("address", initAddress);
		
		inviteDoc = new Document();
		inviteDoc.append("H_id", householdDoc.get("_id"));
		inviteDoc.append("code", initInviteCode);
		inviteDoc.append("was_used", false);
		
		// Creates placeholder Collections if not already in existence
		db.getCollection("Authentication").insertOne(emptyDocument);
		db.getCollection("Household").insertOne(householdDoc);
		db.getCollection("InviteCode").insertOne(inviteDoc);
		db.getCollection("Neighbor").insertOne(emptyDocument);
		db.getCollection("Post").insertOne(emptyDocument);
		db.getCollection("Message").insertOne(emptyDocument);
		
		client.close();
	}

}
