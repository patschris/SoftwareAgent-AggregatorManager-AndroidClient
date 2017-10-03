package com.k23b.team13.project3.ac;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 *  <code>CheckConnection</code> is a singleton class used for retrieving information
 *  about connectivity state at a specific time.
 *
 *  @author C. Patsouras I. Venieris
 *  @version 1
 */
public class CheckConnection {

    /**
     *  the instance of this class.
     */
    private static CheckConnection instance = null;

    /**
     * <code>Context</code> needed for obtaining system information about
     * internet connection.
     */
    private Context acontext;


    /**
     *  Creates an instance of <code>CheckConnection</code> if not created yet, updating
     *  its fields if needed.
     *  @param  con a <code>Context</code> needed for obtaining system information.
     *  @return the instance of this class
     */
    public static CheckConnection getInstance(Context con){
        if (instance == null){
            CheckConnection.instance = new CheckConnection(con);
        }
        if (con != null){
            instance.setAcontext(con);
        }
        return instance;

    }

    /**
     * Constructs a <code>CheckConnection</code>.
     * @param con   a <code>Context</code> needed for obtaining system information.
     */
    private CheckConnection(Context con){
        instance = this;
        this.acontext = con;
    }

    /**
     * Sets the <code>Context</code> for this <code>CheckConnection</code> to the given value.
     * @param acontext  the <code>Context</code>.
     */
    public void setAcontext(Context acontext) {
        this.acontext = acontext;
    }

    /**
     * Obtains system information about internet connection using a <code>ConnectivityManager</code>.
     * @return true if there's an active internet connection, false if not so.
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) this.acontext.getSystemService(this.acontext.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null && ni.isConnected());
    }
}
