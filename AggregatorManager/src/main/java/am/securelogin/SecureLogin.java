package am.securelogin;

import java.security.MessageDigest;


/**	
 * <code>SecureLogin</code> is a singleton class that contains a method for
 * 	encrypting a password using a SHA-256 hash function.
 * 
 * 	@author		C. Patsouras I. Venieris
 *	@version	1
 */
public class SecureLogin {
	
	/**
	 * The instance of this class.
	 */
	private static SecureLogin instance=null;
	
	/**
	 * @return the instance of <code>SecureLogin</code>.
	 */
	public static SecureLogin getInstance(){
		if (instance!=null)	instance = new SecureLogin();
		return instance;
	}
	
	/**
	 * Constructor of <code>SecureLogin</code>.
	 */
	private SecureLogin(){}


	/**	Encrypts a given string-password using SHA-256.
	 * 	@param	base	the password to be encrypted.
	 * 	@return			a String with the encrypted password.
	 */
	public static String sha256(String base) {
		/*
		 http://stackoverflow.com/questions/3103652/hash-string-via-sha-256-in-java
		 */
		try{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(base.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				// encrypting the given password.
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
}