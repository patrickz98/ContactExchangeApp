package com.patrickz.contactsexchanger;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter
{
    private static final String LOGTAG = MainActivity.LOGPATTERN + "SectionsPager";

    public SectionsPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public int getCount()
    {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        Log.d(LOGTAG, "getPageTitle: " + position);

        switch (position)
        {
            case 0:
                return "Your Contact";
            case 1:
                return "Contacts";
        }
        return null;
    }
}
