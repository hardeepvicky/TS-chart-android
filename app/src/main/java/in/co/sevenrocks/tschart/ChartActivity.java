package in.co.sevenrocks.tschart;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import in.co.sevenrocks.tschart.adapter.TabsAdapter;
import in.co.sevenrocks.tschart.collection.ReportEntity;

/**
 * Created by Hardeep on 23-Sep-17.
 */

public class ChartActivity extends AppCompatActivity
{
    public static ArrayList<ReportEntity> reportEntityList;
    ViewPager pager;
    TabsAdapter tabAdapter;
    ActionBar actionBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        tabAdapter = new TabsAdapter(getSupportFragmentManager(), reportEntityList);

        pager = (ViewPager) findViewById(R.id.pager);

        pager.setAdapter(tabAdapter);

        ActionBar.TabListener listener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

            }
        };

        for (ReportEntity entity : reportEntityList)
        {
            actionBar.addTab(actionBar.newTab().setText(entity.name).setTabListener(listener));
        }
    }
}
