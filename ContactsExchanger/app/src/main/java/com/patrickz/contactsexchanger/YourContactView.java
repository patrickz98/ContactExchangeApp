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
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class YourContactView
{
    private ViewGroup rootView;
    private Context context;

    public YourContactView(ViewGroup rootView)
    {
        this.rootView = rootView;
        this.context  = rootView.getContext();
    }

    private TextView createTextView(String text)
    {
        final JSONObject clicked = new JSONObject();
        clicked.put("clicked", true);

        TextView textView = new TextView(context);
        textView.setText(text);
        // textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setTextSize(40f);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        textView.setPadding(0, 20, 0, 0);

        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (clicked.getBoolean("clicked"))
                {
                    ((TextView) view).setTextColor(Color.parseColor("#969696"));
                    clicked.put("clicked", false);
                }
                else
                {
                    ((TextView) view).setTextColor(Color.parseColor("#000000"));
                    clicked.put("clicked", true);
                }
            }
        });

        return textView;
    }

    private void createQR()
    {
        // final ImageView imageView = (ImageView) rootView.findViewById(R.id.qrCode);
        final ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //imageView.setBackgroundColor(Color.parseColor("#ff3355"));

        rootView.addView(imageView);
//        final ProgressDialog progress = new ProgressDialog(context);
//        progress.setMessage("Wait while loading Contacts.");
//        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
//        progress.show();

        final Handler mHandler = new Handler();

        final String contactString = "{\"name\":\"Patrick Zierahn\",\"number\":\"+49123456789\"}";

        // new Thread(new Runnable()
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                ContactManager contacts = new ContactManager(context);
                contacts.getAllContacts();

                if (contacts.contacts == null) return;

                try
                {
                    final Bitmap bitmap = Simple.encodeAsBitmap(contactString, 800, 800);
                    // imageView.setImageBitmap(getRoundedCornerBitmap(bitmap, 80));

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

                // To dismiss the dialog
                // progress.dismiss();
            }
        }).start();
    }

    public void onCreateView()
    {
        // rootView.setBackgroundColor(Color.parseColor("#232323"));
        rootView.setBackgroundColor(Color.parseColor("#ffffff"));

//        ContactManager contacts = new ContactManager(context);
//        contacts.getAllContacts();

        rootView.addView(createTextView("Patrick Zierahn"));
        rootView.addView(createTextView("+49 123456789"));

        createQR();
    }
}
