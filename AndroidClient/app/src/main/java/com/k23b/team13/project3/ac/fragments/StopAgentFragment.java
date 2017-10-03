package com.k23b.team13.project3.ac.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.CheckConnection;
import com.k23b.team13.project3.ac.DBHelper;
import com.k23b.team13.project3.ac.activities.MainActivity;
import com.k23b.team13.project3.ac.classes.Agents;
import com.k23b.team13.project3.ac.classes.KillAgent;
import com.k23b.team13.project3.ac.services.KillAgentRequest;
import com.k23b.team13.project3.ac.structures.AgentsAdapter;
import com.k23b.team13.project3.ac.structures.AgentsList;

/**
 *  <code>StopAgentFragment</code> is the <code>ListFragment</code> loaded if
 *  user select "stop an agent" option from <code>MainActivity</code>'s navigation drawer
 *  or from <code>WelcomeFragment</code>.
 *  If internet connection is not available while selecting "stop an agent" this fragment
 *  won't be loaded.<br></br>
 *  Its view is created after execution of <code>AgentsRequest</code>
 *  which is used for populating the <code>ListView</code> that contains the currently
 *  active <code>SoftwareAgent</code>s.<br></br>
 *  A "stop" <code>Button</code> is used to invoke execution of a
 *  <code>KillAgentRequest</code> about the selected agent if internet is available
 *  at the moment or for inserting this choice in the underlying SQLite database.
 *
 *  @author C. Patsouras I. Venieris
 *  @version 1
 */
public class StopAgentFragment extends ListFragment implements View.OnClickListener {

    /**
     *  The <code>ListView</code> of this <code>ListFragment</code>.
     */
    private ListView lv = null;

    /**
     *  <code>Button</code> used for sending a <code>KillAgentRequest</code> or
     *  inserting this choice in the database if no connection is available.
     */
    private Button stopButton;

    /**
     *  Selected agent.
     */
    private int selected;

    /**
     *  Adapter used for inflating the view for the items
     *  of this <code>Fragment</code>'s <code>ListView</code>.
     */
    private AgentsAdapter aa;

    /**
     *  Used for obtaining connectivity status at a specific moment.
     */
    private CheckConnection cc = null;

    /**
     * The underlying SQLite database.
     */
    private DBHelper mydb;

    /**
     *  Constructs a <code>StopAgentFragment</code>.
     */
    public StopAgentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // obtaining the root view of the fragment and setting appbar's title
        View v = inflater.inflate(R.layout.stop_agent_fragment, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.stop_agent));
        // obtaining the listview of this fragment and the stop button
        lv = (ListView) v.findViewById(android.R.id.list);
        stopButton = (Button)v.findViewById(R.id.stop_agent_button);
        // setting listener for stop button
        stopButton.setOnClickListener(this);
        // obtaining agentlist, connection check and database
        AgentsList al = AgentsList.getInstance();
        selected = -1;
        cc = CheckConnection.getInstance(getActivity());
        mydb = new DBHelper(getActivity());

        // obtaining the agents adapter and setting it to fragment's listview
        aa = AgentsAdapter.getInstance(getActivity(),al.getAlist());
        lv.setAdapter(aa);
        // no agent active right now, no need to display the stop button
        if (al.getAlist().isEmpty()) stopButton.setVisibility(View.GONE);

        return v;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // setting the selected item's position on clicking it
        selected = position;


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.stop_agent_button)
        {
            // if stop button was clicked ..
            if (selected != -1) {
                // .. and an agent was selected
                Agents s = (Agents) lv.getItemAtPosition(selected);
                // obtain its hashkey
                String hashkey = Integer.valueOf(s.getHashKey()).toString();
                if (cc.isNetworkAvailable()) {
                    // send a KillAgentRequest if connection is available..
                    new KillAgentRequest(getActivity(), aa, Integer.valueOf(hashkey), stopButton).execute();
                }
                else{
                    // .. inform user and insert choice in database if not so
                    Toast toast = Toast.makeText(getActivity(), R.string.no_internet_stop_agent, Toast.LENGTH_SHORT);
                    toast.show();
                    KillAgent ka = new KillAgent(0,Integer.parseInt(hashkey),mydb.getConnectedUser().getUsername());
                    mydb.insertIntoKillAgent(ka);
                }
            }
            else{
                // no agent was selected
                Toast toast = Toast.makeText(getActivity(), R.string.select_agent_stop, Toast.LENGTH_SHORT);
                toast.show();
            }
            selected = -1;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
