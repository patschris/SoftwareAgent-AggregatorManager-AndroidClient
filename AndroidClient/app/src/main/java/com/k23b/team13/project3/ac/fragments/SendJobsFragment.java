package com.k23b.team13.project3.ac.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.CheckConnection;
import com.k23b.team13.project3.ac.DBHelper;
import com.k23b.team13.project3.ac.activities.MainActivity;
import com.k23b.team13.project3.ac.classes.Agents;
import com.k23b.team13.project3.ac.classes.DistinctJob;
import com.k23b.team13.project3.ac.classes.Job;
import com.k23b.team13.project3.ac.classes.Users;
import com.k23b.team13.project3.ac.services.ToSendJobRequest;
import com.k23b.team13.project3.ac.structures.AgentsList;
import com.k23b.team13.project3.ac.structures.DistinctJobList;

import java.util.ArrayList;
import java.util.List;

/**
 *  <code>SendJobsFragment</code> is the <code>Fragment</code> loaded if user select
 *  "send a new job" option from <code>MainActivity</code>'s navigation drawer or from
 *  the <code>WelcomeFragment</code>.
 *  If internet connection is not available while selecting "send a new job" this fragment
 *  won't be loaded.<br></br>
 *  Its view is created after execution of two <code>AsyncTask</code>s, <code>AgentsRequest</code>
 *  which is used for populating the <code>Spinner</code> that contains the currently
 *  active <code>SoftwareAgent</code>s and <code>DistinctJobRequest</code> used for populating
 *  the <code>AutoCompleteTextView</code> where user selects the nmap job to send
 *  to an agent.<br></br>
 *  If internet connection is not available while pressing send button, an nmap job is
 *  inserted in the underlying SQLite database to be sent when connection is available again.
 *
 *  @author C. Patsouras I. Venieris
 *  @version 1
 *
 */
public class SendJobsFragment extends Fragment implements View.OnClickListener{

    /**
     *  list containing the currently active <code>SoftwareAgents</code>.
     */
    private AgentsList al;

    /**
     *  used for showing the currently active <code>SoftwareAgents</code>.
     */
    private Spinner sp;

    /**
     *  Used by user to insert the job he wishes to send to an agent.
     *  Populated with <code>DistinctJob</code> received from
     *  <code>DistinctJobRequest</code>.
     */
    private AutoCompleteTextView aview;

    /**
     *  RadioGroup containing the periodic: yes-no <code>RadioButton</code>s.
     */
    private RadioGroup rgroup;

    /**
     *  Button used for clearing the currently given input.
     */
    private Button clear;

    /**
     *  Button used for sending the given NmapJob.
     *  Once pressed <code>ToSendJobRequest</code> is executed if internet
     *  connection is available, otherwise given job is inserted in database.
     */
    private Button send;

    /**
     *  Used for giving the period of a periodic NmapJob.
     */
    private EditText time;

    /**
     *  List containing the different arguments/NmapJobs that
     *  have been assigned to <code>SoftwareAgent</code>s.
     */
    private DistinctJobList dj;

    /**
     *  The checked <code>RadioGroup</code>'s checked <code>RadioButton</code>.
     */
    private int selected;

    /**
     *  Used for obtaining connectivity status at a specific moment.
     */
    private CheckConnection cc;

    /**
     * The underlying SQLite database.
     */
    private DBHelper mydb;

    /**
     * Constructs a <code>SendJobsFragment</code>.
     */
    public SendJobsFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @return  a String list containing the currently active
     *          <code>SoftwareAgents</code> used for populating
     *          the <code>Spinner</code>.
     */
    private List<String> forAgents() {
        List<String> list = new ArrayList<String>();
        for (Agents a : al.getAlist()) {
            int h = a.getHashKey();
            String s = getString(R.string.software_agent) + " " + Integer.toString(h);
            list.add(s);
        }
        return list;
    }

