package amanda.myfirstapp;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import amanda.myfirstapp.controller.GreenhouseAPI;
import amanda.myfirstapp.model.Info;

public class StatusHistory extends AppCompatActivity {
    private final String TAG = HomeActivity.TAG;
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statushistory);

        // Get table data
        getData();

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

    /* Call the class for retrieving data from the Web Service. */
    public void getData() {
        GetDataWS download = new GetDataWS();
        download.execute();
    }

    /* Method that creates the table with the retrieved values. */
    public void createTable(List<Info> infoList) {

        TableLayout ll = findViewById(R.id.tableDataLayout);
        ll.removeAllViewsInLayout();
        ll.setStretchAllColumns(true);

        int i = 0;
        for (Info info : infoList) {

            TableRow row = new TableRow(this);
            TableRow.LayoutParams date_params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.3f);
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);

            Typeface face = Typeface.createFromAsset(getApplicationContext().getAssets(),"quicksand.ttf");

            TextView t0 = new TextView(this);
            TextView t1 = new TextView(this);
            TextView t2 = new TextView(this);
            TextView t3 = new TextView(this);
            TextView t4 = new TextView(this);
            TextView t5 = new TextView(this);
            TextView t6 = new TextView(this);

            t0.setLayoutParams(date_params);
            t1.setLayoutParams(params);
            t2.setLayoutParams(params);
            t3.setLayoutParams(params);
            t4.setLayoutParams(params);
            t5.setLayoutParams(params);
            t6.setLayoutParams(params);

            t0.setTypeface(face);
            t1.setTypeface(face);
            t2.setTypeface(face);
            t3.setTypeface(face);
            t4.setTypeface(face);
            t5.setTypeface(face);
            t6.setTypeface(face);

            t0.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            t1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            t2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            t3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            t4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            t5.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            t6.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
            Date d = new Date(info.date.getTime());
            String date = df.format(d);

            t0.setText(date);
            t1.setText(String.valueOf(info.airTemperature));
            t2.setText(String.valueOf(info.sensors.airTemperature));
            t3.setText(String.valueOf(info.airHumidity));
            t4.setText(String.valueOf(info.sensors.airHumidity));
            t5.setText(String.valueOf(info.sensors.soilHumidity));
            t6.setText(String.valueOf(info.sensors.soilPh));

            t0.setTextColor(this.getResources().getColor(R.color.colorTextHistory));
            t1.setTextColor(this.getResources().getColor(R.color.colorTextHistory));
            t2.setTextColor(this.getResources().getColor(R.color.colorTextHistory));
            t3.setTextColor(this.getResources().getColor(R.color.colorTextHistory));
            t4.setTextColor(this.getResources().getColor(R.color.colorTextHistory));
            t5.setTextColor(this.getResources().getColor(R.color.colorTextHistory));
            t6.setTextColor(this.getResources().getColor(R.color.colorTextHistory));

            row.addView(t0);
            row.addView(t1);
            row.addView(t2);
            row.addView(t3);
            row.addView(t4);
            row.addView(t5);
            row.addView(t6);

            ll.addView(row, i++);
        }
    }

    /* Class for asynchronously retrieve data from the WebService. */
    private class GetDataWS extends AsyncTask<Void, Void, List<Info>> {

        @Override
        protected void onPreExecute() {
            load = ProgressDialog.show(StatusHistory.this,
                    "Por favor, aguarde ...", "Atualizando informações...");
        }

        @Override
        protected List<Info> doInBackground(Void... params) {
            Log.i(TAG, "Getting data...");

            GreenhouseAPI ghAPI = new GreenhouseAPI();
            List<Info> infoList = ghAPI.listInfo();
            return infoList;
        }

        @Override
        protected void onPostExecute(List<Info> infoList) {
            Log.i(TAG, "Creating table...");

            if ((infoList != null) && (infoList.size() > 0)) {
                createTable(infoList);
                Log.i(TAG, "Adding info...");
            }

            Log.i(TAG, "Finished");
            load.dismiss();
        }
    }
}
