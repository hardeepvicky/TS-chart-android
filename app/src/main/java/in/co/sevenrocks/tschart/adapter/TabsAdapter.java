package in.co.sevenrocks.tschart.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import in.co.sevenrocks.tschart.WebviewFragment;
import in.co.sevenrocks.tschart.collection.ReportEntity;

/**
 * Created by Hardeep on 24-Sep-17.
 */

public class TabsAdapter extends FragmentStatePagerAdapter {

    ArrayList<ReportEntity> list;

    public TabsAdapter(FragmentManager fm, ArrayList<ReportEntity> reportList) {
        super(fm);
        this.list = reportList;
    }

    @Override
    public Fragment getItem(int position) {

        ReportEntity entity = this.list.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("url", entity.url);

        WebviewFragment fragment = new WebviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }
}
