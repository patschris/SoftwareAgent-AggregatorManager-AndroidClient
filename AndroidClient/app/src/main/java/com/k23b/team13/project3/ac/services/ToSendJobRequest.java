package com.k23b.team13.project3.ac.services;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.classes.Job;
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
 *  Web service to send job.
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
public class ToSendJobRequest extends AsyncTask<String, Void, Integer> {
    /**
     * In order to access to resources.
     */
    private Context ct = null;

    /**
     * Constructor of <code>ToSendJobRequest</code> class.
     * @param ct In order to access to resources.
     */
    public ToSendJobRequest(Context ct) {
        this.ct = ct;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    public Integer doInBackground(String... params) { /* while task is executing */
        Job job = new Job(0,params[0],Boolean.parseBoolean(params[1]),Integer.parseInt(params[2]),Integer.parseInt(params[3]),null);
        ObjectMapper mapper = new ObjectMapper();
        String tosend = null;
        try { /* convert job into JSON Object */
            tosend = mapper.writeValueAsString(job);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        final String url = ct.getResources().getString(R.string.base_url) + "job"; /* set url of AM's service */
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
        Toast toast;
        if (code == 200) { /* if job sent successfully */
            toast = Toast.makeText(ct, R.string.job_sent_success, Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (code==500) {
            toast = Toast.makeText(ct, R.string.server_error, Toast.LENGTH_SHORT);
            toast.show();

        }
        else {
            toast = Toast.makeText(ct, R.string.server_is_down, Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
