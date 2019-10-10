package test;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.Date;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.time.Instant;

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
	private String writeAuthCred[];

	public DatabaseConnection() {
		authCred 			= new String[] {"", ""}; 
		readCred 			= new String[] {"", ""};
		postCred 			= new String[] {"", ""};
		writeAuthCred 		= new String[] {"", ""};
		database 			= null;
		uri 				= null;
		mongoClient 		= null;
		
		readCredentials();
	} // end of DatabaseConnection()
	
	public String acceptInvite(String inviteCode) {
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
		
		coll.findOneAndUpdate(eq("code", inviteCode), new Document("$set", new Document("was_used", "true")));
		this.disconnect();
		
		return h_id;
	}
	
	public void createAccount(String h_id, String userName, String displayName, String email, String photo, String pass) {
		this.connect("newCred");

		Document doc = new Document("H_id", new ObjectId(h_id))
			.append("user_name", userName)
			.append("display_name", displayName)
			.append("email", email)
			.append("photo", photo)
			.append("reward_pts", 0)
			.append("local_agent", false)
			.append("last_login", Instant.now());
		
		MongoCollection<Document> coll = database.getCollection("Neighbor");
		coll.insertOne(doc);
		
		ObjectId n_id = (ObjectId)doc.get("_id");
		
		this.storeCredentials(n_id, "password");
		
		
		
		this.disconnect();
	}
	
	private void addToHousehold(ObjectId h_id, ObjectId n_id) {
		MongoCollection<Document> coll	= null;
		Document doc					= null;
		
		coll = database.getCollection("Household");
		doc = coll.find( eq("_id", h_id) ).first();
		
		
	}
	
	private void storeCredentials(ObjectId n_id, String pass) {
		String salt 					= "";
		String saltedHashed 			= "";
		SecureRandom random 			= null;
		Document doc 					= null;
		MongoCollection<Document> coll 	= null;
		
				
		byte[] bytes = new byte[20];
		random = new SecureRandom();
		random.nextBytes(bytes);
		salt = bytes.toString();
		
		saltedHashed = hashPassword(pass, salt);
		
		doc = new Document("N_id", n_id)
				.append("password", saltedHashed)
				.append("salt", salt);
		
		coll = database.getCollection("Authentication");
		coll.insertOne(doc);
	
	} // end of storeCredentials()
	
	public void login(String user) {
		
	} // end of login()
	
	private String hashPassword(String pass, String salt) {
		MessageDigest digest		= null;
		byte[] encodedHash 			= null;
		String saltPass				= "";
		
		saltPass = pass + salt;
		
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {}
		encodedHash = digest.digest(saltPass.getBytes(StandardCharsets.UTF_8));
		
		StringBuilder sb = new StringBuilder();
		for (byte b : encodedHash) {
			sb.append(String.format("%02x",  b));
		}
		
		return sb.toString();
		
	} // end of hashPassword()
	
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
			writeAuthCred[0] = scan.next().trim();
			writeAuthCred[1] = scan.next().trim();
			input.close();
		} catch(IOException e) {
			System.out.println("File not found exception");
		}
		
	} // end of readCredentials()
	
	private void connect(String connectionType) { // login, post, read, or newCred
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
		else if (connectionType.equals("newCred")) {
			user = writeAuthCred[0];
			pass = writeAuthCred[1];
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
