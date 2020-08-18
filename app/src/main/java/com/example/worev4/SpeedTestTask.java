package com.example.worev4;

import android.os.AsyncTask;
import android.util.Log;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

public class SpeedTestTask extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... params) {

        SpeedTestSocket speedTestSocket = new SpeedTestSocket();

        // add a listener to wait for speedtest completion and progress
        speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {



            @Override
            public void onCompletion(SpeedTestReport report) {
                // called when download/upload is finished
                Log.v("speedtest", "[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
                Log.v("speedtest", "[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());
            }

            @Override
            public void onProgress(float percent, SpeedTestReport report) {
                // called to notify download/upload progress
                Log.v("speedtest", "[PROGRESS] progress : " + percent + "%");
                Log.v("speedtest", "[PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
                Log.v("speedtest", "[PROGRESS] rate in bit/s   : " + report.getTransferRateBit());
            }

            @Override
            public void onError(SpeedTestError speedTestError, String errorMessage) {
                Log.v("speedtest","ERROR" + errorMessage.toString());
            }
        });

        speedTestSocket.startDownload("http://ipv4.ikoula.testdebit.info/10M.iso");
        //speedTestSocket.startDownload("http://speedtest.pop-sc.rnp.br/10M.iso");
        //speedTestSocket.startUpload( "http://ipv4.ikoula.testdebit.info/", 1000000);
        //String fileName = SpeedTestUtils.generateFileName() + ".txt";
        //speedTestSocket.startUpload("ftp://speedtest.tele2.net/upload/" + fileName, 1000000);


        return null;
    }
}
