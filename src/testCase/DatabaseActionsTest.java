package testCase;

import static org.junit.Assert.*;
import backend.DatabaseActions;
import org.bson.types.ObjectId;
import backend.exception.*;

import org.junit.Test;

public class DatabaseActionsTest {

	@Test
	public void test() {
		
		
		ObjectId id = new ObjectId("5d9e6e03859f7f6355dcaaec");
		
		// is valid user authenticatd?
		try {
		assertTrue(DatabaseActions.login("tmorris", "password".toCharArray()));
		} catch (InvalidLoginException e) {}
		
		// in invalid user rejected?
		try {
			assertFalse(DatabaseActions.login("tmorris", "passw0rd".toCharArray()));
			} catch (InvalidLoginException e) {}
	
		
	}

}
