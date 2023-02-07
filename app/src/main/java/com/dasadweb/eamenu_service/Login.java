package com.dasadweb.eamenu_service;

import android.util.Log;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.protocol.BasicHttpContext;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Login implements Runnable {
    private volatile boolean login;
    private volatile String username;
    private volatile String password;

    @Override
    public void run() {
        Log.println(Log.DEBUG, "Login", username + " " + password);
        // make login request
        HttpClient client = HttpClientBuilder.create().build();
        HttpContext httpContext = new BasicHttpContext();
        HttpPost post = new HttpPost("https://www.easistent.com/p/ajax_prijava");
        // create parameters
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("uporabnik", username));
        parameters.add(new BasicNameValuePair("geslo", password));

        post.setEntity(new UrlEncodedFormEntity(parameters));
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) client.execute(post, httpContext);
            // check response' status code (return true if 200)
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                login = false;
                return;
            }
            String retSrc = EntityUtils.toString(entity);
            // parsing JSON
            JSONObject result = new JSONObject(retSrc);
            String status = result.getString("status");
            // false if status == ""
            if (status.trim().isEmpty()) {
                login = false;
                return;
            }
            // true if status == "ok"
            login = true;
        } catch (
                IOException e) {
            e.printStackTrace();
            login = false;
        } catch (JSONException e) {
            e.printStackTrace();
            login = false;
        } catch (ParseException e) {
            e.printStackTrace();
            login = false;
        }
    }

    public boolean getLogin() {
        return login;
    }

    public void setParams(String usernameParam, String passwordParam) {
        username = usernameParam;
        password = passwordParam;
    }
}
