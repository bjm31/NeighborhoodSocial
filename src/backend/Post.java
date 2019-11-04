package backend;

import org.bson.types.ObjectId;
import org.bson.Document;
import java.time.Instant;

public class Post {
	
	private ObjectId neighborID;
	private ObjectId householdID;
	private String displayName;
	private String type;
	private String content;
	private Instant timeStamp;
	private Document postDoc;
	
	/**
	 * default constructor
	 */
	public Post() {
		this.neighborID = null;
		this.householdID= null;
		this.displayName= null;
		this.type 		= null;
		this.content 	= null;
		this.timeStamp 	= Instant.now();
		this.postDoc	= new Document();
	}
	
	/**
	 * parameterized constructor
	 * @param id
	 * @param type
	 * @param content
	 */
	public Post(ObjectId n_id, ObjectId h_id, String displayName, String type, String content) {
		this.neighborID = n_id;
		this.householdID= h_id;
		this.displayName= displayName;
		this.type 		= type;
		this.content 	= content;
		this.timeStamp 	= Instant.now();
		this.postDoc	= new Document();
	}

	/**
	 * creates and sets mongo document
	 */
	public void createDocument() {
		Document doc = new Document();
		doc.append("N_id",  this.getNeighborID());
		doc.append("H_id",  this.getHouseholdID());
		doc.append("display_name",  this.getDisplayName());
		doc.append("type",  this.getType());
		doc.append("description",  this.getContent());
		doc.append("time_posted",  this.getTimeStamp());
		
		this.setPostDoc(doc);
	}
	/**
	 * @return the neighborID
	 */
	protected ObjectId getNeighborID() {
		return neighborID;
	}

	/**
	 * @param neighborID the neighborID to set
	 */
	protected void setNeighborID(ObjectId neighborID) {
		this.neighborID = neighborID;
	}

	/**
	 * @return the type
	 */
	protected String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	protected void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the content
	 */
	protected String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	protected void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the timeStamp
	 */
	protected Instant getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @return the householdID
	 */
	protected ObjectId getHouseholdID() {
		return householdID;
	}

	/**
	 * @param householdID the householdID to set
	 */
	protected void setHouseholdID(ObjectId householdID) {
		this.householdID = householdID;
	}
	
	/**
	 * @return the displayName
	 */
	protected String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	protected void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the postDoc
	 */
	protected Document getPostDoc() {
		return postDoc;
	}

	/**
	 * @param postDoc the postDoc to set
	 */
	protected void setPostDoc(Document postDoc) {
		this.postDoc = postDoc;
	}
}
