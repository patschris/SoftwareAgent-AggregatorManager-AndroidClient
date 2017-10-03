package com.k23b.team13.project3.ac.services;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.classes.Agents;
import com.k23b.team13.project3.ac.structures.AgentsAdapter;
import com.k23b.team13.project3.ac.structures.AgentsList;

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
 *  Web service to stop the execution of a  <code>Software Agent</code>.
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
public class KillAgentRequest extends AsyncTask<Void, Void, Integer> {
    /**
     * In order to access to resources.
     */
    private Context ct = null;
    /**
     * Describing the client will be stopped.
     */
    private int hashkey;
    /**
     * List's contents.
     */
    private AgentsAdapter aa;
    /**
     * Hide stop button if there is no agent inside the list after the execution of the service.
     */
    private Button stop;

    /**
     * Constructor of <code>KillAgent</code> class.
     * @param ct In order to access to resources.
     * @param adp Describing the client will be stopped.
     * @param hashkey List's contents.
     * @param stop Button to stop an agent.
     */
    public KillAgentRequest(Context ct, AgentsAdapter adp, int hashkey, Button stop){
        this.ct = ct;
        this.aa = adp;
        this.hashkey = hashkey;
        this.stop = stop;
    }

    @Override
    protected void onPreExecute() { /* on pre execute */
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... params) { /* while task is executing */
        final String url = ct.getResources().getString(R.string.base_url) + "killagent/"+hashkey; /* set url of AM's service */
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
        if (code == 200) { /* if agent stopped successfully */
            AgentsList al = AgentsList.getInstance();
            Agents a = al.getAgentbyHashkey(hashkey);
            al.pop(a); /* remove him from the list */
            aa.notifyDataSetChanged(); /* an*/
            if (al.getAlist().isEmpty()){ /* if there is no agent to stop */
                stop.setVisibility(View.GONE); /* hide stop button */
            }
        }
        else { /* if there was an error on this web service */
            Toast toast = Toast.makeText(ct, R.string.server_is_down, Toast.LENGTH_SHORT);
            toast.show(); /* show a message */
        }
    }
}
