package com.k23b.team13.project3.ac.structures;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.classes.Agents;

import java.util.ArrayList;

/**
 *  <code>AgentsAdapter</code> is a singleton class that extends ArrayAdapter
 * 	@author C. Patsouras I. Venieris
 *	@version 1
 */
public class AgentsAdapter extends ArrayAdapter<Agents> {
    /**
     * Instance of this class.
     */
    private static AgentsAdapter instance = null;
    /**
     * Constructor of <code>AgentsAdapter</code> class.
     * @param context In order to access resources.
     * @param ar Contains agents to show.
     */
    public AgentsAdapter(Context context, ArrayList<Agents> ar) {
        super(context, R.layout.item_agent, ar);
    }
    /**
     * @param context In order to access resources.
     * @param ar Contains agents to show.
     * @return instance of this class.
     */
    public static AgentsAdapter getInstance (Context context, ArrayList<Agents> ar){
        if (instance == null){
            instance = new AgentsAdapter(context,ar);
        }
        return instance;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Agents user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_agent, parent, false);
        }
        // Lookup view for data population
        TextView hashkey = (TextView) convertView.findViewById(R.id.hashkey);
        ImageView online = (ImageView) convertView.findViewById(R.id.online);
        if(user.isOnline()) {
            online.setImageResource(R.drawable.agent_online);
        }
        else{
            online.setImageResource(R.drawable.agent_offline);
        }
        // Populate the data into the template view using the data object
        hashkey.setText("Software Agent " + Integer.toString(user.getHashKey()));
        // Return the completed view to render on screen

        return convertView;
    }
}
