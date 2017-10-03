package com.k23b.team13.project3.ac.classes;

import android.os.Build;
/**
 * <code>LoginInfo</code> represents the credentials to be sent to Aggregator Manager in order to login.
 * @author C. Patsouras I. Venieris
 * @version 1
 */
public class LoginInfo {
    /**
     * Username of client who made login request.
     */
    private String username;
    /**
     * Password of client who made login request.
     */
    private String password;
    /**
     * Name of the device which user uses.
     */
    private String device;
    /**
     * Constructor of <code>LoginInfo</code> class.
     * @param username Username of client who made login request.
     * @param password Password of client who made login request.
     */
    public LoginInfo(String username, String password) {
        this.username = username;
        this.password = password;
        this.device = Build.DEVICE;
    }
    /**
     * @return Username of client who made login request.
     */
    public String getUsername() {
        return username;
    }
    /**
     *
     * @return Password of client who made login request.
     */
    public String getPassword() {
        return password;
    }

    public String getDevice(){ return device;}
}
