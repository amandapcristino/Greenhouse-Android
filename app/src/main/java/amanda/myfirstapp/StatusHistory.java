package amanda.myfirstapp;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import amanda.myfirstapp.controller.GreenhouseAPI;
import amanda.myfirstapp.model.Actions;
import amanda.myfirstapp.model.GreenhouseData;
import amanda.myfirstapp.model.Info;

public class StatusHistory extends AppCompatActivity {
    private final String TAG = HomeActivity.TAG;
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statushistory);

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

    public void createTable(List<Info> infoList) {
        TableLayout ll = findViewById(R.id.myTableLayout);
        ll.removeAllViewsInLayout();
        ll.setStretchAllColumns(true);


        int total_width = ll.getWidth();
        int columns_size = total_width/8;
/*
        TableRow rowHeader = new TableRow(this);
        TableRow.LayoutParams lph = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        rowHeader.setLayoutParams(lph);

        TextView tr0 = new TextView(this);
        TextView tr1 = new TextView(this);
        TextView tr2 = new TextView(this);
        TextView tr3 = new TextView(this);
        TextView tr4 = new TextView(this);
        TextView tr5 = new TextView(this);
        TextView tr6 = new TextView(this);

        tr0.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tr1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tr2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tr3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tr4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tr5.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tr6.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        tr0.setWidth(columns_size*2);
        tr1.setWidth(columns_size);
        tr2.setWidth(columns_size);
        tr3.setWidth(columns_size);
        tr4.setWidth(columns_size);
        tr5.setWidth(columns_size);
        tr6.setWidth(columns_size);

        tr0.setTextSize(16);
        tr1.setTextSize(16);
        tr2.setTextSize(16);
        tr3.setTextSize(16);
        tr4.setTextSize(16);
        tr5.setTextSize(16);
        tr6.setTextSize(16);

        tr0.setTypeface(null, Typeface.BOLD);
        tr1.setTypeface(null, Typeface.BOLD);
        tr2.setTypeface(null, Typeface.BOLD);
        tr3.setTypeface(null, Typeface.BOLD);
        tr4.setTypeface(null, Typeface.BOLD);
        tr5.setTypeface(null, Typeface.BOLD);
        tr6.setTypeface(null, Typeface.BOLD);

        tr0.setTextColor(this.getResources().getColor(R.color.colorTextHistory));
        tr1.setTextColor(this.getResources().getColor(R.color.colorTextHistory));
        tr2.setTextColor(this.getResources().getColor(R.color.colorTextHistory));
        tr3.setTextColor(this.getResources().getColor(R.color.colorTextHistory));
        tr4.setTextColor(this.getResources().getColor(R.color.colorTextHistory));
        tr5.setTextColor(this.getResources().getColor(R.color.colorTextHistory));
        tr6.setTextColor(this.getResources().getColor(R.color.colorTextHistory));

        tr0.setText("Date");
        tr1.setText("InºC");
        tr2.setText("OutºC");
        tr3.setText("In%");
        tr4.setText("Out%");
        tr5.setText("Soil%");
        tr6.setText("Ph");

        rowHeader.addView(tr0);
        rowHeader.addView(tr1);
        rowHeader.addView(tr2);
        rowHeader.addView(tr3);
        rowHeader.addView(tr4);
        rowHeader.addView(tr5);
        rowHeader.addView(tr6);

        ll.addView(rowHeader, 0);

        NestedScrollView scroll = new NestedScrollView(this);
        ll.addView(scroll,1);

        TableLayout ll2 = new TableLayout(this);
        ll2.setStretchAllColumns(true);
        scroll.addView(ll2);*/

        int i = 0; // if it has a header starts with 1
        for (Info info : infoList) {
            Log.i(TAG, "createTable: " + info.toString());

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);

            TextView t0 = new TextView(this);
            TextView t1 = new TextView(this);
            TextView t2 = new TextView(this);
            TextView t3 = new TextView(this);
            TextView t4 = new TextView(this);
            TextView t5 = new TextView(this);
            TextView t6 = new TextView(this);
/*
            tr0.setWidth(columns_size*2);
            tr1.setWidth(columns_size);
            tr2.setWidth(columns_size);
            tr3.setWidth(columns_size);
            tr4.setWidth(columns_size);
            tr5.setWidth(columns_size);
            tr6.setWidth(columns_size);*/

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

    private class GetDataWS extends AsyncTask<Void, Void, List<Info>> {

        @Override
        protected void onPreExecute() {
            load = ProgressDialog.show(StatusHistory.this,
                    "Por favor, aguarde ...", "Atualizando informações...");
        }

        @Override
        protected List<Info> doInBackground(Void... params) {
            Log.i(TAG, "doInBackground: started");

            GreenhouseAPI ghAPI = new GreenhouseAPI();
            List<Info> infoList = ghAPI.listInfo();
            return infoList;
        }

        @Override
        protected void onPostExecute(List<Info> infoList) {
            if ((infoList != null)&&(infoList.size()>0)) {
                Log.i(TAG, "onPostExecute: creating table");
                createTable(infoList);
                Log.i(TAG, "onPostExecute: table created");
            }
            load.dismiss();
        }
    }
}
