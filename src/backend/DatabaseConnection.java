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
	private final String connectionFileName = "connection.dat";
	private final String dbName = "TestDB";						//TODO change this before prod deployment
	
	protected DatabaseConnection(String connectionType) {
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
		
		credential = PasswordManager.getServiceCredentials(connectionType);
		file = new File(connectionFileName);
		
		try {
			input = new FileInputStream(file);
			scan = new Scanner(input);
			mongoURI = new MongoClientURI(scan.nextLine().trim() + credential.getUser()
				+ ":" + new String(credential.getPass()) + scan.nextLine().trim());
		} catch (FileNotFoundException e) {
			e.getMessage();
		}
		if (mongoClient == null) {
			throw new DatabaseConnectionFailureException("Failed to connect to database");
		}
		
		setMongoClient(new MongoClient(mongoURI));
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
}
