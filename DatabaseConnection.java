import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

public class DatabaseConnection 
{
	
	private MongoDatabase database;
	private MongoClientURI uri;
	private MongoClient mongoClient;
	private String systemCredentialsFileName = "cred.dat";
	private String dbName = "TestDB";
	
	private String authCred[];
	private String readCred[];
	private String postCred[];

	public DatabaseConnection() {
		authCred = new String[] {"", ""}; 
		readCred = new String[] {"", ""};
		postCred = new String[] {"", ""};
		database = null;
		uri = null;
		mongoClient = null;
		
		this.readCredentials();
	} // end of DatabaseConnection()
	
	public void acceptInvite(String inviteCode) {
		this.connect("read");
		MongoCollection<Document> coll = database.getCollection("InviteCode");
//		Document doc = coll.find(eq("code", inviteCode)).first();
// NEED TO WRITE "InviteCodeNotFoundException"		
//		System.out.println(doc.toString());
		Document doc = coll.find().projection(fields(exclude("_id", "code", "was_used"))).first();
		String h_id = doc.toString();
		h_id = h_id.replace("{",  "");
		h_id = h_id.replace("}",  "");
		h_id = h_id.replace("Document", "");
		h_id = h_id.replace("H_id=", "");
		h_id = h_id.trim();
		System.out.println(h_id);
		
		coll.findOneAndUpdate(eq("code", inviteCode), new Document("$set", new Document("was_used", "true")));
		this.disconnect();
	}
	
	public void login(String user) {
		
	} // end of login()
	
	private void readCredentials() {
		Scanner scan = null;
		FileInputStream input = null;
		File file = new File(systemCredentialsFileName);
		try {
			input = new FileInputStream(file);
			scan = new Scanner(input);
			authCred[0] = scan.nextLine().trim();
			authCred[1] = scan.next().trim();
			readCred[0] = scan.next().trim();
			readCred[1] = scan.next().trim();
			postCred[0] = scan.next().trim();
			postCred[1] = scan.next().trim();
			input.close();
		} catch(IOException e) {
			System.out.println("File not found exception");
		}
		
	} // end of readCredentials()
	
	private void connect(String connectionType) { // login, post, or read
		String user = "";
		String pass = "";
		if (connectionType.equals("login")) {
			user = authCred[0];
			pass = authCred[1];
		}
		else if (connectionType.equals("post")) {
			user = postCred[0];
			pass = postCred[1];
		}
		else if (connectionType.equals("read")) {
			user = readCred[0];
			pass = readCred[1];
		}
System.out.println("Connecting as: " + user + "\t" + pass + "...");
// DATABASE CONNECTION FAILED EXCEPTION NEEDED
		uri = new MongoClientURI(
				"mongodb+srv://"+user+":"+pass+"@neighborhoodsocial-cv2tl.mongodb.net/admin?retryWrites=true&w=majority");
		mongoClient = new MongoClient(uri);
		database = mongoClient.getDatabase(dbName);

System.out.println("Connected to " + dbName); // REMOVE ME
	
	} // end of connect()
	
	private void disconnect() {
		System.out.println("Disconnecting...");
		mongoClient.close();
		System.out.println("Disconnected");
	} // end of disconnect()
	
} // end of DatabaseConnection class
