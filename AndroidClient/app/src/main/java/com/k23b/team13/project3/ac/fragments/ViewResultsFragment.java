package com.k23b.team13.project3.ac.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.activities.MainActivity;
import com.k23b.team13.project3.ac.classes.Result;
import com.k23b.team13.project3.ac.structures.ResultList;

import java.util.ArrayList;
import java.util.List;

/**
 *  <code>ViewResultsFragment</code> is the <code>ListFragment</code> loaded after
 *  execution of <code>ResultRequest</code> (invoked by pressing the "see results" button in
 *  <code>ResultsFragment</code>) which populates the <code>ListView</code> that contains
 *  the requested <code>Result</code>s.<br></br>.
 *  If internet connection is not available while pressing "see results" <code>Button</code>
 *  of <code>ResultsFragment</code>, this fragment won't be loaded.<br></br>.
 *  If user press back button while this fragment is displayed, <code>ResultsFragment</code> is
 *  popped from the backstack and displayed again.
 *
 *  @author C. Patsouras I. Venieris
 *  @version 1
 */
public class ViewResultsFragment extends ListFragment {

    /**
     *  list containing the requested <code>Result</code>s retrieved by
     *  <code>ResultRequest</code>.
     */
    private ResultList rl;


    /**
     *  @return a String list containing the requested <code>Results</code>
     *          used for populating this fragment's <code>ListView</code>.
     */
    private List<String> forResults() {
        List<String> list = new ArrayList<String>();
        for (Result a : rl.getAlist()) {
            list.add("\n" + a.toString() +"\n");
        }
        return list;
    }

    /**
     * Constructs a <code>ViewResultsFragment</code>.
     */
    public ViewResultsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // obtaining the root view of the fragment and its listview and setting appbar's title
        View v = inflater.inflate(R.layout.view_results_fragment, container, false);
        ListView lv = (ListView) v.findViewById(android.R.id.list);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.view_results_title));
        // obtaining resultlist
        rl= ResultList.getInstance();
        List<String> ls = forResults();
        // setting informative messages
        TextView desc = (TextView) v.findViewById(R.id.description);
        if (ls.size() == 0){
            ls.add(getString(R.string.no_results_returned));
        }
        String s = rl.getNum() + " " + getString(R.string.results_from) + " ";
        if (rl.getHashkey().equals(getString(R.string.results_all))){
            s+= getString(R.string.results_all_agents);
        }
        else{
            s += rl.getHashkey() + " " + getString(R.string.results_agent);
        }
        desc.setText(s);
        // obtaining the results adapter and setting it to fragment's listview
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ls);
        lv.setAdapter(dataAdapter);
        return v;

    }

    @Override
    public ListAdapter getListAdapter() {
        return super.getListAdapter();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
