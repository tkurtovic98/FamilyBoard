package com.hr.kurtovic.tomislav.familyboard.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hr.kurtovic.tomislav.familyboard.R;
import com.hr.kurtovic.tomislav.familyboard.ui.ListRoomFragment;
import com.hr.kurtovic.tomislav.familyboard.ui.ProfileFragment;
import com.hr.kurtovic.tomislav.familyboard.ui.RoomChatFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.list_tab, R.string.chat_tab, R.string.profile_tab};
    private final Context mContext;

    private List<Fragment> fragments;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
        populateList();
    }

    private void populateList() {
        fragments = new ArrayList<>();
        fragments.add(new ListRoomFragment());
        fragments.add(new RoomChatFragment());
        fragments.add(new ProfileFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

  /*  @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }*/

    @Override
    public int getCount() {
        return fragments.size();
    }
}