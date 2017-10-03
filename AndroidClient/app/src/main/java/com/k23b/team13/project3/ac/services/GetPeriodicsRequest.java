package com.k23b.team13.project3.ac.services;

import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.activities.MainActivity;
import com.k23b.team13.project3.ac.classes.Job;
import com.k23b.team13.project3.ac.structures.PeriodicJobList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *  Web service to take periodic jobs from <code>Aggregator Manager</code>.
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
public class GetPeriodicsRequest extends AsyncTask<Void, Void, Integer> {

    /**
     * Shows progress while async task isn't over yet.
     */
    private ProgressBar p = null;

    /**
     * In order to change fragment after async task is done.
     */
    private FragmentTransaction ft = null;

    /**
     * In order to access to resources and safely commit fragment transaction.
     */
    private MainActivity ma;

    /**
     * Constructor of <code>GetPeriodicsRequest</code> class.
     * @param p Shows progress while async task isn't over yet.
     * @param ft In order to change fragment after async task is done.
     * @param ma In order to access to resources and safely commit fragment transaction.
     */
    public GetPeriodicsRequest(ProgressBar p, FragmentTransaction ft, MainActivity ma){
        this.p = p;
        this.ft = ft;
        this.ma = ma;
    }

    @Override
    protected void onPreExecute() { /* on pre execute */
        super.onPreExecute();
        p.setVisibility(View.VISIBLE); /* show progress bar */
    }

    @Override
    protected Integer doInBackground(Void... params) { /* while task is executing */
        PeriodicJobList pjl = PeriodicJobList.getInstance();
        final String url = ma.getResources().getString(R.string.base_url) + "returnperiodic"; /* set url of AM's service */
        /* initialize a new rest web service */
        RestTemplate restTemplate = new RestTemplate(true);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpStatus st = null;

        HttpEntity<String> he = new HttpEntity<String>(headers);
        ResponseEntity<String> res = null;
        try { /* and wait for server's response */
            res = restTemplate.exchange(url, HttpMethod.GET, he, String.class);
            st = res.getStatusCode();
            if (!res.getBody().isEmpty()) {
                Gson g = new Gson(); /* convert json array to class's Job objects */
                ArrayList<Job> aa = null;
                InputStream is = new ByteArrayInputStream(res.getBody().getBytes());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                aa = g.fromJson(br, new TypeToken<ArrayList<Job>>(){}.getType());
                pjl.setAlist(aa); /* update the list which stores periodic jobs */
            }
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
        super.onPostExecute(code); /* after receiving response */
        p.setVisibility(View.GONE); /* we don't need progress bar any more  */
        if (code == 200) { /* if agents returned successfully */
            if (!ma.getDestroyed())ft.commit(); /* change fragment if MainActivity is not destroyed */
        }
        else { /* if there was an error on this web service */
            Toast toast = Toast.makeText(ma.getBaseContext(), R.string.server_is_down, Toast.LENGTH_SHORT);
            toast.show(); /* show a message */

        }
    }
}
