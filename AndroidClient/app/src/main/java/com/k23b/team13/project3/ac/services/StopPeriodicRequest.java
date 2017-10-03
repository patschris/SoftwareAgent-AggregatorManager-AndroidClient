package com.k23b.team13.project3.ac.services;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.structures.PeriodicJobList;
import com.k23b.team13.project3.ac.structures.PeriodicsAdapter;

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
 *  Web service to stop the execution of a periodic job.
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
public class StopPeriodicRequest extends AsyncTask<String, Void, Integer> {
    /**
     * Shows progress while async task isn't over yet.
     */
    private ProgressBar p = null;
    /**
     * In order to access to resources.
     */
    private Context ct = null;
    /**
     * List's contents.
     */
    private PeriodicsAdapter pa = null;
    /**
     * Agent's identifier whose job I want to stop.
     */
    private String hashkey;
    /**
     * Job's id I want to stop.
     */
    private String id;
    /**
     * This Job's position in job's list.
     */
    private int position;
    /**
     * Hide stop button if there is no job inside the list after the execution of the service.
     */
    private Button stop;
    /**
     * Constructor of <code>StopPeriodicRequest</code> class.
     * @param p Shows progress while async task isn't over yet.
     * @param ct In order to access to resources.
     * @param position This Job's position in job's list.
     * @param stop Hide stop button if there is no job inside the list after the execution of the service.
     */
    public StopPeriodicRequest(ProgressBar p, Context ct,int position, Button stop){
        this.p = p;
        this.ct = ct;
        this.position = position;
        this.stop = stop;
    }
    @Override
    protected void onPreExecute() { /* on pre execute */
        super.onPreExecute();
        p.setVisibility(View.VISIBLE); /* show progress bar */
    }

    @Override
    protected Integer doInBackground(String... params) { /* while task is executing */
        /* set url of AM's service */
        final String url = ct.getResources().getString(R.string.base_url) + "stopperiodic/"+params[0]+"/"+params[1];
        /* initialize a new rest web service */
        RestTemplate restTemplate = new RestTemplate(true);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        id = params[0];
        hashkey = params[1];

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpStatus st = null;

        HttpEntity<String> he = new HttpEntity<String>(headers);
        ResponseEntity<String> res = null;
        try { /* and wait for server's response */
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
    protected void onPostExecute(Integer code) { /* after receiving response */
        super.onPostExecute(code);
        p.setVisibility(View.GONE); /* hide progress bar */
        if (code == 200) { /* if periodic job stopped successfully */
            PeriodicJobList pl = PeriodicJobList.getInstance();
            pl.remove(position); /* remove him from list*/
            pa = PeriodicsAdapter.getInstance(ct, pl.getAlist());
            pa.notifyDataSetChanged(); /* update adapter */
            if (pl.getAlist().isEmpty()){ /* if list is empty */
                stop.setVisibility(View.GONE); /* hide stop button */
            }
            Toast toast = Toast.makeText(ct, "Job stopped successfully", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            Toast toast = Toast.makeText(ct, "Server is probably down", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
