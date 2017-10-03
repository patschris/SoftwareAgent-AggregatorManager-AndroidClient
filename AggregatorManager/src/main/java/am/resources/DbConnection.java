package am.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;



/**	<code>DbConnection</code> is a singleton class that contains used for connecting
 * 	to the underlying database.
 * 
 *	@author		C. Patsouras I. Venieris
 *	@version	1
 */
public class DbConnection {
	/**
	 * Username for connecting to database.
	 */
	private static String username;
	
	/**
	 * Password for connecting to database.
	 */
	private static String password;
	
	/**
	 * Driver used for connecting to database.
	 */
	private static String driver;
	
	/**
	 * URL of the underlying database.
	 */
	private static String url;
	
	/**
	 * The instance of this class.
	 */
	private static DbConnection instance = null ;

	/**
	 * Constructor of the <code>DbConnection</code>. Reads from property file
	 * info used for connecting to the underlying database.
	 */
	private DbConnection(){
		try {
			// reading the appropriate properties from property file
			File file = new File("src/main/java/am/resources/server.properties");
			FileInputStream fileInput = new FileInputStream(file);
			Properties prop = new Properties();
			prop.load(fileInput);
			fileInput.close();
			// sets username, password, driver and url
			// to the values read from property file
			username = prop.getProperty("Username");
			password = prop.getProperty("Password");
			driver = prop.getProperty("JDBC_DRIVER");
			url = prop.getProperty("DB_URL");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the instance of <code>DbConnection</code> class.
	 */
	public static synchronized DbConnection getInstance(){
		if (instance == null){
			instance = new DbConnection();
		}
		return instance;
	}

	/**
	 * @return 	username used for connecting to database.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return	password used for connecting to database.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return	driver used for connecting to database.
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * @return	URL of the underlying database.
	 */
	public String getUrl() {
		return url;
	}

}
