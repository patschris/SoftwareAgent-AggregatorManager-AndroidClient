package com.k23b.team13.project3.ac.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.CheckConnection;
import com.k23b.team13.project3.ac.DBHelper;
import com.k23b.team13.project3.ac.services.LoginRequest;

/** <code>LoginActivity</code> is either started after <code>OpeningActivity</code> if
 * `no logged in user was found in the underlying SQLite database or after the successful logout
 *  of a connected user. It contains two <code>EditText</code> fields where user inserts
 *  its username and password, <code>Button</code>s for clearing the text fields and sending
 *  a <code>LoginRequest</code> and a <code>Button</code> that starts <code>RegisterActivity</code>
 *  if the user hasn't created an account yet.
 *
 *  @author C. Patsouras I. Venieris
 *  @version 1
 *
 */
public class LoginActivity extends AppCompatActivity {

    /**
     *  field used for the inserted username.
     */
    private EditText username;

    /**
     *  field used for the inserted password.
     */
    private EditText password;

    /**
     *  the <code>View</code> containing an indeterminate <code>ProgressBar</code> and
     *  a <code>TextView</code> while <code>LoginRequest</code> is processed.
     */
    private View mProgressView;

    /**
     *  the <code>View</code> containing the <code>EditText</code>s, <code>Button</code>s
     *  and <code>TextView</code> shown while no <code>LoginRequest</code> is processed.
     */
    private View mLoginRegisterView;

    /**
     *  <code>Button</code> pressed when user wish to login.
     */
    private Button login;


    /** Creates a <code>LoginActivity</code> setting its content view components.
     *  The activity is started either by (launcher) <code>OpeningActivity</code>
     *  due to no connected user found in the underlying SQLite database
     *  or after the successful logout of a connected user.
     *
     *  @param savedInstanceState   If the activity is being re-initialized after
     *                              previously being shut down then this Bundle
     *                              contains the most recently supplied data.
     *                              Otherwise is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // obtain a flag describing if the activity was started
        // by OpeningActivity (launcher) or after a successful logout
        Bundle b = getIntent().getExtras();
        boolean flag = b.getBoolean("flag");
        DBHelper mydb = new DBHelper(this);
        final LoginActivity la = this;
        if (flag){
            // if flag is true activity was started by launcher
            // so no connected user was found and SQLite database
            // gets cleared
            mydb.onDestroy();
        }
        setContentView(R.layout.activity_login);
        mProgressView = findViewById(R.id.login_progress);
        mLoginRegisterView = findViewById(R.id.login_username_form);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        login = (Button) findViewById(R.id.signin);
        Button clear = (Button) findViewById(R.id.clear);
        Button register = (Button) findViewById(R.id.register_button);


        // setting a click listener for login button
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // clearing previous errors on each field
                // and focus.
                username.setError(null);
                password.setError(null);
                View focusView = null;
                username.clearFocus();
                password.clearFocus();
                // hiding keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                // checking validity of input fields
                // setting error to the invalid field
                // and focus on this one.
                if(TextUtils.isEmpty(username.getText())){
                    // username is a required field
                    if (!TextUtils.isEmpty(password.getText())) password.setText("");
                    username.setError(getString(R.string.error_field_required));
                    focusView = username;
                }
                else if (TextUtils.isEmpty(password.getText())) {
                    // password is a required field
                    password.setError(getString(R.string.error_field_required));
                    focusView = password;

                }
                else if(username.getText().length() < 4){
                    // username should be 4 characters minimum
                    username.setText("");
                    username.setError(getString(R.string.username_minimum));
                    focusView = username;
                }
                else if (password.getText().length() < 4) {
                    // password should be 4 characters minimum
                    if (!TextUtils.isEmpty(username.getText())) username.setText("");
                    password.setText("");
                    password.setError(getString(R.string.error_invalid_password));
                    focusView = password;
                }
                else if (!username.getText().toString().matches(".*[A-Za-z].*")){
                    // username must contains at least one alphabetic character
                    username.setText("");
                    username.setError(getString(R.string.requires_character));
                    focusView = username;
                }
                else if (!password.getText().toString().matches(".*[A-Za-z].*")){
                    // password must contains at least one alphabetic character
                    password.setText("");
                    password.setError(getString(R.string.requires_character));
                    focusView = password;
                }
                else {
                    // both input fields are valid
                    CheckConnection cc = CheckConnection.getInstance(getBaseContext());
                    if (cc.isNetworkAvailable()) {
                        // if there's an available connection at the moment
                        // create a LoginRequest,
                        LoginRequest lr = new LoginRequest(la);
                        // hide the login form and show the progress bar
                        showProgress(true);
                        // send LoginRequest
                        lr.execute(username.getText().toString(), password.getText().toString());

                    } else {
                        // no internet connection available right now, show a toast
                        Toast toast = Toast.makeText(getBaseContext(), R.string.no_internet_later, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                if (focusView != null){
                    // focus on the invalid field
                    focusView.requestFocus();
                }
            }
        });

        // setting a click listener for clear button
        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                username.setText("");
                password.setText("");
                username.requestFocus();
            }
        });

        // setting a click listener for register button
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if register button gets pressed
                // RegisterActivity is started
                Intent i = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(i);
            }
        });

        // setting a touch listener for username EditText
        username.setOnTouchListener(new EditText.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // if username field is touched clear errors
                    // of username and password fields
                    clearErrors();
                }
                return false;
            }
        });

        // setting a textchanged listener for username EditText
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // if text in username EditText field is changed
                // clear errors of username and password fields
                clearErrors();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // setting a touch listener for password EditText
        password.setOnTouchListener(new EditText.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // if password field is touched clear errors
                // of username and password fields
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    clearErrors();
                }
                return false;
            }
        });

        // setting a textchanged listener for password EditText
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // if text in password EditText field is changed
                // clear errors of username and password fields
                clearErrors();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // setting a listener for send soft key
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // if send soft key is pressed perform
                // a login button click
                if(actionId == EditorInfo.IME_ACTION_SEND){
                    login.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Shows the progress UI and hides the login form or vice versa.
     *
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (!show)
        {   // focus on the field that had an error after the
            // LoginRequest
            if (username.getError() != null)
                username.requestFocus();
            if (password.getError() != null)
                password.requestFocus();

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginRegisterView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     *
     * @return the EditText used for username.
     */
    public EditText getUsernameView(){
        return this.username;
    }

    /**
     *
     *  @return the EditText used for password.
     */
    public EditText getPasswordView(){ return this.password;}

    private void clearErrors(){
        if (password.getError() != null) {
            password.setError(null);
        }
        if (username.getError() != null) {
            username.setError(null);
        }
    }
}
