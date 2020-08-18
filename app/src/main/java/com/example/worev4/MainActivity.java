package com.example.worev4;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.AccessNetworkConstants;
import android.telephony.AvailableNetworkInfo;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.apache.commons.net.ntp.TimeStamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    protected TelephonyManager tm;
    protected AvailableNetworkInfo availableNetworkInfo;
    protected String ltestr;
    protected String[] parts;
    protected List<CellInfo> cellInfoList;
    protected SignalStrengthListener signalStrengthListener;
    String imei;
    TextView textViewRsrq;
    EditText editText;
    TextView textViewCi;
    TextView textViewRSRP;
    TextView textViewRSSI;
    TextView textViewTA;
    GraphView graphView;
    GraphView graphViewBar;
    Button buttonGraphics;
    LineGraphSeries<DataPoint> series;
    BarGraphSeries<DataPoint> barGraphSeries;
    private LineGraphSeries<DataPoint> mSeries1;
    private LineGraphSeries<DataPoint> mSeries2;
    private double graph2LastXValue = 0d;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    String[] permissions;

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewRsrq = findViewById(R.id.textViewRsrqValue);
        textViewCi = findViewById(R.id.textViewCi);
        textViewRSRP = findViewById(R.id.textViewRSRP);
        textViewRSSI = findViewById(R.id.textViewRSSI);
        textViewTA = findViewById(R.id.textViewTA);

        buttonGraphics = findViewById(R.id.buttonGraphics);
        buttonGraphics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Graphics.class);
                startActivity(intent);
            }
        });

        graphView = (GraphView) findViewById(R.id.graph);
        mSeries2 = new LineGraphSeries<>();
        barGraphSeries = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, -1),
                new DataPoint(1, 6),
                new DataPoint(2, 3),
                new DataPoint(3, 6),
        });

        graphView.addSeries(mSeries2);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(40);


        barGraphSeries.setSpacing(50);
        barGraphSeries.setDrawValuesOnTop(true);
        //graphViewBar.setTitle("RSSI");
        //graphViewBar.getViewport().setYAxisBoundsManual(true);
        //graphViewBar.addSeries(barGraphSeries);
        //graphView.getGridLabelRenderer().setHumanRounding(false);
        //permission check
        //get cell info
        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String operatorName = tel.getNetworkOperatorName();
        String resultado = operatorName;
        Log.d("Result", resultado);
        requestFineCoarseLocation();
        Log.d("Result - ", String.valueOf(tel.getAllCellInfo()));


         signalStrengthListener = new SignalStrengthListener();
        ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).
                listen(signalStrengthListener, SignalStrengthListener.LISTEN_SIGNAL_STRENGTHS);

        //new SpeedTestTask().execute();
    }
    protected class SignalStrengthListener extends PhoneStateListener {
        @SuppressLint("MissingPermission")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
            //Teste do gráfico
            //graph2LastXValue += 1d;
            //mSeries2.appendData(new DataPoint(graph2LastXValue, getRandom()), true, 40);
            tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            requestFineCoarseLocation();
            cellInfoList = tm.getAllCellInfo();
            Log.d("cellInfoList", cellInfoList.toString());
            int count = 0;
            for (CellInfo cellInfo : cellInfoList) {
                if (cellInfo instanceof CellInfoLte) {
                    if(cellInfo.isRegistered()){
                        Log.d("infoserver", String.valueOf(cellInfo.isRegistered()) + " " + ((CellInfoLte) cellInfo).getCellIdentity().getPci());
                    }
                    else{
                        Log.d("infoserver",String.valueOf(cellInfo.isRegistered()) + " " + ((CellInfoLte) cellInfo).getCellIdentity().getPci());
                    }
                    if (count == 0) {
                        String time = DateFormat.getTimeInstance(DateFormat.MEDIUM)
                                    .format(System.currentTimeMillis());

                        String data = DateFormat.getDateInstance(DateFormat.SHORT)
                                .format(System.currentTimeMillis());
                        Log.d("date", data + " " +  time);

                        int cellPci = ((CellInfoLte) cellInfo).getCellIdentity().getPci();
                        int ci = ((CellInfoLte) cellInfo).getCellIdentity().getCi();
                        double enobebId = ci / 256;
                        int cid = 0;
                        int tac = ((CellInfoLte) cellInfo).getCellIdentity().getTac();
                        int earfcn = ((CellInfoLte) cellInfo).getCellIdentity().getEarfcn();
                        String operatorLong = (String) ((CellInfoLte) cellInfo).getCellIdentity().getOperatorAlphaLong();
                        String operatorAlphaShort = (String) ((CellInfoLte) cellInfo).getCellIdentity().getOperatorAlphaShort();

                        String mcc = ((CellInfoLte) cellInfo).getCellIdentity().getMccString();
                        String mnc = ((CellInfoLte) cellInfo).getCellIdentity().getMncString();
                        String detail = ((CellInfoLte) cellInfo).getCellIdentity().toString();
                        //Cell Level
                        int rsrp = ((CellInfoLte) cellInfo).getCellSignalStrength().getRsrp();
                        int rsrq = ((CellInfoLte) cellInfo).getCellSignalStrength().getRsrq();
                        int snr = ((CellInfoLte) cellInfo).getCellSignalStrength().getRssnr();
                        int ta = ((CellInfoLte) cellInfo).getCellSignalStrength().getTimingAdvance();
                        int asu = ((CellInfoLte) cellInfo).getCellSignalStrength().getAsuLevel();
                        int cqi = ((CellInfoLte) cellInfo).getCellSignalStrength().getCqi();
                        int dbm = ((CellInfoLte) cellInfo).getCellSignalStrength().getDbm();
                        int level = ((CellInfoLte) cellInfo).getCellSignalStrength().getLevel();
                        int rssi = ((CellInfoLte) cellInfo).getCellSignalStrength().getRssi();
                        textViewRsrq.setText("" + rsrq);
                        textViewCi.setText("" + ci);
                        textViewRSRP.setText("" + rsrp);
                        textViewTA.setText("" + ta);
                        textViewRSSI.setText(""+rssi);
                        double rsrpD = rsrq;
                        graph2LastXValue += 1d;
                        graphView.getViewport().setScrollable(true);
                        mSeries2.appendData(new DataPoint(graph2LastXValue, rsrpD), true, 200);
                        graphView.setTitle("RSRP");
                        //graphView.getViewport().setScalable(true);
                        //barGraphSeries.appendData(new DataPoint(graph2LastXValue,getRandom()),true,40);
                        count++;
                        }
                        Log.d("CellInfo", String.valueOf(cellInfo));
                        // cast to CellInfoLte and call all the CellInfoLte methods you need
                        // Gets the LTE PCI: (returns Physical Cell Id 0..503, Integer.MAX_VALUE if unknown)
                    }
                }

            super.onSignalStrengthsChanged(signalStrength);
        }
    }

    private DataPoint[] generateData() {
        int count = 30;
        DataPoint[] values = new DataPoint[count];
        for (int i = 0; i < count; i++) {
            double x = i;
            double f = mRand.nextDouble() * 0.15 + 0.3;
            double y = Math.sin(i * f + 2) + mRand.nextDouble() * 0.3;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }

    double mLastRandom = 50;
    Random mRand = new Random();

    private double getRandom() {
        return mLastRandom += mRand.nextDouble() * 0.5 - 0.25;
    }

    void requestAccessCoarseLocation(){
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)){
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_ACCESS_COARSE_LOCATION);
            }
        }
    }
    void requestFineCoarseLocation(){
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)){
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_ACCESS_FINE_LOCATION);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_ACCESS_COARSE_LOCATION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this,
                        "ACCESS COARSE LOCATION OK",
                        Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(MainActivity.this,
                        "ACCESS COARSE LOCATION OK",
                        Toast.LENGTH_LONG).show();
            }
        }
        if(requestCode == PERMISSION_ACCESS_FINE_LOCATION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this,
                        "ACCESS FINE LOCATION OK",
                        Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(MainActivity.this,
                        "ACCESS FINE LOCATION OK",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

}