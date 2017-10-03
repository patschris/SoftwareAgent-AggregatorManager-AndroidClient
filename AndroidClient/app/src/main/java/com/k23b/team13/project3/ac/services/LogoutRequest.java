package com.k23b.team13.project3.ac.services;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.DBHelper;
import com.k23b.team13.project3.ac.activities.LoginActivity;
import com.k23b.team13.project3.ac.activities.MainActivity;
import com.k23b.team13.project3.ac.classes.Users;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 *  Logout web service for <code>Android Clients</code>.
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
public class LogoutRequest extends AsyncTask<String, Void, Integer> {
    /**
     * In order to stop Main activity.
     */
    private MainActivity act;
    /**
     * Progress bar's id.
     */
    private int id;
    /**
     * User who wants to logout.
     */
    private String name;

    /**
     * Constructor of <code>LogoutRequest</code> class.
     * @param id Progress bar's id.
     * @param act In order to stop Main activity.
     */
    public LogoutRequest(int id, MainActivity act) {
        this.id = id;
        this.act = act;
    }

    @Override
    protected void onPreExecute() { /* on pre excecute */
        super.onPreExecute();
        act.findViewById(id).setVisibility(View.VISIBLE); /* show progress bar */
    }

    @Override
    protected Integer doInBackground(String... params) { /* while task is executing */
        /* initialize a new rest web service */
        RestTemplate restTemplate = new RestTemplate(true);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        this.name = params[0]; /* User who wants to logout */
        String url = act.getResources().getString(R.string.base_url) + "logout/" + params[0];
        //if (params[0].equals("admin")) return 200;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpStatus st = null;
        HttpEntity<String> he = new HttpEntity<String>(headers);
        ResponseEntity<String> res = null;
        try { /* wait for server's response */
            res = restTemplate.exchange(url, HttpMethod.GET, he, String.class);
            st = res.getStatusCode();
        } catch (HttpClientErrorException e){
            st = e.getStatusCode();
        }
        catch (ResourceAccessException re){
            return -1;
        }
        return st.value();
    }

    @Override
    protected void onPostExecute(Integer code) {
        act.findViewById(id).setVisibility(View.GONE); /* hide progress bar */
        if (code == 200){ /* if user logged out successfully */
            DBHelper mydb = new DBHelper(act.getBaseContext());
            Users u = new Users(name, false);
            mydb.logoutConnected(u); /* update sqlite database */
            Bundle b = new Bundle();
            b.putBoolean("flag", false); /* in order to know that login activity opened after logout */
            Intent i = new Intent(act.getBaseContext(), LoginActivity.class);
            i.putExtras(b);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            act.getBaseContext().startActivity(i); /* Start login activity */
            act.finish(); /* Finish main activity */
        }
        else { /* if user couldn't log out*/
            Toast toast = Toast.makeText(act.getBaseContext(), R.string.server_is_down, Toast.LENGTH_SHORT);
            toast.show(); /* show proper message*/
            act.findViewById(R.id.flContent).setVisibility(View.VISIBLE);

        }
    }
}
