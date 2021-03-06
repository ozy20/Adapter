package com.ozy.adapter.fragment;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by WORK on 2016/8/31.
 */

public class TabsAdapter extends PagerAdapter {
    public static final boolean DEBUG = false;
    private final FragmentManager _fm;
    private final String[] _titles;
    private Fragment[] _fragments;
    private FragmentTransaction _currentTransaction;
    private Fragment _currentPrimaryItem;

    public TabsAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
        this._currentTransaction = null;
        this._currentPrimaryItem = null;
        this._fm = fm;
        this._fragments = fragments;
        if(titles == null) {
            this._titles = new String[fragments.length];

            for(int i = 0; i < fragments.length; ++i) {
                this._titles[i] = fragments[i].toString();
            }
        } else {
            this._titles = titles;
        }


    }

    public TabsAdapter(FragmentManager fm, Fragment[] fragments) {
        this(fm, fragments, (String[])null);
    }

    public CharSequence getPageTitle(int position) {
        return this._titles[position];
    }

    public int getCount() {
        return this._fragments.length;
    }

    public long getItemId(int position) {
        return (long)this._titles[position].hashCode();
    }

    private void log(String content) {
    }

    private static String nameIt(int viewId, long id) {
        return "ezy:tabs:" + viewId + ":" + id;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        if(this._currentTransaction == null) {
            this._currentTransaction = this._fm.beginTransaction();
        }

        long itemId = this.getItemId(position);
        Fragment fragment = this._fragments[position];
        if(!fragment.isAdded()) {
            this._currentTransaction.add(container.getId(), fragment, nameIt(container.getId(), itemId));
            this.log("Adding item #" + itemId + ": f=" + fragment);
        }

        this._currentTransaction.show(fragment);
        if(fragment != this._currentPrimaryItem) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }

        return fragment;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        if(this._currentTransaction == null) {
            this._currentTransaction = this._fm.beginTransaction();
        }

        this.log("hide item #" + this.getItemId(position) + ": f=" + object + " v=" + ((Fragment)object).getView());
        this._currentTransaction.hide((Fragment)object);
    }

    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment)object;
        if(fragment != this._currentPrimaryItem) {
            if(this._currentPrimaryItem != null) {
                this._currentPrimaryItem.setMenuVisibility(false);
                this._currentPrimaryItem.setUserVisibleHint(false);
            }

            if(fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }

            this._currentPrimaryItem = fragment;
        }

    }

    public void finishUpdate(ViewGroup container) {
        if(this._currentTransaction != null) {
            this._currentTransaction.commitAllowingStateLoss();
            this._currentTransaction = null;
            if(this._fm != null) {
                this._fm.executePendingTransactions();
            }
        }

    }

    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment)object).getView() == view;
    }

    public Parcelable saveState() {
        return null;
    }

    public void startUpdate(ViewGroup container) {
    }

    public void restoreState(Parcelable state, ClassLoader loader) {
    }
}
