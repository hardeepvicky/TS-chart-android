package in.co.techformation.tschart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import in.co.techformation.tschart.adapter.MenuListAdapter;
import in.co.techformation.tschart.collection.MenuEntity;
import in.co.techformation.tschart.collection.ReportEntity;

/**
 * Created by Hardeep on 16-Sep-17.
 */

public class MenuActivity extends AppCompatActivity
{
    public static MenuEntity root;

    ListView list;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        list = (ListView) findViewById(R.id.list);

        list.setAdapter(new MenuListAdapter(this, root.children, new MenuListAdapter.MenuListAdapterCallback() {
            @Override
            public void onSelect(MenuEntity entity) {

                Intent i = new Intent(MenuActivity.this, MenuActivity.class);
                MenuActivity.root = entity;
                startActivity(i);
            }

            @Override
            public void onSelect(ArrayList<ReportEntity> reportList) {
                Intent i = new Intent(MenuActivity.this, ChartActivity.class);
                ChartActivity.reportEntityList = reportList;
                MenuActivity.this.startActivity(i);
            }
        }));
    }
}
