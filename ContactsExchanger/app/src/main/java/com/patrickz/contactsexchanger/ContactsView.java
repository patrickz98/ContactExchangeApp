package com.patrickz.contactsexchanger;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
//        linLayoutParam.setMargins(0, 40, 0, 0);

        LinearLayout scrollLayout = new LinearLayout(context);
        scrollLayout.setOrientation(LinearLayout.VERTICAL);
        scrollLayout.setLayoutParams(linLayoutParam);

        scroll.addView(scrollLayout);

        this.rootView = scrollLayout;
    }

    private TextView contactView(String name)
    {
        TextView textView = new TextView(context);
        textView.setText(name);
        // textView.setTextColor(Color.parseColor("#000000"));
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setTextSize(35f);
        textView.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayoutParam.setMargins(0, 0, 0, 40);

        textView.setLayoutParams(linLayoutParam);

//        textView.setLayoutParams(new LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        textView.setBackground(Simple.roundedCorners(400, "#333333"));

        // textView.setPadding(0, 40, 0, 0);

        return textView;
    }

    public void createContactList()
    {
        for (int inx = 0; inx < 100; inx++)
        {
            rootView.addView(contactView("Contact: " + inx));
        }
    }
}
