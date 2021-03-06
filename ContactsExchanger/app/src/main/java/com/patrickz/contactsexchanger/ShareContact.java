package com.patrickz.contactsexchanger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import org.json.simple.JSONObject;

public class ShareContact extends Activity
{
    private final static String LOGTAG = MainActivity.LOGPATTERN + "ShareContact";

    private ImageView imageView;

    private void setQrContent(String qrContent)
    {
        final Handler mHandler = new Handler();

        final String contactString = qrContent;

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    final Bitmap bitmap = Simple.encodeAsBitmap(contactString, 800, 800);

                    mHandler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                }
                catch (Exception exc)
                {
                    exc.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        JSONObject json = new JSONObject(intent.getStringExtra("json"));

        Log.d(LOGTAG, json.toString(0));

        int color = Color.parseColor("#7C34F8");
//        int color = Color.parseColor("#1ED760");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }

        //
        // Layout
        //

        LinearLayout layout = new LinearLayout(this);
        layout.setBackgroundColor(Color.WHITE);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        layout.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        setContentView(layout);

        //
        // Toolbar
        //

        Toolbar toolbar = new Toolbar(this);
        toolbar.setTitle(json.getString("name"));
        toolbar.setBackgroundColor(color);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        toolbar.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        layout.addView(toolbar);

        //
        // Qr Code
        //

        imageView = new ImageView(this);

        layout.addView(imageView);

        imageView.setPadding(20, 20, 20, 20);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        setQrContent(json.toString());

        //
        // TextView
        //

        TextView text = new TextView(this);

        text.setTextSize(20f);
        text.setText(json.toString(0));
        // text.setBackgroundColor(Color.parseColor("#ff33ff"));

        text.setPadding(20, 20, 20, 20);

        text.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        layout.addView(text);
    }
}
