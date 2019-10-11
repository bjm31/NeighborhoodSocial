/**
 * 
 */
package neighborhoodSocial;


/**
 * @author Tim Morris
 * static class for managing database connections.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.Scanner;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

class DBConnection {
	
	private static MongoDatabase database;
	private static MongoClient	mongoClient	 = null;
	private static String connectionFileName = "connections.dat";
	
	protected static void connect (String connectionType) {
		String credentials[] 	= null;
		File file 				= null;
		FileInputStream input	= null;
		Scanner scan			= null;
		String dbName			= null;
		String uriPart1			= null;
		String uriPart2			= null;
		MongoClientURI uri		= null;
		
		credentials = PasswordManager.getServiceCredentials(connectionType);
		file = new File(connectionFileName);
		try {
			input = new FileInputStream(file);
			scan = new Scanner(input);
			dbName = scan.nextLine().trim();
			uriPart1 = scan.nextLine().trim();
			uriPart2 = scan.nextLine().trim();
		} catch (FileNotFoundException e) {
			System.out.println("Critical File Access Error.");
		}
		uri = new MongoClientURI(
				uriPart1 + credentials[0] + ":" + credentials[1] + uriPart2);
		setMongoClient(new MongoClient(uri));
		setDatabase(getMongoClient().getDatabase(dbName));
	}


	/**
	 * @return the database
	 */
	protected static MongoDatabase getDatabase() {
		return database;
	}


	/**
	 * @param database the database to set
	 */
	private static void setDatabase(MongoDatabase database) {
		DBConnection.database = database;
	}
	
	/**
	 * @return the mongoClient
	 */
	private static MongoClient getMongoClient() {
		return mongoClient;
	}


	/**
	 * @param mongoClient the database to set
	 */
	private static void setMongoClient(MongoClient mongoClient) {
		DBConnection.mongoClient = mongoClient;
	}
	
	protected static void disconnect() {
		getMongoClient().close();
	}


} // end of DBConnection class
