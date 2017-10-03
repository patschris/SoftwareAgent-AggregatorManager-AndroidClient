package com.k23b.team13.project3.ac.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.DBHelper;
import com.k23b.team13.project3.ac.classes.Users;

/** <code>OpeningActivity</code> is the first activity created once the
 *  application starts executing (launcher activity).
 *  During its lifecycle, it queries the underlying sqlite database
 *  for a connected user.
 *  If a connected user is found, <code>MainActivity</code> is started
 *  otherwise <code>LoginActivity</code>.
 *
 *  @author C. Patsouras I. Venieris
 *  @version 1
 *
 */
public class OpeningActivity extends AppCompatActivity {

    /** Creates an <code>OpeningActivity</code>, setting its
     *  Content View.
     *
     * @param savedInstanceState    If the activity is being re-initialized after
     *                              previously being shut down then this Bundle
     *                              contains the most recently supplied data.
     *                              Otherwise is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("on ", "create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

    }

    @Override
    protected void onResume(){
        super.onResume();
        if (getIntent().getBooleanExtra("EXIT", false)){
            finish();
            moveTaskToBack(true);
            System.exit(0);
        }
        setContentView(R.layout.activity_opening);

        // a thread executes checking the underlying SQLite database for a connected user
        Thread background = new Thread() {
            public void run() {
                try {
                    DBHelper mydb = new DBHelper(getBaseContext());
                    Users u = mydb.getConnectedUser();
                    Intent i;
                    Bundle b = new Bundle();
                    // no connected user found, LoginActivity will be started
                    if (u == null){
                        // put a boolean flag set to true in Bundle b
                        // for LoginActivity
                        b.putBoolean("flag", true);
                        i = new Intent(getBaseContext(), LoginActivity.class);
                        i.putExtras(b);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    // a connected user was found, MainActivity will be started
                    else {
                        // put a boolean flag set to true in Bundle b
                        // for MainActivity
                        b.putString("username", u.getUsername());
                        b.putBoolean("flag", true);
                        i = new Intent(getBaseContext(), MainActivity.class);
                        i.putExtras(b);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    // start the activity (LoginActivity or MainActivity)
                    // and finish this one
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        };
        // start thread
        background.start();
    }

}
