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
import amanda.myfirstapp.HomeActivity;
import amanda.myfirstapp.model.Actions;
import amanda.myfirstapp.model.Info;

public class GreenhouseAPI {

    public static String IP = "";
    private final String URL = "http://" + IP + ":8080/greenhousews-repo/webresources/greenhouse/";
    private final String TAG = HomeActivity.TAG;

    public String get(String OPERATION) {
        Log.i(TAG, "Getting from url: " + (URL + OPERATION));

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL + OPERATION);
        String result = "";

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpClient.execute(httpGet, responseHandler);
            Log.i(TAG, "get: response:::: " + response);
            result = response;
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "httpGet: result: " + result);
        return result;
    }

    public String post(String OPERATION, Actions actions) {
        Log.i(TAG, "Posting to url: " + (URL + OPERATION));

        try {
            HttpClient cli = HttpClientBuilder.create().build();

            HttpPost requisicao = new HttpPost(URL + OPERATION);
            requisicao.addHeader("Content-Type", "application/json");

            StringEntity se = new StringEntity(new Gson().toJson(actions), "UTF-8");
            requisicao.setEntity((HttpEntity) se);

            HttpResponse resposta = cli.execute(requisicao);
            return EntityUtils.toString(resposta.getEntity());

        } catch (Exception e) {
            Log.i(TAG, "Post exception:");
            e.printStackTrace();
            return null;
        }
    }

    public Info getInfo() {
        Info info = new Gson().fromJson(get("get/info"), Info.class);
        return info;
    }

    public Actions getActions() {
        Actions actions = new Gson().fromJson(get("get/actions"), Actions.class);
        return actions;
    }

    public List<Info> listInfo() {
        List<Info> info = new Gson().fromJson(get("list/info"),
                new TypeToken<ArrayList<Info>>() {
                }.getType());
        return info;
    }

    public List<Info> listInfo(String dates) {
        List<Info> info = new Gson().fromJson(get("list/info/" + dates),
                new TypeToken<ArrayList<Info>>() {
                }.getType());
        return info;
    }

    public String setActions(Actions actions) {
        return post("set/actions", actions);
    }
}
