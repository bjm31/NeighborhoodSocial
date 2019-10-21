package backend;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class PasswordManagerTest {

	@Test
	public void test() {
		char[] pass = "password".toCharArray();
		String salt = "6acffac0101ddf2110ba72451e86ac6fe43a093bc8a74047bc1604fad887535ad"
				+ "0b73826a63511339ad5e6c35add3229bf084713e7f350f3552f208b3604719b";
		
//		System.out.println(PasswordManager.generateSalt());
		
		System.out.println(PasswordManager.hashPassword(pass, salt));
		

		
	}

}
