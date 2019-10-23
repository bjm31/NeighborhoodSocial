package backend;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.ArrayList;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

class PasswordManager {
	
	private static final String credentialsFile = "cred.dat";

	/**
	 * 
	 * @param type service agent type (local_agent, standard_user, read_credentials, write_credentials, invite)
	 * @return needed credentials
	 */
	protected static Credential getServiceCredentials(String type) {
		Scanner scan						= null;
		File file							= null;
		FileInputStream	input 				= null;
		ArrayList<Credential> credentials 	= null;
		
		credentials = new ArrayList<Credential>();
		
credentials.add(new Credential("local_agent_svc", "iJCLgzzLb0siAbuX".toCharArray()));		// TODO - DELETE BEFORE PROD!
credentials.add(new Credential("standard_user_svc", "MlR6a1KKOyArWTv1".toCharArray()));		// TODO - DELETE BEFORE PROD!
credentials.add(new Credential("read_credentials_svc", "uHZyAShB7d9GLzUq".toCharArray()));	// TODO - DELETE BEFORE PROD!
credentials.add(new Credential("write_credentials_svc", "8EzWQFTKd4tJY5rb".toCharArray())); // TODO - DELETE BEFORE PROD!
credentials.add(new Credential("invite_svc", "LYtJFkQym30Dc3CE".toCharArray()));			// TODO - DELETE BEFORE PROD!
/*		
		file = new File(credentialsFile);
		try {
			input = new FileInputStream(file);
			scan = new Scanner(input);
			while (scan.hasNext()) {
				credentials.add(new Credential(scan.nextLine().trim(), scan.nextLine().trim().toCharArray()));
			}			
		} catch (FileNotFoundException e) {
			System.out.println("Critical File Access Error");;
		}
*/
		
		for (Credential c : credentials) {
			if (c.getUser().contains(type)) {
				return c;
			}
		}
		return null;
	}
	
	/**
	 * generate random salt value (512 bit)
	 * @return random salt
	 */
	protected static String generateSalt() {
		SecureRandom random = null;
		StringBuilder salt	= null;
		byte[] saltInBytes	= null;
		
		saltInBytes = new byte[64];
		random = new SecureRandom();
		random.nextBytes(saltInBytes);
		
		salt = new StringBuilder();
		for (byte b : saltInBytes) {
			salt.append(String.format("%02x",  b));
		}

		return salt.toString();
	}
	
	/**
	 * adds a salt to password and hashes with SHA-512
	 * @param pass String	cleartext password
	 * @param salt String	random salt, either just generated or pulled from DB
	 * @return salted, hashed password String
	 */
	protected static String hashPassword(char[] pass, String salt) {
		MessageDigest digest		= null;
		byte[] hashInBytes			= null;
		StringBuffer hexString	 	= null;
		
		try {
			digest = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {}
		
		hashInBytes = digest.digest((new String(pass) + salt).getBytes(StandardCharsets.UTF_8));
		
		hexString = new StringBuffer();
		for (int i = 0; i < hashInBytes.length; i ++) {
			String hex = Integer.toHexString(0xff & hashInBytes[i]);
			if (hex.length() == 1) { 
				hexString.append('0');
			}
			hexString.append(hex);
			
		}
		
		return hexString.toString();
	}
	
	/**
	 * resets values in a char array
	 * @param charArray
	 */
	public static void clear(char[] charArray) {
		for (@SuppressWarnings("unused") char c : charArray) {
			c = 0;
		}
	}
	
}
