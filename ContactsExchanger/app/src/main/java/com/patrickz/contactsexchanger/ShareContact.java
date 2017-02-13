package com.patrickz.contactsexchanger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import org.json.simple.JSONObject;

public class ShareContact extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        JSONObject json = new JSONObject(intent.getStringExtra("json"));

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
        layout.setOrientation(LinearLayout.VERTICAL);


        layout.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        setContentView(layout);

        //
        // Toolbar
        //

        Toolbar toolbar = new Toolbar(this);
        toolbar.setTitle(intent.getStringExtra("name"));
        toolbar.setBackgroundColor(color);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        toolbar.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        layout.addView(toolbar);

        //
        // TextView
        //

        TextView text = new TextView(this);

        text.setTextSize(20f);
        text.setText(json.toString(2));
        // text.setBackgroundColor(Color.parseColor("#ff33ff"));

        text.setPadding(20, 20, 20, 20);

        text.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        layout.addView(text);
    }
}
