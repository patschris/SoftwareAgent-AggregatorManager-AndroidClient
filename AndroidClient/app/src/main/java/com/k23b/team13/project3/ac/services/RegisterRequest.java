package com.k23b.team13.project3.ac.services;

import android.os.AsyncTask;
import android.widget.Toast;

import com.k23b.team13.project3.R;
import com.k23b.team13.project3.ac.activities.RegisterActivity;
import com.k23b.team13.project3.ac.classes.RegisterInfo;
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
 *  Register web service for <code>Android Clients</code>.
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
public class RegisterRequest extends AsyncTask<String, Void, Integer>{
    /**
     * In order to stop register activity.
     */
    private RegisterActivity ra;

    /**
     * Constructor of <code>RegisterActivity</code> class.
     * @param ra In order to stop register activity.
     */
    public RegisterRequest(RegisterActivity ra){
        this.ra = ra;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(String... params) { /* while task is executing */
        SecureLogin sl = SecureLogin.getInstance();
        String username = params[0]; /* username was given as parameter */
        String password = sl.sha256(params[1]); /* encrypt password - was given as a parameter */
        RegisterInfo ri = new RegisterInfo(username,password);
        ObjectMapper mapper = new ObjectMapper();
        String tosend = null;
        try { /* convert registration credentials into JSON Object */
            tosend = mapper.writeValueAsString(ri);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        final String url = ra.getResources().getString(R.string.base_url) + "register"; /* set url of AM's service */
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
        catch (ResourceAccessException re){
            return -1;
        }
        return st.value();
    }

    @Override
    protected void onPostExecute(Integer code) { /* after receiving response */
        super.onPostExecute(code);
        Toast toast;
        if (code == 200) {  /* if a new account created successfully */
            toast = Toast.makeText(ra, R.string.register_success, Toast.LENGTH_SHORT);
            ra.showProgress(false); /* hide progress bar */
            toast.show();
            ra.finish(); /* stop register avtivity */
        }
        else if (code == 403) { /* if username was already taken */
            ra.getPassword().setText("");
            ra.getVerification().setText("");
            ra.getUsernameView().setText("");
            toast = Toast.makeText(ra, R.string.username_taken, Toast.LENGTH_SHORT);
            toast.show();
            ra.showProgress(false);
        }
        else { /* if server is down */
            toast = Toast.makeText(ra, R.string.server_is_down, Toast.LENGTH_SHORT);
            ra.showProgress(false);
            toast.show();
        }
    }
}
