package com.patrickz.contactsexchanger;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class YourContactView
{
    private ViewGroup rootView;
    private Context context;
    private final ImageView imageView;
    private JSONObject json;

    public YourContactView(ViewGroup rootView)
    {
        this.context  = rootView.getContext();
        this.imageView = new ImageView(context);
        this.json = new JSONObject();

        rootView.setBackgroundColor(Color.parseColor("#ffffff"));

        ScrollView scroll = new ScrollView(context);
        rootView.addView(scroll);

        LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        LinearLayout scrollLayout = new LinearLayout(context);
        scrollLayout.setOrientation(LinearLayout.VERTICAL);
        scrollLayout.setLayoutParams(linLayoutParam);

        scroll.addView(scrollLayout);

        this.rootView = scrollLayout;
    }

    private TextView createTextView(String key, String text)
    {
        final JSONObject clicked = new JSONObject();
        clicked.put("clicked", true);

        json.put(key, text);

        TextView textView = new TextView(context);
        textView.setText(text);
        // textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setTextSize(40f);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        textView.setPadding(0, 20, 0, 0);

        final String finalKey  = key;
        final String finalText = text;

        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (clicked.getBoolean("clicked"))
                {
                    ((TextView) view).setTextColor(Color.parseColor("#969696"));
                    clicked.put("clicked", false);

                    json.remove(finalKey);
                    setQrText(json.toString());
                }
                else
                {
                    ((TextView) view).setTextColor(Color.parseColor("#000000"));
                    clicked.put("clicked", true);

                    json.put(finalKey, finalText);
                    setQrText(json.toString());
                }
            }
        });

        return textView;
    }

    private void setQrText(String qrContent)
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

    public void onCreateView()
    {
        // rootView.setBackgroundColor(Color.parseColor("#232323"));
        rootView.setBackgroundColor(Color.parseColor("#ffffff"));

//        ContactManager contacts = new ContactManager(context);
//        contacts.getAllContacts();

        rootView.addView(createTextView("name",  "Patrick Zierahn"));
        rootView.addView(createTextView("phone", "+49 123456789"));

        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //imageView.setBackgroundColor(Color.parseColor("#ff3355"));
        imageView.setPadding(0, 50, 0, 100);

        rootView.addView(imageView);

        setQrText(json.toString());
    }
}
