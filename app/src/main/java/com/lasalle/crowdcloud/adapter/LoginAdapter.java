package com.lasalle.crowdcloud.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import fragments.Login;
import fragments.Register;

public class LoginAdapter extends FragmentStatePagerAdapter {
    private final String[] listTab = {"Login", "Register"};
    private final Login mLoginFragment;
    private final Register mRegisterFragment;


    public LoginAdapter(FragmentManager fm) {
        super( fm );
        mLoginFragment = new Login();
        mRegisterFragment = new Register();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return mLoginFragment;
        } else
            return mRegisterFragment;
    }

    @Override
    public int getCount() {
        return listTab.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTab[position];
    }
}
