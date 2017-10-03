package com.k23b.team13.project3.ac.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.k23b.team13.project3.ac.services.RegisterRequest;

/**
 *  <code>RegisterActivity</code> is started when a user who wishes to create a new account
 *  to AM presses the corresponding <code>Button<code> in <code>LoginActivity</code>.
 *  It's composed by 3 <code>EditText</code> fields and a Button.
 *  If a new account was sucessfully created <code>RegisterActivity</code> is finished and
 *  a new <code>LoginActivity</code> is created.
 *
 */
public class RegisterActivity extends AppCompatActivity {

    /**
     *  field used for the inserted username.
     */
    private EditText username;

    /**
     *  field used for the inserted password.
     */
    private EditText password;

    /**
     *  field used for checking retyping the desired password.
     */
    private EditText verification;

    /**
     *  the <code>View</code> containing an indeterminate <code>ProgressBar</code> and
     *  a <code>TextView</code> while <code>RegisterRequest</code> is processed.
     */
    private View registerProgress;

    /**
     *  the <code>View</code> containing the <code>EditText</code>s and <code>Button</code>
     *  shown while no <code>RegisterRequest</code> is processed.
     */
    private View registerForm;

    /**
     * used for checking existance of internet connection at
     * a specific time.
     */
    private CheckConnection cc = null;

    /**
     * button used for sending a <code>RegisterRequest</code>.
     */
    private Button register;

    /**
     *
     * @return a string containing the text input of username edittext.
     */
    public String getUsername() {
        return username.getText().toString();
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
     * @return the EditText used for verification.
     */
    public EditText getVerification() {
        return verification;
    }

    /**
     *
     * @return the EditText used for password.
     */
    public EditText getPassword() {
        return password;
    }

    /**
     *  Creates a <code>RegisterActivity</code>, setting its content view components and listeners
     *  on them.
     *  <code>RegisterActivity</code> is started when a user who wishes to create a new account
     *  to AM presses the corresponding <code>Button<code> in <code>LoginActivity</code>.
     *
     *  @param savedInstanceState   If the activity is being re-initialized after
     *                              previously being shut down then this Bundle
     *                              contains the most recently supplied data.
     *                              Otherwise is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting activity's content view
        setContentView(R.layout.activity_register);
        final RegisterActivity ra = this;
        // obtaining view, edittexts and button
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        verification = (EditText) findViewById(R.id.verify_password);
        register = (Button) findViewById(R.id.register_button);
        registerProgress = findViewById(R.id.register_progress);
        registerForm = findViewById(R.id.register_form);
        // getting instance for CheckConnection class
        cc = CheckConnection.getInstance(this);

        // setting a click listener for register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View focusView = null;
                // hiding keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                // checking validity of input fields
                // setting error to the invalid field
                // and focus on this one.
                if (TextUtils.isEmpty(username.getText())) {
                    // username is a required field
                    if (!TextUtils.isEmpty(password.getText())) password.setText("");
                    if (!TextUtils.isEmpty(verification.getText())) verification.setText("");
                    username.setError(getString(R.string.error_field_required));
                    focusView = username;
                }
                else if (TextUtils.isEmpty(password.getText())) {
                    // password is a required field
                    if (!TextUtils.isEmpty(verification.getText())) verification.setText("");
                    password.setError(getString(R.string.error_field_required));
                    focusView = password;
                }
                else if (username.getText().length() < 4){
                    // username should be 4 characters minimum
                    username.setText("");
                    username.setError(getString(R.string.username_minimum));
                    focusView = username;
                }
                else if (password.getText().length() < 4) {
                    // password should be 4 characters minimum
                    if (!TextUtils.isEmpty(password.getText())) password.setText("");
                    if (!TextUtils.isEmpty(verification.getText())) verification.setText("");
                    password.setError(getString(R.string.error_invalid_password));
                    focusView = password;

                }
                else if (TextUtils.isEmpty(verification.getText())){
                    // verification is a required field
                    verification.setError(getString(R.string.error_field_required));
                    focusView = verification;
                }
                else if (!password.getText().toString().equals(verification.getText().toString()) ) {
                    // password given should match verification
                    if (!TextUtils.isEmpty(password.getText())) password.setText("");
                    if (!TextUtils.isEmpty(verification.getText())) verification.setText("");
                    verification.setError(getString(R.string.error_verification));
                    focusView = verification;
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
                    verification.setText("");
                    password.setError(getString(R.string.requires_character));
                    focusView = password;
                }
                else{
                    // all input fields are valid
                    if (cc.isNetworkAvailable()) {
                        // if there's an available connection at the moment
                        // create a RegisterRequest
                        RegisterRequest rr = new RegisterRequest(ra);
                        // hide the register form and show the progress bar
                        showProgress(true);
                        // send LoginRequest
                        rr.execute(username.getText().toString(),verification.getText().toString());
                    } else{
                        // no internet connection available right now, show a toast
                        Toast toast = Toast.makeText(getBaseContext(), R.string.no_internet,Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                // focus on the invalid field
                if (focusView != null) focusView.requestFocus();
            }
        });

        // setting a touch listener for username EditText
        username.setOnTouchListener(new EditText.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // if username field is touched clear errors of all fields
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
                // clear errors of all fields
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
                // of all fields
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
                // clear errors of all fields
                clearErrors();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // setting a touch listener for verification EditText
        verification.setOnTouchListener(new EditText.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // if verification field was touched clear errors
                    // of all fields.
                    clearErrors();
                }
                return false;
            }
        });

        // setting a listener for send soft key
        verification.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    // if send soft key is pressed perform
                    // a register button click
                    register.performClick();
                    return true;
                }
                return false;
            }
        });

        // setting a textchanged listener for verification EditText
        verification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // if text in verification EditText field is changed
                // clear errors of all fields
                clearErrors();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * Clearing errors of all the <code>EditText</code> if there are any.
     */
    private void clearErrors(){
        if (verification.getError() != null ) {
            verification.setError(null);
        }
        if (password.getError() != null) {
            password.setError(null);
        }
        if (username.getError() != null) {
            username.setError(null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        if (!show && username.getError() != null) username.requestFocus();
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            registerForm.setVisibility(show ? View.GONE : View.VISIBLE);
            registerForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    registerForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            registerProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            registerProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    registerProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            registerProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            registerForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}