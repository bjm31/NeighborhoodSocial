package backend;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
//import java.io.File;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.BufferedReader;
//import java.util.Scanner;

class DatabaseConnection {
	
	private MongoDatabase database;
	private MongoClient mongoClient;
	private String dbName;
//	private final String connectionFileName = "connection.dat";
	
	protected DatabaseConnection(String connectionType) {
		this.dbName = null;
		connect(connectionType);		
	}

	/**
	 * 
	 * @param connectionType
	 * @throws DatabaseConnectionFailureException
	 */
	private void connect(String connectionType) {
		Credential credential		= null;
//		File file					= null;
//		FileInputStream	input		= null;
//		Scanner scan				= null;
		MongoClientURI mongoURI		= null;
		String uri1					= null;
		String uri2					= null;
				
		credential = PasswordManager.getServiceCredentials(connectionType);
		

/*
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
*/
dbName = "TestDB";
uri1 = "mongodb+srv://" + credential.getUser() + ":";								// TODO delete
uri2 = "@neighborhoodsocial-cv2tl.mongodb.net/admin?retryWrites=true&w=majority";   // TODO delete
		
		mongoURI = new MongoClientURI(uri1 + new String(credential.getPass()) + uri2);

		
		setMongoClient(new MongoClient(mongoURI));
		if (mongoClient == null) {
			System.out.println("Failed to connect to database");
			
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
	
/*	
	private String convertInputStream(InputStream inputStream) throws IOException{
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} finally {}
		
		return sb.toString();
	}
*/
	

}

