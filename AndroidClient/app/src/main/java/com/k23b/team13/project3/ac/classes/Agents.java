package com.k23b.team13.project3.ac.classes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *   Software Agents returned by web service.
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Agents {
    /**
     * HashKey	describing this client (primary key for Agents table in database).
     */
    private int hashkey;
    /**
     * Status of this client (online or offline).
     */
    private boolean online;
    /**
     * Constructor of Agents' class.
     * @param hashkey HashKey	describing this client (primary key for Agents table in database).
     * @param online Status of this client (online or offline).
     */
    public Agents(int hashkey, boolean online) {
        this.hashkey = hashkey;
        this.online = online;
    }
    /**
     * Constructor of Agents' class.
     * @param object JSON object that contains hashkey and online fields.
     */
    public Agents(JSONObject object){
        try {
            this.hashkey = object.getInt("hashkey");
            this.online = object.getBoolean("online");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
      public Agents(){}

    /**
     * @return 	HashKey	describing this client (primary key for Agents table in database).
     */
    public int getHashKey() {
        return hashkey;
    }
    /**
     * @return	true if this client is currently online, false if not so.
     */
    public boolean isOnline() {
        return online;
    }
}
