package backend;
// this is a package-only class and should only be accessible to other classes within the neighborhoodSocial package

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

class PasswordManager {

	/**
	 * @param String type	service account type (login, query, post, writeAuth)
	 * @return String array with service account credentials
	 */
	protected static String[] getServiceCredentials(String type) {
		Scanner scan 			= null;
		File file				= null;
		FileInputStream input 	= null;
		String[] credsAll		= null;
		String[] credsOne		= null;
		int index				= 2;
		
		credsAll = new String[8];
		file = new File("cred.dat");
		try {
			input = new FileInputStream(file);
			scan = new Scanner(input);
			for (int i = 0; i < 8; i ++) {
				credsAll[i] = scan.nextLine().trim();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Critical File Access Error.");
		}
				
		if(type.equals("login")) {
			index = 0;
		} else if (type.equals("query")) {
			index = 2;
		} else if (type.equals("post")) {
			index = 4;
		} else if (type.equals("writeAuth")) {
			index = 6;
		}
		
		credsOne = new String[2];
		credsOne[0] = credsAll[index];
		credsOne[1] = credsAll[index + 1];
		
		return credsOne;
	}
	
	/**
	 * 
	 * @return random salt
	 */
	protected static String generateSalt() {
		SecureRandom random = null;
		StringBuilder salt	= null;
		byte[] saltInBytes	= null;
		
		saltInBytes = new byte[20];
		random = new SecureRandom();
		random.nextBytes(saltInBytes);
		
		salt = new StringBuilder();
		for (byte b : saltInBytes) {
			salt.append(String.format("%02x",  b));
		}

		return salt.toString();
	}
	
	/**
	 * 
	 * @param pass String	cleartext password
	 * @param salt String	random salt, either just generated or pulled from DB
	 * @return salted, hashed password String
	 */
	protected static String hashPassword(String pass, String salt) {
		MessageDigest digest		= null;
		byte[] hashInBytes			= null;
		String passWithSalt			= null;
		StringBuilder saltedHashed 	= null;
		
		passWithSalt = pass + salt;
		
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {}
		
		hashInBytes = digest.digest(passWithSalt.getBytes(StandardCharsets.UTF_8));
		
		saltedHashed = new StringBuilder();
		for (byte b : hashInBytes) {
			saltedHashed.append(String.format("%02x",  b));
		}
		
		return saltedHashed.toString();
	}

} // end of class
