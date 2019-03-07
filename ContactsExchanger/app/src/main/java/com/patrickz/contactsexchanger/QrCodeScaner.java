package com.patrickz.contactsexchanger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.provider.ContactsContract.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONException;
import org.json.simple.JSONObject;

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

        JSONObject json = null;

        try
        {
            json = new JSONObject(qrResult);
        }
        catch (JSONException exc)
        {
        }

//        nuke();
//        finish();

        if (json == null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Error with Code!");
            builder.setCancelable(false);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int id)
                {
                    startScanner();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();

            return;
        }

        Log.d(LOGTAG, "json = " + json.toString());

//        Intent intent = new Intent(this, SaveContact.class);
//        intent.putExtra("json", qrResult);


        Intent intent = new Intent(Intents.Insert.ACTION);
        // Sets the MIME type to match the Contacts Provider
        intent.setType(RawContacts.CONTENT_TYPE);

        if (json.has("name"))
        {
            intent.putExtra(Intents.Insert.NAME, json.getString("name"));
            Log.d(LOGTAG, "has name");
        }

        if (json.has("mail"))
        {
            intent.putExtra(Intents.Insert.EMAIL, json.getString("mail"));
            intent.putExtra(Intents.Insert.EMAIL_TYPE, CommonDataKinds.Email.TYPE_MOBILE);
            Log.d(LOGTAG, "has mail");
        }

        if (json.has("numbers"))
        {
            JSONArray numbers = json.getJSONArray("numbers");
            intent.putExtra(Intents.Insert.PHONE, numbers.getJSONObject(0).getString("normalizedNumber"));

            Log.d(LOGTAG, "has numbers");
        }

        this.startActivity(intent);
    }
}