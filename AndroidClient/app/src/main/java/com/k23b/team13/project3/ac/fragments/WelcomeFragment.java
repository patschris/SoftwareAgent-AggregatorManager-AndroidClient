package com.k23b.team13.project3.ac.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.CheckConnection;
import com.k23b.team13.project3.ac.activities.MainActivity;
import com.k23b.team13.project3.ac.services.AgentsRequest;
import com.k23b.team13.project3.ac.services.GetPeriodicsRequest;

/**
 *  <code>WelcomeFragment</code> is the default fragment (first fragment loaded on every
 *  <code>MainActivity</code>'s (re)creation ), not associated with a menu item from
 *  <code>MainActivity</code>'s navigation drawer.<br></br>
 *  It contains a scroll view showing the possible options to the user and brief info
 *  about each of them (except from the Logout option).
 *  Upon clicking of the header of an option, if internet connection is available at the moment
 *  the corresponding <code>Fragment</code> will be loaded in <code>MainActivity</code> an this option
 *  will be checked on the navigation drawer. <br></br>
 *  If a users press twice the back button during a specific time while <code>WelcomeFragment</code>
 *  is loaded, <code>MainActivity</code> gets destroyed. If back is pressed while
 *  <code>ViewResultsFragment</code> is displayed, <code>ResultsFragment</code> is popped back from
 *  the stack. Otherwise, <code>WelcomeFragment</code> is loaded again.
 *
 *  @author C. Patsouras I. Venieris
 *  @version 1
 */
public class WelcomeFragment extends Fragment {


    /**
     *  Constructs a <code>WelcomeFragment</code>.
     */
    public WelcomeFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // creates the view of the WelcomeFragment
        // sets listeners for its TextViews and a title to MainActivity's appbar

        View v = inflater.inflate(R.layout.welcome_fragment, container, false);

        v.findViewById(R.id.see_agents_click).setOnClickListener(headerListener);
        v.findViewById(R.id.send_job_click).setOnClickListener(headerListener);
        v.findViewById(R.id.see_results_click).setOnClickListener(headerListener);
        v.findViewById(R.id.stop_periodic_click).setOnClickListener(headerListener);
        v.findViewById(R.id.stop_agent_click).setOnClickListener(headerListener);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_activity_main));

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    // listener for the TextViews.
    private View.OnClickListener headerListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // get id of the clicked view
            int id = v.getId();
            CheckConnection cc = CheckConnection.getInstance(getActivity());
            boolean flag;
            MenuItem item = null;
            // if no connection is available now
            if (!(getActivity().findViewById(R.id.welcome_linear)).isClickable()){
                // if welcome_linear layout (layout containing the textviews)
                // is not clickable at the moment, return immediately
                return;
            }
            if (!cc.isNetworkAvailable()) {
                Toast toast;
                // show proper message to user
                toast = Toast.makeText(getActivity(), R.string.no_internet_option, Toast.LENGTH_SHORT);
                toast.show();
                // checked item on drawer isn't changed
                flag = false;
            }
            else{
                // checked item will change
                flag = true;
                // the fragment which will be loaded
                Fragment fragment = null;
                FragmentTransaction ft;
                // MainActivity's fragment manager
                FragmentManager fm = getActivity().getSupportFragmentManager();
                // set welcome_linear's clickability to false.
                // Once user has selected and option and internet connection is available
                // the parent layout is set unclickable so no more AsyncTasks will be started
                // until the corresponding to the first selection is completed and the appropriate
                // fragment is loaded. Welcome_layout will be clickable again
                // when WelcomeFragment recreates its view.
                (getActivity().findViewById(R.id.welcome_linear)).setClickable(false);

                // obtain the indeterminate horizontal progress bar of main activity
                ProgressBar pb = (ProgressBar) getActivity().findViewById(R.id.progressBar1);
                Class fragmentClass = null;
                Menu menu = ((NavigationView)getActivity().findViewById(R.id.nav_view)).getMenu();

                // obtain a class reference to the fragment that will be displayed
                // and find the corresponding item of the navigation drawer
                if (id == R.id.see_agents_click){
                    fragmentClass = AgentsFragment.class;
                    item = menu.findItem(R.id.see_status);
                }
                else if (id == R.id.stop_agent_click){
                    fragmentClass = StopAgentFragment.class;
                    item = menu.findItem(R.id.stop_agent);
                }
                else if (id == R.id.see_results_click){
                    fragmentClass = ResultsFragment.class;
                    item = menu.findItem(R.id.see_results);
                }
                else if (id == R.id.stop_periodic_click){
                    fragmentClass = StopPeriodicFragment.class;
                    item = menu.findItem(R.id.stop_periodic);
                }
                else if(id == R.id.send_job_click){
                    item = menu.findItem(R.id.create_job);
                    fragmentClass = SendJobsFragment.class;
                }
                if (fragmentClass != null){
                    // a fragment should be replaced
                    try {
                        // get an instance of it
                        fragment = (Fragment) fragmentClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // replace the current fragment with the new one
                    // transaction will be committed by asynctask
                    ft = fm.beginTransaction().replace(R.id.flContent, fragment);
                    // execute the appropriate asynctask
                    if (fragment instanceof StopPeriodicFragment){
                        new GetPeriodicsRequest(pb, ft, (MainActivity) getActivity()).execute();
                    }
                    else{
                        // .. with the appropriate arguments
                        new AgentsRequest(pb, ft, (MainActivity) getActivity(), fragment instanceof SendJobsFragment).execute();
                    }
                    // set activity's selected item number to the new value
                    ((MainActivity) getActivity()).setSelected(item.getItemId());
                }
            }
            if (flag && item != null){
                // check the selected item on the navigation drawer
                item.setChecked(true);
            }
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
