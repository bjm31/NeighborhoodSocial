package test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class SandBox {

	@SuppressWarnings("unused")
	@Test
	public void test() {
//		protected static String hashPassword(char[] pass, String salt)
char[] pass = "password".toCharArray();
String salt = "6acffac0101ddf2110ba72451e86ac6fe43a093bc8a74047bc1604fad887535ad"
		+ "0b73826a63511339ad5e6c35add3229bf084713e7f350f3552f208b3604719b";

		MessageDigest digest		= null;
		byte[] hashInBytes			= null;
		StringBuffer hexString	 	= null;

		try {
			digest = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {}
		
byte[] bytes = ("password"+salt).getBytes(StandardCharsets.UTF_8);
		
		hashInBytes = digest.digest("pass".getBytes(StandardCharsets.UTF_8));

		hexString = new StringBuffer();
		for (int i = 0; i < hashInBytes.length; i ++) {
			String hex = Integer.toHexString(0xff & hashInBytes[i]);
			if (hex.length() == 1) { 
				hexString.append('0');
			}
			hexString.append(hex);
	
		}
	}
}
