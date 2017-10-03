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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.CheckConnection;
import com.k23b.team13.project3.ac.activities.MainActivity;
import com.k23b.team13.project3.ac.classes.Agents;
import com.k23b.team13.project3.ac.services.ResultRequest;
import com.k23b.team13.project3.ac.structures.AgentsAdapter;
import com.k23b.team13.project3.ac.structures.AgentsList;

import java.util.ArrayList;
import java.util.List;

/**
 *  <code>ResultsFragment</code> is the <code>Fragment</code> loaded if
 *  user select "see results" option from <code>MainActivity</code>'s navigation drawer
 *  or from <code>WelcomeFragment</code>.
 *  If internet connection is not available while selecting "see results" this fragment
 *  won't be loaded.<br></br>
 *  Its view is created after execution of <code>AgentsRequest</code>
 *  which is used for populating the <code>Spinner</code> that contains the currently
 *  active <code>SoftwareAgent</code>s.<br></br>
 *  An <code>EditText</code> is used for inserting the number of results to be retrieved
 *  for a specific <code>SoftwareAgent</code> or all of them via a <code>ResultRequest</code>.
 *  Those results will soon get displayed by <code>ViewResultsFragment</code> and
 *  <code>ResultsFragment</code> will be added to the backstack.
 *  The request for results is executed only if there is available internet connection
 *  at the moment,otherwise user gets informed.
 *
 *  @author C. Patsouras I. Venieris
 *  @version 1
 *
 */
public class ResultsFragment extends Fragment implements View.OnClickListener {

    /**
     *  <code>OnResultViewListener</code> is an interface used for
     *  commiting a fragment transaction (replace <code>ResultsFragment</code>
     *  with <code>ViewResultsFragment</code>) and add to the backstack.
     */
    public interface OnResultViewListener {

        /**
         *  Implemented by <code>MainActivity</code> to replace <code>ResultsFragment</code>
         *  with <code>ViewResultsFragment</code>.
         */
        void onChangeFragment();
    }

    /**
     *  list containing the currently active <code>SoftwareAgents</code>.
     */
    private AgentsList al;

    /**
     *  used for showing the currently active <code>SoftwareAgents</code>.
     */
    private Spinner sp;

    /**
     *  Button used for sending the given request for results.
     *  Once pressed <code>ResultRequest</code> is executed if internet
     *  connection is available, otherwise user gets informed.
     */
    private Button seeRes;

    /**
     *  used for giving the number of results.
     */
    private EditText num;

    /**
     *  interface used for commiting a fragment transaction.
     */
    OnResultViewListener listener;

    /**
     *
     * @return  a String list containing the currently active
     *          <code>SoftwareAgents</code> used for populating
     *          the <code>Spinner</code>.
     */
    private List<String> forAgents() {
        List<String> list = new ArrayList<String>();
        list.add(getString(R.string.agents_all));
        for (Agents a : al.getAlist()) {
            int h = a.getHashKey();
            String s = getString(R.string.software_agent) + " " + Integer.toString(h);
            list.add(s);
        }
        return list;
    }

    /**
     * Constructs a <code>ResultsFragment</code>.
     */
    public ResultsFragment() {
        super();
    }

    @Override
    public void onAttach(Context context) {

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            listener = (OnResultViewListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // getting instance of agent list
        al = AgentsList.getInstance();
        AgentsAdapter aa = AgentsAdapter.getInstance(getActivity(), al.getAlist());
        // getting the root view of this fragment and
        // setting a title to activity's appbar
        View rv = inflater.inflate(R.layout.results_fragment, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.results_title));
        sp = (Spinner) rv.findViewById(R.id.agents_spinner);
        // creating an adapter used for viewing the active agents
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, forAgents());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // setting the adapter for active agents to the spinner
        sp.setAdapter(dataAdapter);
        // obtaining see results button and number of results edittext
        seeRes = (Button) rv.findViewById(R.id.get_results);
        // setting listener for button..
        seeRes.setOnClickListener(this);
        num = (EditText) rv.findViewById(R.id.agents_results_num);
        // and editor listener for number field
        num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // if send soft key is pressed perform
                // a see results button click
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    seeRes.performClick();
                    return true;
                }
                return false;
            }
        });

        return rv;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.get_results) {
            // see results button was clicked
            // retrieve agent(s) selected
            String s = (String) sp.getSelectedItem();
            String hashkey = null;
            String[] ar = s.split(" ");
            if (s.equals("All")) hashkey = "All";
            else hashkey = ar[2];
            // hide the keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (getActivity().getCurrentFocus() != null) imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            if (num.getText().toString().isEmpty()) {
                // if no number of results was given, inform the user
                Toast toast = Toast.makeText(getActivity(), R.string.select_number_results, Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                // otherwise, check for internet connection
                String number = num.getText().toString();
                CheckConnection cc = CheckConnection.getInstance(getActivity());
                if (!cc.isNetworkAvailable()){
                    // if not available, inform user
                    Toast toast = Toast.makeText(getActivity(), R.string.no_internet_later, Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    // else, execute the appropriate ResultRequest
                    new ResultRequest((MainActivity) getActivity(), listener).execute(hashkey, number);
                }
                // and clear selections
                num.setText("");
                sp.setSelection(0);
            }


        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
