package amanda.myfirstapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import java.text.DecimalFormat;
import amanda.myfirstapp.controller.GreenhouseAPI;
import amanda.myfirstapp.model.Actions;
import amanda.myfirstapp.model.GreenhouseData;
import amanda.myfirstapp.model.Info;

public class HomeActivity extends AppCompatActivity {
    public static final String TAG = "LOG_AMANDA";
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Input dialog for server IP address
        settings(null);

        // RefreshLayout configurations
        final SwipeRefreshLayout srl = (SwipeRefreshLayout) findViewById(R.id.refreshingLayout);
        srl.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getData();
                        srl.setRefreshing(false);
                    }
                }
        );
    }

    /* Method for setting the server IP address. */
    public void settings(View view) {
        //Creates a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("IP do Servidor:");
        //And an input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        //Set dialog buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputOkClicked(input);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    /* Method for settings dialog action. */
    public void inputOkClicked(EditText input){
        String IP = input.getText().toString();
        GreenhouseAPI.IP = IP;
        getData();
    }

    /* Call the class for retrieving data from the Web Service. */
    public void getData(){
        GetWSData download = new GetWSData();
        download.execute();
    }

    /* Method for setting status and actions values. */
    private void setInfoValues(Info info, Actions actions) {

        Log.i(TAG, "Setting values...");
        final TextView lblTempIn = findViewById(R.id.txtInsideTemperatureValue);
        final TextView lblTempOut = findViewById(R.id.txtOutsideTemperatureValue);
        final TextView lblHumidIn = findViewById(R.id.txtInsideHumidityValue);
        final TextView lblHumidOut = findViewById(R.id.txtOutsideHumidityValue);
        final TextView lblHumidSoil = findViewById(R.id.txtSoilHumidityValue);
        final TextView lblSoilPh = findViewById(R.id.txtSoilPhValue);

        lblTempIn.setText(String.valueOf(info.sensors.airTemperature) + " ºC");
        lblTempOut.setText(String.valueOf(info.airTemperature) + " ºC");
        lblHumidIn.setText(String.valueOf(info.sensors.airHumidity) + " %");
        lblHumidOut.setText(String.valueOf(info.airHumidity) + " %");
        lblHumidSoil.setText(String.valueOf(info.sensors.soilHumidity) + " %");
        DecimalFormat df = new DecimalFormat("#.00");
        String result = df.format(info.sensors.soilPh);
        String ph = result.replace(',', '.');
        lblSoilPh.setText(ph);

        final Switch switchLights = findViewById(R.id.switchLights);
        final Switch switchIrrigation = findViewById(R.id.switchIrrigation);
        final Switch switchExaust = findViewById(R.id.switchExhaust);
        switchLights.setChecked(actions.light);
        switchIrrigation.setChecked(actions.water);
        switchExaust.setChecked(actions.exaust);
    }

    /* Action method for history button. */
    public void onHistoryButtonClicked(View view) {
        Log.i(TAG, "onHistoryButtonClick:  Call activity..");
        startActivity(new Intent(HomeActivity.this, StatusHistory.class));
    }

    /* Action method for the switch buttons. */
    public void onSwitchCheckedChanged(View view) {
        UpdateWSData update = new UpdateWSData();
        update.execute();
    }

    /* Class for asynchronously retrieve data from the WebService. */
    private class GetWSData extends AsyncTask<Void, Void, GreenhouseData> {

        @Override
        protected void onPreExecute() {
            load = ProgressDialog.show(HomeActivity.this,
                    "Por favor, aguarde ...", "Recuperando Informações do Servidor...");
        }

        @Override
        protected GreenhouseData doInBackground(Void... params) {
            Log.i(TAG, "Getting data...");
            GreenhouseAPI ghAPI = new GreenhouseAPI();
            Info info = ghAPI.getInfo();
            Actions actions = ghAPI.getActions();
            return new GreenhouseData(info, actions);
        }

        @Override
        protected void onPostExecute(GreenhouseData data) {
            if ((data.getInfo() != null) && (data.getActions() != null)) {
                Log.i(TAG, "Setting values...");
                setInfoValues(data.getInfo(), data.getActions());
            }
            else{
                Log.i(TAG, "The response was null");
            }
            Log.i(TAG, "All set");
            load.dismiss();
        }
    }

    /* Class for asynchronously send data to the WebService. */
    private class UpdateWSData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            Log.i(TAG, "Executing UpdateWSData");

            final Switch switchLights = findViewById(R.id.switchLights);
            final Switch switchIrrigation = findViewById(R.id.switchIrrigation);
            final Switch switchExaust = findViewById(R.id.switchExhaust);

            Actions actions = new Actions(switchLights.isChecked(), switchIrrigation.isChecked(), switchExaust.isChecked());
            return new GreenhouseAPI().setActions(actions);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "The data was set");
            Log.i(TAG, "The result was: " + result);
            load.dismiss();
        }
    }

}

