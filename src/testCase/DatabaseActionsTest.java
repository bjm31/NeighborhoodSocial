package testCase;

import static org.junit.Assert.*;
import backend.DatabaseActions;
import org.bson.types.ObjectId;

import org.junit.Test;

public class DatabaseActionsTest {

	@SuppressWarnings("unused")
	@Test
	public void test() {
		
		
		ObjectId id = new ObjectId("5d9e6e03859f7f6355dcaaec");
		
		// is valid user authenticatd?
		assertTrue(DatabaseActions.login("tmorris", "password".toCharArray()));
		
		// in invalid user rejected?
		assertFalse(DatabaseActions.login("tmorris", "passw0rd".toCharArray()));
	}

}
