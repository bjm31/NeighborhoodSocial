/**
 * 
 */
package neighborhoodSocial;
import org.bson.types.ObjectId;

/**
 * @author Tim Morris
 * For testing the functionality of NeihgborhoodSocial package-only classes (such as PasswordManager)
 */
public class PackageTester {

	
	public static void main(String[] args) {
		/*
		String[] test = new String[2];
		String loginType = "query";
		*/
		
		/*
		test = PasswordManager.getServiceCredentials(loginType);  // (login, query, post, writeAuth)
		System.out.println(test[0]);
		System.out.println(test[1]);
		*/
		
		/*
		String test2 = "";
		test2 = pm.generateSalt() ;
		System.out.println(test2);
		*/
		
		/*
		String test3 = "";
		test3 = pm.hashPassword("Password", "b37834365f2dc3f3e7789f5eda003f6b42cf2227");
		System.out.println(test3);
		*/

		/*
		String inviteCode = "aRandomString";
		ObjectId id = null;
		try {
			id = InviteHandler.AcceptInvite(inviteCode);
			System.out.println(id.toString());
		} catch (InviteCodeNotFoundException e) {
			System.out.println("\"" + inviteCode + "\" not found.");
		}
		*/
		
		InviteHandler.generateInviteCode(new ObjectId("5d9bf1dc7b1e78663c91c924"));
		
	}

}
