package com.k23b.team13.project3.ac.services;

import android.os.AsyncTask;
import android.widget.Toast;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.activities.MainActivity;
import com.k23b.team13.project3.ac.classes.Result;
import com.k23b.team13.project3.ac.structures.ResultList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.k23b.team13.project3.ac.fragments.ResultsFragment.OnResultViewListener;

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
 *  <code>ResultRequest</code> is an <code>AsyncTask</code> performing the web service
 *  that retrieves a specific number of <code>Result</code>s for a specific
 *  <code>SoftwareAgent</code> or all of them.
 *  It starts its execution once the user clicks "see results" button in
 *  <code>ResultsFragment</code>, if internet connection is available at the moment.
 *  When completing, <code>ViewResultsFragment</code> will be displayed with the
 *  obtained data.
 *
 *  @author C. Patsouras I. Venieris
 *  @version 1
 */
public class ResultRequest extends AsyncTask<String, Void, Integer> {

    /**
     * In order to access to resources and safely commit fragment transaction.
     */
    private MainActivity ma;

    /**
     * Interface with implemented method in <code>MainActivity</code> called to
     * commit transaction between <code>ResultsFragment</code> and
     * <code>ViewResultsFragment</code>.
     *
     */
    private OnResultViewListener vr;

    /**
     * Constructs a <code>ResultRequest</code>.
     * @param ma the <code>MainActivity<code> that invoked this task.
     * @param rview interface used for commiting a fragment transaction.
     */
    public ResultRequest(MainActivity ma, OnResultViewListener rview){
        this.ma = ma;
        this.vr = rview;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(String... params) { /* while task is executing */
        ResultList rl = ResultList.getInstance();
        /* set url of AM's service */
        final String url = ma.getResources().getString(R.string.base_url) + "result/"+params[0]+"/"+params[1];
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
                Gson g = new Gson();
                ArrayList <Result> aa = null;
                InputStream is = new ByteArrayInputStream(res.getBody().getBytes());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                aa = g.fromJson(br, new TypeToken<ArrayList<Result>>(){}.getType());
                rl.setAlist(aa);
                rl.setHashkey(params[0]);
                rl.setNum(params[1]);
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
        Toast toast;
        if (code == 200) { /* if request was successful */
            // if MainActivity isn't destroyed, call
            // interface's onChangeFragment method to commit a fragment transaction
            if (!ma.getDestroyed()) vr.onChangeFragment();
        }
        else if (code == 500){ // request failed
            toast = Toast.makeText(ma, R.string.server_error, Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            // server is probably down case
            toast = Toast.makeText(ma,R.string.server_is_down,Toast.LENGTH_SHORT);
            toast.show();

        }
    }
}
