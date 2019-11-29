package com.hr.kurtovic.tomislav.familyboard;

import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.hr.kurtovic.tomislav.familyboard.adapter.SectionsPagerAdapter;
import com.hr.kurtovic.tomislav.familyboard.api.UserHelper;
import com.hr.kurtovic.tomislav.familyboard.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BoardActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        ButterKnife.bind(this);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(1);
        setTabIcons(tabs);

    }

    private void setTabIcons(TabLayout tabs) {
        int[] icons = {
                R.drawable.ic_search_black_24dp,
                R.drawable.ic_home_black_24dp,
                R.drawable.ic_account_circle_black_24dp
        };

        for(int i = 0; i < tabs.getTabCount();i++){
            tabs.getTabAt(i).setIcon(icons[i]);
        }
    }
}