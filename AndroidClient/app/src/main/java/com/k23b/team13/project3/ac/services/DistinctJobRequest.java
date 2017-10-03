package com.k23b.team13.project3.ac.services;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ProgressBar;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.activities.MainActivity;
import com.k23b.team13.project3.ac.classes.DistinctJob;
import com.k23b.team13.project3.ac.structures.DistinctJobList;
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
 *  Web service to take distinct job from <code>Aggregator Manager</code>.
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
public class DistinctJobRequest extends AsyncTask<Void, Void, Integer> {
    /**
     * Shows progress while async task isn't over yet.
     */
    private ProgressBar p = null;
    /**
     * In order to change fragment after async task is done.
     */
    private FragmentTransaction ft = null;
    /**
     * In order to access to resources.
     */
    private Context ct = null;

    /**
     * In order to access to resources and safely commit fragment transaction.
     */
    private MainActivity ma;
    /**
     * Constructor of <code>DistinctJobRequest</code> class.
     * @param p Shows progress while async task isn't over yet.
     * @param ft In order to change fragment after async task is done.
     * @param ma In order to access to resources and safely commit fragment transaction.
     */
    //@param ct In order to access to resources.
    public DistinctJobRequest(ProgressBar p, FragmentTransaction ft,MainActivity ma){
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
    protected Integer doInBackground(Void... params) {
        DistinctJobList al = DistinctJobList.getInstance();
        final String url = ma.getResources().getString(R.string.base_url) + "distinctjobs"; /* set url of AM's service */
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
                Gson g = new Gson(); /* convert json array to class's DistinctJob object */
                ArrayList<DistinctJob> aa = null;
                InputStream is = new ByteArrayInputStream(res.getBody().getBytes());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                aa = g.fromJson(br, new TypeToken<ArrayList<DistinctJob>>(){}.getType());
                /* set the adapter used for autocomplete */
                al.setAlist(aa);
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
    protected void onPostExecute(Integer code) { /* after receiving response */
        super.onPostExecute(code);
        p.setVisibility(View.GONE); /* we don't need progress bar any more  */
        if (code == 200) {
            if (!ma.getDestroyed())ft.commit(); /* change fragment if calling activity isn't destroyed */
        }
    }
}
