package com.k23b.team13.project3.ac.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.CheckConnection;
import com.k23b.team13.project3.ac.DBHelper;
import com.k23b.team13.project3.ac.activities.MainActivity;
import com.k23b.team13.project3.ac.classes.Job;
import com.k23b.team13.project3.ac.classes.StopPeriodic;
import com.k23b.team13.project3.ac.services.StopPeriodicRequest;
import com.k23b.team13.project3.ac.structures.PeriodicJobList;
import com.k23b.team13.project3.ac.structures.PeriodicsAdapter;

import java.util.ArrayList;

/**
 *  <code>StopPeriodicFragment</code> is the <code>ListFragment</code> loaded if
 *  user select "stop a periodic job" option from <code>MainActivity</code>'s navigation drawer
 *  or from <code>WelcomeFragment</code>.
 *  If internet connection is not available while selecting "stop a periodic job" this fragment
 *  won't be loaded.<br></br>
 *  Its view is created after execution of <code>GetPeriodicsRequest</code>
 *  which is used for populating the <code>ListView</code> that contains the currently
 *  running periodic <code>NmapJobs</code>s.<br></br>
 *  A "stop" <code>Button</code> is used to invoke execution of a
 *  <code>StopPeriodicRequest</code> about the selected job if internet is available
 *  at the moment or for inserting this choice in the underlying SQLite database.
 *
 *  @author C. Patsouras I. Venieris
 *  @version 1
 */
public class StopPeriodicFragment extends ListFragment implements View.OnClickListener {

    /**
     *  list containing the curently running periodic jobs as they where
     *  retrieved by a <code>GetPeriodicsRequest</code>.
     */
    private PeriodicJobList pl;

    /**
     *  <code>Button</code> used for sending a <code>StopPeriodicRequest</code> or
     *  inserting this choice in the database if no connection is available.
     */
    private Button stopButton;

    /**
     *  Selected job position.
     */
    private int position;

    /**
     *  The <code>ListView</code> of this <code>ListFragment</code>.
     */
    private ListView lv;

    /**
     * The underlying SQLite database.
     */
    private DBHelper mydb;

    /**
     *  Constructs a <code>StopPeriodicFragment</code>.
     */
    public StopPeriodicFragment(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // obtaining the root view of the fragment and setting appbar's title
        View v = inflater.inflate(R.layout.stop_periodic_fragment, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.stop_periodic_title));
        // getting the currently running periodic job list
        pl = PeriodicJobList.getInstance();
        // initializing selected item's position
        position = -1;
        // obtaining the underlying database, list view and stop button
        mydb = new DBHelper(getActivity());
        lv = (ListView) v.findViewById(android.R.id.list);
        stopButton = (Button)v.findViewById(R.id.stop_periodic_button);

        // obtainig the periodic job adapter and setting it to fragment's listview
        PeriodicsAdapter pa = PeriodicsAdapter.getInstance(getActivity(),pl.getAlist());
        lv.setAdapter(pa);

        // if no periodic jobs are running now, no need to display stop button
        if (pl.getAlist().isEmpty()) v.findViewById(R.id.stop_periodic_button).setVisibility(View.GONE);
        stopButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // setting the selected item's position on clicking it
        this.position = position;


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.stop_periodic_button){
            // if stop button was clicked..
            if (position >= 0) {
                // ..and a job was selected, retrieve periodic job's identifier
                // and its host agent's hashkey
                String s = ((Job) lv.getItemAtPosition(position)).toString();
                String[] ar = s.split("\n");
                String[] ar2 = ar[0].split(":");
                String id = ar2[0];
                String[] ar3 = ar[1].split(" ");
                String hashkey = ar3[0];
                CheckConnection cc = CheckConnection.getInstance(getActivity());
                if (cc.isNetworkAvailable()) {
                    // send a StopPeriodicRequest if connection is available..
                    new StopPeriodicRequest((ProgressBar) getActivity().findViewById(R.id.progressBar1), getActivity(),position, stopButton).execute(id, hashkey);

                }
                else  {
                    // .. inform user and insert choice in database if not so
                    Toast toast = Toast.makeText(getActivity(), R.string.no_internet_stop_job, Toast.LENGTH_SHORT);
                    toast.show();
                    StopPeriodic sp = new StopPeriodic(0,Integer.parseInt(id),mydb.getConnectedUser().getUsername(),Integer.parseInt(hashkey));
                    mydb.insertIntoStopPeriodic(sp);
                }
                position = -1;
            }
            else{
                // no job was selected
                Toast toast = Toast.makeText(getActivity(), R.string.select_stop_job, Toast.LENGTH_SHORT);
                toast.show();
            }

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
