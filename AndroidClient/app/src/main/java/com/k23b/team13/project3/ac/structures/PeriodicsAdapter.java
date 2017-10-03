package com.k23b.team13.project3.ac.structures;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.k23b.team13.project3.ac.classes.Job;

import java.util.ArrayList;

/**
 *  <code>PeriodicsAdapter</code> is a singleton class that extends ArrayAdapter
 * 	@author C. Patsouras I. Venieris
 *	@version 1
 */
public class PeriodicsAdapter extends ArrayAdapter <Job> {
    /**
     * Instance of this class.
     */
    private static PeriodicsAdapter instance = null;
    /**
     * Constructor of <code>PeriodicsAdapter</code> class.
     * @param context In order to access resources.
     * @param ar Contains jobs to show.
     */
    public PeriodicsAdapter(Context context, ArrayList<Job> ar) {
        super(context, android.R.layout.simple_list_item_1, ar);
    }
    /**
     * @param context In order to access resources.
     * @param ar Contains jobs to show.
     * @return instance of this class.
     */
    public static PeriodicsAdapter getInstance (Context context, ArrayList<Job> ar){
        if (instance == null){
            instance = new PeriodicsAdapter(context,ar);
        }
        return instance;
    }

}
