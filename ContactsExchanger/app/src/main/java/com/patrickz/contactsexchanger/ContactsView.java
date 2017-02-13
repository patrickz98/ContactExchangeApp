package com.patrickz.contactsexchanger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.simple.JSONObject;

import java.util.Arrays;

public class ContactsView
{
    private ViewGroup rootView;
    private Context context;

    ContactsView(ViewGroup rootView)
    {
        this.context = rootView.getContext();

        rootView.setBackgroundColor(Color.parseColor("#ffffff"));

        ScrollView scroll = new ScrollView(context);
        rootView.addView(scroll);

        LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        LinearLayout scrollLayout = new LinearLayout(context);
        scrollLayout.setOrientation(LinearLayout.VERTICAL);
        scrollLayout.setLayoutParams(linLayoutParam);

        scrollLayout.setPadding(0, 0, 0, 200);

        scroll.addView(scrollLayout);

        this.rootView = scrollLayout;
    }

    private LinearLayout contactView(final String name, JSONObject json)
    {
        if (name.length() <= 0)
        {
            return null;
        }

        LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayoutParam.setMargins(20, 20, 20, 20);

        LinearLayout pupsLayout = new LinearLayout(context);
        pupsLayout.setOrientation(LinearLayout.HORIZONTAL);
        pupsLayout.setLayoutParams(linLayoutParam);

        final Context toastContext = context;
        final String jsonString = json.toString();

        pupsLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(toastContext, ShareContact.class);
                intent.putExtra("name", name);
                intent.putExtra("json", jsonString);

                toastContext.startActivity(intent);
            }
        });

        //
        // Circle
        //

        String firstChar = "" + name.charAt(0);

        TextView textView = new TextView(context);
         textView.setText(firstChar.toUpperCase());
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setTextSize(35f);
        textView.setGravity(Gravity.CENTER);
        // textView.setBackground(Simple.roundedCorners(400, "#333333"));
        textView.setBackground(Simple.roundedCorners(400, "#7C34F8"));

        textView.setLayoutParams(new LinearLayout.LayoutParams(150, 150));

        pupsLayout.addView(textView);

        //
        // Label
        //

        TextView nameView = new TextView(context);
        nameView.setText(name);
        nameView.setTextColor(Color.parseColor("#000000"));
        nameView.setTextSize(35f);
        nameView.setGravity(Gravity.LEFT);
        // nameView.setBackgroundColor(Color.parseColor("#32ff44"));

        nameView.setPadding(40, 0, 0, 0);

        nameView.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 150));

        pupsLayout.addView(nameView);

        return pupsLayout;
    }

    public void createContactList()
    {
        ContactManager contacts = new ContactManager(context);
        contacts.getAllContacts();

        String[] names = new String[ contacts.contacts.length() ];

        // Hack, doesn't work well...
        JSONObject array = new JSONObject();

        for (int inx = 0; inx < contacts.contacts.length(); inx++)
        {
            JSONObject json = contacts.contacts.getJSONObject(inx);

            String name = json.getString("name");

            array.put(name, json);

            names[ inx ] = name;
        }

        Arrays.sort(names);

        for (int inx = 0; inx < names.length; inx++)
        {
            String name = names[ inx ];
            rootView.addView(contactView(name, array.getJSONObject(name)));
        }
    }
}
