package com.patrickz.contactsexchanger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment
{
    private static final String LOGTAG = MainActivity.LOGPATTERN + "PlaceholderFragment";

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment()
    {}

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber)
    {
        PlaceholderFragment fragment = new PlaceholderFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        int id = getArguments().getInt(ARG_SECTION_NUMBER);

        Log.d(LOGTAG, "onCreateView id: " + id);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        if (id == 1)
        {
            YourContactView contactView = new YourContactView((ViewGroup) rootView);
            contactView.onCreateView();
        }

        if (id == 2)
        {
            ContactsView contactsView = new ContactsView((ViewGroup) rootView);
            contactsView.createContactList();
        }

        return rootView;
    }
}
