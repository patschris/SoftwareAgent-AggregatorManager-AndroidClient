package com.k23b.team13.project3.ac.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.activities.MainActivity;
import com.k23b.team13.project3.ac.structures.AgentsAdapter;
import com.k23b.team13.project3.ac.structures.AgentsList;


/**
 *  <code>AgentsFragment</code> is the <code>ListFragment</code> loaded if
 *  user select "see agents" option from <code>MainActivity</code>'s navigation drawer
 *  or from <code>WelcomeFragment</code>.
 *  If internet connection is not available while selecting "see agents" this fragment
 *  won't be loaded.<br></br>
 *  Its view is created after execution of <code>AgentsRequest</code>
 *  which is used for populating the <code>ListView</code> that contains the currently
 *  active <code>SoftwareAgent</code>s.
 *
 *  @author C. Patsouras I. Venieris
 *  @version 1
 */
public class AgentsFragment extends ListFragment {

    /**
     *  Constructs an <code>AgentsFragment</code>.
     */
    public AgentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // obtaining fragment's root view, setting a title on the appbar
        View rv = inflater.inflate(R.layout.agents_status_fragment, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.active_agents_title));
        // obtaining agents list and the agentsadapter
        // and setting the adapter to fragments listview
        AgentsList al = AgentsList.getInstance();
        AgentsAdapter aa = AgentsAdapter.getInstance(getActivity(), al.getAlist());
        setListAdapter(aa);
        return rv;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
