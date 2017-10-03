package com.k23b.team13.project3.ac.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.CheckConnection;
import com.k23b.team13.project3.ac.DBHelper;
import com.k23b.team13.project3.ac.classes.Users;
import com.k23b.team13.project3.ac.fragments.AgentsFragment;
import com.k23b.team13.project3.ac.fragments.ResultsFragment;
import com.k23b.team13.project3.ac.fragments.SendJobsFragment;
import com.k23b.team13.project3.ac.fragments.StopAgentFragment;
import com.k23b.team13.project3.ac.fragments.StopPeriodicFragment;
import com.k23b.team13.project3.ac.fragments.ViewResultsFragment;
import com.k23b.team13.project3.ac.fragments.WelcomeFragment;
import com.k23b.team13.project3.ac.services.AgentsRequest;
import com.k23b.team13.project3.ac.services.GetPeriodicsRequest;
import com.k23b.team13.project3.ac.services.LogoutRequest;
import com.k23b.team13.project3.ac.services.RemainingAgentsRequest;
import com.k23b.team13.project3.ac.services.RemainingPeriodicsRequest;
import com.k23b.team13.project3.ac.services.SendRemainingJobsRequest;

/** <code>MainActivity</code> is either started after the successful login of a user
 *  - user was not already logged in, password sent was right and AM accepted him - right after
 *  <code>LoginActivity</code> or by <code>OpeningActivity</code> if a connected user was found
 *  while searching the underlying SQLite database.<br></br>
 *  Its main UI is consisted of a Navigation Drawer with all the possible options and a
 *  FrameLayout used for carrying the currenty displayed <code>Fragment</code>.
 *  If internet connection is available upon selecting an item from the Drawer,
 *  an <code>AsyncTask</code> is executed and the corresponding <code>Fragment</code> gets displayed.
 *  Otherwise the selected option/fragment isn't loaded.
 *  When a user logs off, <code>MainActivity</code> finishes and a new <code>LoginActivity</code>
 *  is created.
 *  If a users press twice the back button during a specific time while <code>WelcomeFragment</code>
 *  is loaded, <code>MainActivity</code> gets destroyed. If back is pressed while
 *  <code>ViewResultsFragment</code> is displayed, <code>ResultsFragment</code> is popped back from
 *  the stack. Otherwise, <code>WelcomeFragment</code> is loaded again.
 *
 *  @author C. Patsouras I. Venieris
 *  @version 1
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ResultsFragment.OnResultViewListener {

    /**
     *  fragment manager used to replacing fragments
     */
    private FragmentManager fm = null;

    /**
     *  name of the logged in user.
     */
    private String name;

    /**
     *  the underlying SQLite database
     */
    private DBHelper mydb;

    /**
     *  the connected user.
     */
    private Users user;

    /**
     *  max time elapsed between two consecutive clicks
     *  of back button to exit the app.
     */
    private final int time_to_exit = 2000;

    /**
     *  time in milis since epoch describing for pressing
     *  the back button for the first time.
     */
    private long back_pressed;

    /**
     *  the drawer's navigation view.
     */
    private NavigationView navigationView;

    /**
     *  selected item of the drawer.
     */
    private int selected;

    /**
     *  indicating if the onDestroy() method for this activity has been called.
     *  checked by web services so that no fragment transactions are commited
     *  after <code>MainActivity</code>'s destruction.
     */
    private boolean destroyed;

    /**
     *  Creates a <code>MainActivity</code>, setting its content view components.
     *
     *  @param savedInstanceState   If the activity is being re-initialized after
     *                              previously being shut down then this Bundle
     *                              contains the most recently supplied data.
     *                              Otherwise is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // obtaining the logged in username by OpeningActivity
        // or after a successful LoginRequest
        Bundle b = getIntent().getExtras();
        name = b.getString("username");
        user = new Users(name, true);
        // getting the underlying SQLite database
        mydb = new DBHelper(this);
        destroyed = false;

        boolean flag = b.getBoolean("flag");
        if (flag){
            // main launched by opening activity
            mydb.deleteAllInactiveData(user);
        }
        else {
            // main launched by login activity
            // search for this user in database
            boolean bool = mydb.findUser(user);
            if (bool){
                // user found in db, update its status
                mydb.makeUserActive(user);
            }
            else{
                // new user, insert record in database
                mydb.insertIntoConnected(user);
            }
        }
        // obtaining toolbar of the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // obtaining drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        // setting a drawer listener
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // obtaining drawer's navigation view
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // registering a receiver responsible for sending pending jobs,
        // periodic and agent termination requests that were inserted into
        // SQLite database due to no internet connection at a given time
        this.registerReceiver(this.mConnReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        // getting the header of the drawer's navigation view
        View header = navigationView.getHeaderView(0);
        // obtaining logged in user's textview and updating its text
        TextView username = (TextView) header.findViewById(R.id.logged_in_user);
        if (username != null) username.setText(name);
        // same for user's device
        TextView device = (TextView) header.findViewById(R.id.device_name);
        device.setText(Build.DEVICE);
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.flContent, new WelcomeFragment());
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        // if drawer is open, just close the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        // otherwise, get the currently displayed fragment
        Fragment f = fm.findFragmentById(R.id.flContent);
        if (f instanceof ViewResultsFragment){
            // displayed fragment is ViewResultsFragment
            // pop ResultsFragment from the stack
            fm.popBackStack();
        }
        else if (f instanceof WelcomeFragment){
            // displayed fragment is WelcomeFragment
            if (back_pressed + time_to_exit > System.currentTimeMillis()){
                // finish activity if button is pressed twice
                // during last time_to_exit seconds
                this.finish();
            }
            else{
                // otherwise, inform the user
                Toast.makeText(getBaseContext(), R.string.press_to_exit, Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }
        // another fragment is currently displayed
        else{
            // clear previously checked drawer option
            navigationView.getMenu().findItem(selected).setChecked(false);
            // and load WelcomeFragment again
            fm.beginTransaction().replace(R.id.flContent, new WelcomeFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks.
        int id = item.getItemId();

        // if logout was selected from options menu..
        if (id == R.id.action_logout) {
            CheckConnection cc = CheckConnection.getInstance(this);
            if (cc.isNetworkAvailable()) {
                // and internet connection is available
                findViewById(R.id.flContent).setVisibility(View.GONE);
                // send a logout request to AM
                new LogoutRequest(R.id.logout_progress, this).execute(name);
            }
            else{
                // no internet connection right now, inform user
                Toast toast = Toast.makeText(this, R.string.no_internet_later, Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        CheckConnection cc = CheckConnection.getInstance(this);
        boolean flag;
        // if no connection is available now
        if (!cc.isNetworkAvailable()){
            Toast toast;
            // show proper message to user
            if (id == R.id.logout) toast = Toast.makeText(this, R.string.no_internet_later, Toast.LENGTH_SHORT);
            else toast = Toast.makeText(this, R.string.no_internet_option, Toast.LENGTH_SHORT);
            toast.show();
            // checked item on drawer isn't changed
            flag = false;
        }
        else{
            // checked item on drawer will be changed
            flag = true;
            Fragment fragment = null;
            FragmentTransaction ft;

            // obtain the indeterminate horizontal progress bar of main activity
            ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
            Class fragmentClass = null;
            // obtain a class reference to the fragment that will be displayed
            // if server is not down
            if (id == R.id.see_status) fragmentClass = AgentsFragment.class;
            else if (id == R.id.stop_agent) fragmentClass = StopAgentFragment.class;
            else if (id == R.id.see_results) fragmentClass = ResultsFragment.class;
            else if (id == R.id.stop_periodic) fragmentClass = StopPeriodicFragment.class;
            else if(id == R.id.create_job) fragmentClass = SendJobsFragment.class;
            else {
                // logout option was selected
                // hide whatever fragment is currently displayed and show
                // a circular indeterminate progress bar
                // while logout request is under process
                findViewById(R.id.flContent).setVisibility(View.GONE);
                new LogoutRequest(R.id.logout_progress, this).execute(name);
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
                    new GetPeriodicsRequest(pb, ft, this).execute();
                }
                else{
                    // .. with the appropriate arguments
                    new AgentsRequest(pb, ft, this, fragment instanceof SendJobsFragment).execute();
                }
            }
        }

        // either way, close the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        // if checked item will be changed, update selected with its id
        if (flag){
            selected = id;
            navigationView.getMenu().findItem(selected).setChecked(true);
        }
        return flag;
    }

    /**
     *  Called by <code>ResultRequest</code> upon receiving specific number of
     *  results to replace <code>ResultsFragment</code> with <code>ViewResultsFragment</code>.
     *
     */
    @Override
    public void onChangeFragment() {
        Fragment fragment = null;
        Class fragmentClass;
        fragmentClass = ViewResultsFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = fm.beginTransaction().replace(R.id.flContent, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     *  Sets the action bar title to the given one.
     *  @param  s   title to display on the action bar.
     */
    public void setActionBarTitle(String s){
        getSupportActionBar().setTitle(s);
    }

    /**
     *  Destroys the <code>MainActivity</code>, unregistering the receiver
     *  used for handling re-established internet connection and sending
     *  pending jobs and agents/job termination requests.
     */
    @Override
    protected void onDestroy() {
        unregisterReceiver(mConnReceiver);
        destroyed = true;
        super.onDestroy();
    }

    /**
     *
     * @return true if <code>MainActivity</code>'s onDestroy() method has been called.
      */
    public boolean getDestroyed(){
        return destroyed;
    }

    /**
     *
     *  @return the name of the connected user.
     */
    public String getName() {
        return name;
    }

    /**
     *  <code>BroadcastReceiver</code> is used for sending jobs, agent termination and
     *  periodic job termination requests that were given by user at a moment when
     *  no internet connection was available.
     *  Reads the underlying SQLite database for each case and if there are existing records
     *  executes the appropriate <code>AsyncTask</code>.
     *
     */
    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

            // an internet connection is available again
            if(currentNetworkInfo.isConnected()){
                // query database for unsent jobs
                String tosend = mydb.fetchJob(user);
                if (!tosend.isEmpty()){
                    // execute async task if there are any
                    new SendRemainingJobsRequest(getBaseContext(),mydb).execute(tosend);
                }
                // query database for unsent agent termination requests
                tosend = mydb.fetchKillAgent(user);
                if (!tosend.isEmpty()){
                    // execute async task if there are any
                    new RemainingAgentsRequest(getBaseContext(),mydb,(Button)findViewById(R.id.stop_agent_button)).execute(tosend);
                }
                // query database for unsent periodic job termination requests
                tosend = mydb.fetchStopPeriodic(user);
                if (!tosend.isEmpty()){
                    // execute async task if there are any
                    new RemainingPeriodicsRequest(getBaseContext(),mydb, (Button) findViewById(R.id.stop_periodic_button)).execute(tosend);
                }

            }
        }
    };

    /**
     *  Sets the navigation drawer's selected-checked item id to the given value.
     *  @param  id  id of the selected item.
     */
    public void setSelected(int id){
        this.selected = id;
    }


}
