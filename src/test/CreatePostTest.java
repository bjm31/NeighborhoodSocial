package test;

import static org.junit.jupiter.api.Assertions.*;
import backend.DatabaseActions;
import org.bson.types.ObjectId;

import org.junit.jupiter.api.Test;

@SuppressWarnings("unused")
class CreatePostTest {

	@Test
	void test() {
		ObjectId id = new ObjectId("5dbffcb2860a4935a13d03c9");
		DatabaseActions.createPost(id.toString(), "For Sale", "Got an old couch, $5");
	}

}
