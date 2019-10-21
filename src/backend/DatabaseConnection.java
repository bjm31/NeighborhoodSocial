package backend;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import backend.exception.DatabaseConnectionFailureException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;;

class DatabaseConnection {
	
	private MongoDatabase database;
	private MongoClient mongoClient;
	private String dbName;
	private final String connectionFileName = "connection.dat";
	
	protected DatabaseConnection(String connectionType) {
		this.dbName = null;
		try {
			connect(connectionType);
		} catch (DatabaseConnectionFailureException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 
	 * @param connectionType
	 * @throws DatabaseConnectionFailureException
	 */
	private void connect(String connectionType) throws DatabaseConnectionFailureException {
		Credential credential		= null;
		File file					= null;
		FileInputStream	input		= null;
		Scanner scan				= null;
		MongoClientURI mongoURI		= null;
		String uri1					= null;
		String uri2					= null;
		char[] pass					= null;
		
		credential = PasswordManager.getServiceCredentials(connectionType);
		file = new File(connectionFileName);
		
		try {
			input = new FileInputStream(file);
			scan = new Scanner(input);
			setDbName(scan.nextLine().trim());
			uri1 = scan.nextLine().trim() + credential.getUser() + ":";
			uri2 = scan.nextLine().trim();
		} catch (FileNotFoundException e) {
			e.getMessage();
		}
		mongoURI = new MongoClientURI(uri1 + new String(credential.getPass()) + uri2);
		
		setMongoClient(new MongoClient(mongoURI));
		if (mongoClient == null) {
			throw new DatabaseConnectionFailureException("Failed to connect to database");
		}
		setDatabase(getMongoClient().getDatabase(dbName));		
	}

	
	/**
	 * @return the database
	 */
	protected MongoDatabase getDatabase() {
		MongoDatabase database = this.database;
		return database;
	}
	
	/**
	 * @param database the database to set
	 */
	protected void setDatabase(MongoDatabase database) {
		this.database = database;
	}

	/**
	 * @return the mongoClient
	 */
	protected MongoClient getMongoClient() {
		MongoClient mongoClient = this.mongoClient;
		return mongoClient;
	}

	/**
	 * @param mongoClient the mongoClient to set
	 */
	protected void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}
	
	protected void disconnect() {
		getMongoClient().close();
	}

	/**
	 * @return the dbName
	 */
	protected String getDbName() {
		String dbName = this.dbName;
		return dbName;
	}

	/**
	 * @param dbName the dbName to set
	 */
	protected void setDbName(String dbName) {
		this.dbName = dbName;
	}
	
	
}