    /**
     *
     * @return  a String list containing the different arguments/Nmap commands
     *          that have been assigned to <code>SoftwareAgent</code>s, used for
     *          populating the <code>AutoCompleteTextView</code>.
     */
    private List<String> forJobs(){
        List<String> list = new ArrayList<String>();
        for (DistinctJob j : dj.getAlist()){
            list.add(j.getCommand());
        }
        return list;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // getting instances of agent and distinct job lists
        al = AgentsList.getInstance();
        // getting the root view of this fragment
        View rv = inflater.inflate(R.layout.send_jobs_fragment, container, false);
        if (al.getAlist().isEmpty()){
            // if no agent is currently active, hide (radio) buttons, textviews etc
            // and display just an info message
            rv.findViewById(R.id.no_agents_jobs).setVisibility(View.VISIBLE);
            rv.findViewById(R.id.agents_exist_jobs).setVisibility(View.GONE);
            return rv;
        }
        dj = DistinctJobList.getInstance();
        // setting a title to activity's appbar
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.send_job_title));
        // creating an adapter used for viewing the nmap commands
        ArrayAdapter<String> jobsadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, forJobs());

        sp = (Spinner) rv.findViewById(R.id.agents_jobs_spinner);
        aview =(AutoCompleteTextView) rv.findViewById(R.id.auto_jobs);
        // setting the adapter for autocomplete nmap job command field
        aview.setAdapter(jobsadapter);
        // creating an adapter used for viewing the active agents
        ArrayAdapter<String> agentAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, forAgents());
        agentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // setting the adapter for active agents to the spinner
        sp.setAdapter(agentAdapter);
        // obtaining radiogroup
        rgroup = (RadioGroup) rv.findViewById(R.id.radio_group);
        // default option is one-time nmap job
        selected = R.id.one_time_job_button;

        // obtaining buttons and editext, setting listeners
        clear = (Button) rv.findViewById(R.id.clear_job_button);
        send = (Button) rv.findViewById(R.id.send_job_button);
        time = (EditText) rv.findViewById(R.id.periodic_job_time);
        clear.setOnClickListener(this);
        send.setOnClickListener(this);
        rgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // change selected button's id
                selected = checkedId;
            }
        });
        cc = CheckConnection.getInstance(getActivity());
        // obtaining the underlying database
        mydb = new DBHelper(getActivity());

        // setting a listener for send soft key
        time.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // if send soft key is pressed perform
                // a send job button click
                if(actionId == EditorInfo.IME_ACTION_SEND){
                    send.performClick();
                    return true;
                }
                return false;
            }
        });
        return rv;

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.clear_job_button) {
            // clear button was clicked, empty input fields
            // and recheck one-time radio button
            aview.setText("");
            time.setText("");
            rgroup.check(R.id.one_time_job_button);

        }
        else if (v.getId() == R.id.send_job_button){
            String command = aview.getText().toString();
            // hide the keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (getActivity().getCurrentFocus() != null) imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            if(sp.getSelectedItemPosition() == -1){
                // no agent was selected
                Toast toast = Toast.makeText(getActivity(), R.string.no_agent_selected, Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            if (command.isEmpty()){
                // no command was given as input, inform user
                Toast toast = Toast.makeText(getActivity(), R.string.set_command, Toast.LENGTH_SHORT);
                time.setText("");
                rgroup.check(R.id.one_time_job_button);
                toast.show();
                return;
            }
            else if (!command.contains("nmap") || !command.contains("-oX -")){
                // the given command is not valid
                Toast toast = Toast.makeText(getActivity(), R.string.invalid_command, Toast.LENGTH_SHORT);
                aview.setText("");
                time.setText("");
                rgroup.check(R.id.one_time_job_button);
                toast.show();
                return;
            }
            // obtaining selected agent's hashkey
            String s = (String) sp.getSelectedItem();
            String hashkey;
            String[] ar = s.split(" ");
            hashkey = ar[2];
            // obtaining the given period and the checked button
            String timePeriod = time.getText().toString();
            int id = rgroup.getCheckedRadioButtonId();
            String periodic;
            if (id == R.id.periodic_job_button) {
                // periodic button is checked..
                periodic = "true";
                if (timePeriod.isEmpty()){
                    // but no period is given, inform user
                    Toast toast = Toast.makeText(getActivity(), R.string.set_period, Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
            } else {
                // one time job was given
                periodic = "false";
                timePeriod = "0";
            }
            if (cc.isNetworkAvailable()) {
                // connection available right now, send the given job
                // execute the asynctask
                new ToSendJobRequest(getActivity()).execute(command, periodic, timePeriod, hashkey);
            }
            else{
                // internet not available right now
                // inform user, get the connected user, create a nmapjob
                // and insert it to database
                // this job will be sent when connection is available again
                Toast toast = Toast.makeText(getActivity(), R.string.no_internet_job, Toast.LENGTH_LONG);
                toast.show();
                Users u = mydb.getConnectedUser();
                Job j = new Job(0,command,Boolean.parseBoolean(periodic),Integer.parseInt(timePeriod),Integer.parseInt(hashkey), u.getUsername());
                mydb.insertIntoJob(j);
            }
            // reset selections
            aview.setText("");
            time.setText("");
            rgroup.check(R.id.one_time_job_button);
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
