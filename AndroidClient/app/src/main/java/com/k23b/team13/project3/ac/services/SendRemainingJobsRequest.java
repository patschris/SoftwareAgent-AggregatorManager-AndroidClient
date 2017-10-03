package com.k23b.team13.project3.ac.services;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.DBHelper;

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
 *  Web service to send jobs remained in sqlite database while there was no internet connections.
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
public class SendRemainingJobsRequest extends AsyncTask <String, Void, Integer> {
    /**
     * In order to access to resources.
     */
    private Context ct = null;
    /**
     * Database used for this app.
     */
    private DBHelper mydb;
    /**
     * Constructor of <code>SendRemainingJobsRequest</code> class.
     * @param ct In order to access to resources.
     * @param mydb Database used for this app.
     */
    public SendRemainingJobsRequest(Context ct, DBHelper mydb) {
        this.ct = ct;
        this.mydb = mydb;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(String... params) { /* while task is executing */
        /* initialize a new rest web service */
        RestTemplate restTemplate = new RestTemplate(true);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus st;
        /* set url of AM's service */
        final String url = ct.getResources().getString(R.string.base_url) + "remainingjobs";

        HttpEntity<String> entity = new HttpEntity<String>(params[0],headers);
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
        if (code == 200) { /*if jobs sent successfully */
            mydb.deleteJob(mydb.getConnectedUser()); /* delete them from database */
            toast = Toast.makeText(ct, R.string.remain_jobs_success, Toast.LENGTH_SHORT);
            toast.show(); /* show proper message */
        }
        else if (code == 500){
            toast = Toast.makeText(ct, R.string.server_error, Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            toast = Toast.makeText(ct, R.string.server_is_down, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
