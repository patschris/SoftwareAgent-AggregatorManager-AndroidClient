package com.k23b.team13.project3.ac.services;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.activities.MainActivity;
import com.k23b.team13.project3.ac.classes.Agents;
import com.k23b.team13.project3.ac.structures.AgentsAdapter;
import com.k23b.team13.project3.ac.structures.AgentsList;
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
 *  Web service to take registered agents from <code>Aggregator Manager</code>.
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
public class AgentsRequest extends AsyncTask<Void, Void, Integer>{
    /**
     * Shows progress while async task isn't over yet.
     */
    private ProgressBar p = null;
    /**
     * In order to change fragment after async task is done.
     */
    private FragmentTransaction ft = null;

    /**
     * List's contents.
     */
    private AgentsAdapter aa = null;
    /**
     * True if this async task is called from "Send Job" option, false otherwise.
     */
    private boolean flag;

    /**
     * In order to access to resources and safely commit fragment transaction.
     */
    private MainActivity ma;

    /**
     * Constructor of <code>AgentsRequest</code> class.
     * @param p Shows progress while async task isn't over yet.
     * @param ft In order to change fragment after async task is done.
     * @param ma In order to access to resources and safely commit fragment transaction.
     * @param flag True if this async task is called from "Send Job" option, false otherwise.
     *
     */
    public AgentsRequest(ProgressBar p, FragmentTransaction ft, MainActivity ma, boolean flag){
        this.p = p;
        this.ft = ft;
        this.ma = ma;
        this.flag=flag;
    }

    @Override
    protected void onPreExecute() { /* on pre execute */
        super.onPreExecute();
        p.setVisibility(View.VISIBLE); /* show progress bar */
        AgentsList al = AgentsList.getInstance();
    }

    @Override
    protected Integer doInBackground(Void... params) { /* while task is executing */
        AgentsList al = AgentsList.getInstance();
        /* set url of AM's service */
        final String url = ma.getResources().getString(R.string.base_url) + "agents";
        /* initialize a new rest web service */
        RestTemplate restTemplate = new RestTemplate(true);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpStatus st = null;

        HttpEntity <String> he = new HttpEntity<String>(headers);
        ResponseEntity<String> res = null;
        try { /* and wait for server's response */
            res = restTemplate.exchange(url, HttpMethod.GET, he, String.class);
            st = res.getStatusCode();
            if (!res.getBody().isEmpty()) {
                Gson g = new Gson(); /* convert json array to class's Agents object */
                InputStream is = new ByteArrayInputStream(res.getBody().getBytes());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                al.updateList((ArrayList) g.fromJson(br, new TypeToken<ArrayList<Agents>>() {
                }.getType())); /* update the list which stores Agents*/
                aa = AgentsAdapter.getInstance(ma.getBaseContext(), al.getAlist()); /* set the adapter that shows agents into AgentsFragment */
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
        p.setVisibility(View.GONE); /* we don't need progress bar any more */
        if (code == 200) { /* if agents returned successfully */
            aa.notifyDataSetChanged(); /* update agents adapter*/
            if (flag){ /* if this async task is called from "Send Job" option */
                new DistinctJobRequest(p,ft,ma).execute(); /* start a async task for distinct jobs */
                return;
            }
            if (!ma.getDestroyed()) ft.commit(); /* change fragment if calling activity isn't destroyed */
        }
        else { /* if there was an error on this web service */
            Toast toast = Toast.makeText(ma.getBaseContext(), R.string.server_is_down, Toast.LENGTH_SHORT);
            toast.show(); /* show a message */
        }
    }
}
