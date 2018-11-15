package amanda.myfirstapp.controller;

import android.os.StrictMode;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import amanda.myfirstapp.HomeActivity;
import amanda.myfirstapp.model.Actions;
import amanda.myfirstapp.model.Info;

public class GreenhouseAPI {

    //private String IP = "192.168.56.1";
    //private String IP = "192.168.1.105";
    private String IP = "192.168.43.30";
    //private String IP = "10.0.2.2";
    private final String URL = "http://"+IP+":8080/greenhousews-repo/webresources/greenhouse/";
    private final String TAG = HomeActivity.TAG;

    public String get(String OPERATION){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL + OPERATION);
        //Log.i(TAG, "get: URL: " + URL + OPERATION);
        String result = "";

        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpClient.execute(httpGet, responseHandler);
            Log.i(TAG, "get: response:::: " + response);
            result = response;
        }
        catch(Exception e){
            e.printStackTrace();
        }

        Log.i(TAG, "httpGet: result: " + result);
        return result;
    }

    public String post(String OPERATION, Actions actions){

        Log.i(TAG, "post: started");
        Log.i(TAG, "post: " + URL + OPERATION);
        try {
            Log.i(TAG, "post: creating HttpBuilder");
            HttpClient cli = HttpClientBuilder.create().build();

            Log.i(TAG, "post: creating HttpPost");
            HttpPost requisicao = new HttpPost(URL + OPERATION);
            requisicao.addHeader("Content-Type", "application/json");

            Log.i(TAG, "post: getting actions Json");
            StringEntity se = new StringEntity(new Gson().toJson(actions),"UTF-8");
            requisicao.setEntity((HttpEntity) se);

            Log.i(TAG, "post: executing");
            HttpResponse resposta = cli.execute(requisicao);
            Log.i(TAG, "post: finished");
            return EntityUtils.toString(resposta.getEntity());

        } catch (Exception e) {
            Log.i(TAG, "post: exception");
            e.printStackTrace();
            return null;
        }

    }

    public Info getInfo(){

        System.out.println("GreenhouseAPI : getInfo");
        Info info = new Gson().fromJson(get("get/info"), Info.class);
        return info;
    }
    public Actions getActions(){
        System.out.println("GreenhouseAPI : getActions");
        Actions actions = new Gson().fromJson(get("get/actions"), Actions.class);
        return actions;
    }
    public List<Info> listInfo(){
        System.out.println("GreenhouseAPI : listInfo");
        List<Info> info = new Gson().fromJson(get("list/info"),
                new TypeToken<ArrayList<Info>>(){}.getType());
        return info;
    }
    public List<Info> listInfo(String dates){
        System.out.println("GreenhouseAPI : listInfo");
        List<Info> info = new Gson().fromJson(get("list/info/"+dates),
                new TypeToken<ArrayList<Info>>(){}.getType());
        return info;
    }
    public String setActions(Actions actions){
        System.out.println("GreenhouseAPI : setActions");
        return post("set/actions", actions);
    }

}
