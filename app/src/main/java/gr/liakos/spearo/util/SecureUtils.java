package gr.liakos.spearo.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import androidx.core.util.Pair;
import android.util.Base64;

public class SecureUtils {
	
	public static boolean authenticate(String attemptedPassword, byte[] encryptedPassword, byte[] salt)
			   throws NoSuchAlgorithmException, InvalidKeySpecException {
			  // Encrypt the clear-text password using the same salt that was used to
			  // encrypt the original password
			  byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);
			 
			  // Authentication succeeds if encrypted password that the user entered
			  // is equal to the stored hash
			  return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
	}
	
	public static Pair<String, String> getPassWithSalt(String plainPass) throws NoSuchAlgorithmException, InvalidKeySpecException{
		byte[] saltbytes = generateSalt();
		byte[] encryptedPassword = getEncryptedPassword(plainPass, saltbytes);
		String saltString =  Base64.encodeToString(saltbytes, Base64.DEFAULT);
		String passString =  Base64.encodeToString(encryptedPassword, Base64.DEFAULT);
		
		Pair<String, String> passWithSalt = new Pair<String, String>(passString, saltString);
		return passWithSalt;
	}
	
	static byte[] getEncryptedPassword(String password, byte[] salt)
			   throws NoSuchAlgorithmException, InvalidKeySpecException {
			  // PBKDF2 with SHA-1 as the hashing algorithm. Note that the NIST
			  // specifically names SHA-1 as an acceptable hashing algorithm for PBKDF2
			  String algorithm = "PBKDF2WithHmacSHA1";
			  // SHA-1 generates 160 bit hashes, so that's what makes sense here
			  int derivedKeyLength = 160;
			  // Pick an iteration count that works for you. The NIST recommends at
			  // least 1,000 iterations:
			  // http://csrc.nist.gov/publications/nistpubs/800-132/nist-sp800-132.pdf
			  // iOS 4.x reportedly uses 10,000:
			  // http://blog.crackpassword.com/2010/09/smartphone-forensics-cracking-blackberry-backup-passwords/
			  int iterations = 20000;
			 
			  KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);
			 
			  SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
			 
			  return f.generateSecret(spec).getEncoded();
			 }
	
	static byte[] generateSalt() throws NoSuchAlgorithmException {
		  // VERY important to use SecureRandom instead of just Random
		  SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		 
		  // Generate a 8 byte (64 bit) salt as recommended by RSA PKCS5
		  byte[] salt = new byte[8];
		  random.nextBytes(salt);
		 
		  return salt;
	}

}
