package com.k23b.team13.project3.ac.services;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.activities.LoginActivity;
import com.k23b.team13.project3.ac.activities.MainActivity;
import com.k23b.team13.project3.ac.classes.LoginInfo;
import com.k23b.team13.project3.ac.classes.SecureLogin;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 *  Login web service for <code>Android Clients</code>.
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
    public class LoginRequest extends AsyncTask<String, Void, Integer> {
    /**
     * In order to stop login activity.
     */
    private LoginActivity la;
    /**
     * User's name of client who wants to login.
     */
    private String name;
    /**
     * Constructor of <code>LoginRequest</code> class.
     * @param la In order to stop login activity.
     */
    public LoginRequest(LoginActivity la){
        this.la = la;
    }

    @Override
    protected void onPreExecute() { /* on pre execute */
        super.onPreExecute();
    }

    @Override
    public Integer doInBackground(String... params) { /* while task is executing */
        SecureLogin sc = SecureLogin.getInstance();
        this.name = params[0];/* username was given as parameter */
        String password = sc.sha256(params[1]); /* encrypt password - was given as a parameter */
        LoginInfo li = new LoginInfo(this.name, password);
        ObjectMapper mapper = new ObjectMapper();
        String tosend = null;
        try { /* convert login credentials into JSON Object */
            tosend = mapper.writeValueAsString(li);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        final String url = la.getResources().getString(R.string.base_url) + "login"; /* set url of AM's service */
        //if (params[0].equals("admin") && params[1].equals("admin")) return 200;
        /* initialize a new rest web service */
        RestTemplate restTemplate = new RestTemplate(true);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus st;

        HttpEntity<String> entity = new HttpEntity<String>(tosend,headers);
        ResponseEntity<String> res = null;
        try { /* and wait for server's response */
            res = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            st = res.getStatusCode();
        } catch (HttpClientErrorException e){
            st = e.getStatusCode();
        }
        catch (ResourceAccessException re) {
            re.printStackTrace();
            return -1;
        }
        return st.value();
    }

    @Override
    protected void onPostExecute(Integer code) { /* after receiving response */
        super.onPostExecute(code);
        EditText username = la.getUsernameView();
        EditText password = la.getPasswordView();
        username.clearFocus();
        password.clearFocus();
        Toast toast;
        if (code == 200) { /* if user logge in successfully */
            toast = Toast.makeText(la, R.string.logged_in_success,Toast.LENGTH_SHORT);
            toast.show(); /* show proper message*/
            Bundle b = new Bundle();
            /*
                pass as extras username of logged in user and
                a flag with false value in order to know that before main activity was login activity
             */
            b.putString("username", name);
            b.putBoolean("flag", false);
            Intent i = new Intent(la.getBaseContext(), MainActivity.class);
            i.putExtras(b);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            la.getBaseContext().startActivity(i); /* start main acivity */
            la.finish(); /* and stop login activity */
        } else if (code == 401) { /* if username didn't match t oanyone stored in database */
            // first clean inserted text
            username.setText("");
            password.setText("");
            toast = Toast.makeText(la, R.string.create_account_first, Toast.LENGTH_SHORT);
            toast.show();
            la.showProgress(false);

        } else if (code == 403) { /* if request rejected from the server */
            toast = Toast.makeText(la, R.string.login_rejected, Toast.LENGTH_SHORT);
            toast.show();
            la.showProgress(false);
        }
        else if (code == 400){ /* if password was incorrect */
            password.setText("");
            toast = Toast.makeText(la, R.string.wrong_password, Toast.LENGTH_SHORT);
            toast.show();
            la.showProgress(false);
        }
        else if (code == 409){ /* if user is already logged in */
            username.setText("");
            password.setText("");
            toast = Toast.makeText(la, R.string.already_loggedin, Toast.LENGTH_SHORT);
            toast.show();
            la.showProgress(false);
        }
        else {
            la.showProgress(false);
            toast = Toast.makeText(la,R.string.server_is_down,Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
