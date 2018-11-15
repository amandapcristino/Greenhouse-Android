package amanda.myfirstapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DecimalFormat;

import amanda.myfirstapp.model.Actions;
import amanda.myfirstapp.model.GreenhouseData;
import amanda.myfirstapp.model.Info;
import amanda.myfirstapp.controller.GreenhouseAPI;

public class HomeActivity extends AppCompatActivity {
    public static final String TAG = "LOG_AMANDA";
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        GetDataWS download = new GetDataWS();
        download.execute();

        final SwipeRefreshLayout srl = (SwipeRefreshLayout) findViewById(R.id.refreshingLayout);

        srl.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        GetDataWS download = new GetDataWS();
                        download.execute();
                        srl.setRefreshing(false);
                    }
                }
        );
    }

    public void onSwitchCheckedChanged(View view) {
        UpdateDataWS update = new UpdateDataWS();
        update.execute();
    }

    private void setValues(Info info, Actions actions) {

        Log.i(TAG, "trying to set values");
        final TextView lblTempIn = findViewById(R.id.lblTempInValue);
        final TextView lblTempOut = findViewById(R.id.lblTempOutValue);
        final TextView lblHumidIn = findViewById(R.id.lblUmidInValue);
        final TextView lblHumidOut = findViewById(R.id.lblUmidOutValue);
        final TextView lblHumidSoil = findViewById(R.id.lblUmidSoilValue);
        final TextView lblSoilPh = findViewById(R.id.lblPhSoilValue);

        lblTempIn.setText(String.valueOf(info.sensors.airTemperature) + " ºC");
        lblTempOut.setText(String.valueOf(info.airTemperature) + " ºC");
        lblHumidIn.setText(String.valueOf(info.sensors.airHumidity) + " %");
        lblHumidOut.setText(String.valueOf(info.airHumidity)+" %");
        lblHumidSoil.setText(String.valueOf(info.sensors.soilHumidity) + " %");
        DecimalFormat df = new DecimalFormat("#.00");
        String result = df.format(info.sensors.soilPh);
        String ph = result.replace(',', '.');
        lblSoilPh.setText(ph);

        final Switch switchLights = findViewById(R.id.switchLights);
        final Switch switchIrrigation = findViewById(R.id.switchIrrigation);
        final Switch switchExaust = findViewById(R.id.switchExaustor);
        switchLights.setChecked(actions.light);
        switchIrrigation.setChecked(actions.water);
        switchExaust.setChecked(actions.exaust);

    }

    public void onButtonClick(View view) {
        Log.i(TAG, "onHistoryButtonClick:  Call activity..");
        startActivity(new Intent(HomeActivity.this, StatusHistory.class));
    }



private class GetDataWS extends AsyncTask<Void, Void, GreenhouseData> {

    @Override
    protected void onPreExecute(){
         load = ProgressDialog.show(HomeActivity.this,
                "Por favor, aguarde ...", "Recuperando Informações do Servidor...");
    }

    @Override
    protected GreenhouseData doInBackground(Void... params) {
        Log.i(TAG, "doInBackground: started");
        //ConnectionWS ghAPI = new ConnectionWS();
        GreenhouseAPI ghAPI = new GreenhouseAPI();
        Info info = ghAPI.getInfo();
        Actions actions = ghAPI.getActions();
        return new GreenhouseData(info, actions);
    }

    @Override
    protected void onPostExecute(GreenhouseData data){
        Log.i(TAG, "onPostExecute: started");
        if ((data.getInfo() != null)&&(data.getActions()!=null)) {
            Log.i(TAG, "onPostExecute: not null");
            setValues(data.getInfo(), data.getActions());
        }
        Log.i(TAG, "onPostExecute: all setted");;
        load.dismiss();
    }
}

private class UpdateDataWS extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            Log.i(TAG, "doInBackground: started");

            final Switch switchLights = findViewById(R.id.switchLights);
            final Switch switchIrrigation = findViewById(R.id.switchIrrigation);
            final Switch switchExaust = findViewById(R.id.switchExaustor);

            Actions actions = new Actions(switchLights.isChecked(), switchIrrigation.isChecked(), switchExaust.isChecked());
            return new GreenhouseAPI().setActions(actions);
        }

        @Override
        protected void onPostExecute(String result){
            Log.i(TAG, "onPostExecute: started");
            Log.i(TAG, "onPostExecute: " + result);;
            load.dismiss();
        }
    }
}