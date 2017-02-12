package com.patrickz.contactsexchanger;

import android.app.Activity;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeScaner extends Activity implements ZXingScannerView.ResultHandler
{
    private final static String LOGTAG = MainActivity.LOGPATTERN + "QrCodeScaner";

    private static ZXingScannerView mScannerView;
    private boolean qrStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d(LOGTAG, "onCreate");

        startScanner();
    }

    public void startScanner()
    {
        Log.d(LOGTAG, "startScanner");

        mScannerView = new ZXingScannerView(this);

        mScannerView.setResultHandler(this);
        mScannerView.setAutoFocus(true);
        mScannerView.setFlash(false);

        mScannerView.startCamera();

        qrStart = true;
        this.setContentView(mScannerView);
    }

    public void nuke()
    {
        Log.d(LOGTAG, "nuke");

        if (mScannerView != null)
        {
            mScannerView.stopCamera();
            mScannerView = null;
        }

        qrStart = false;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        Log.d(LOGTAG, "resumeScanner");

        if (! qrStart) startScanner();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        Log.d(LOGTAG, "pauseScanner");

        if (qrStart) nuke();
    }

    @Override
    public void onBackPressed()
    {
        nuke();

        super.onBackPressed();
    }

    @Override
    public void handleResult(Result rawResult)
    {
        String qrResult = rawResult.getText();
        Toast.makeText(this, "Scan: " + qrResult, Toast.LENGTH_SHORT).show();

        Log.d(LOGTAG, "Scan: " + qrResult);

//        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME);
        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, ToneGenerator.MAX_VOLUME);
        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);

        onBackPressed();
    }
}