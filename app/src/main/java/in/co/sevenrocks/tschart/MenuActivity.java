package in.co.sevenrocks.tschart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import in.co.sevenrocks.tschart.adapter.MenuListAdapter;
import in.co.sevenrocks.tschart.collection.LoginEntity;
import in.co.sevenrocks.tschart.collection.MenuEntity;
import in.co.sevenrocks.tschart.collection.ReportEntity;

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

                LoginEntity loginEntity = LoginEntity.getInstance(getApplicationContext());
                if (entity.isPrivate && loginEntity.username.isEmpty())
                {
                    Intent i = new Intent(MenuActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                else
                {
                    MenuActivity.root = entity;
                    Intent i = new Intent(MenuActivity.this, MenuActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onSelect(MenuEntity entity, ArrayList<ReportEntity> reportList)
            {
                LoginEntity loginEntity = LoginEntity.getInstance(getApplicationContext());
                if (entity.isPrivate && loginEntity.username.isEmpty())
                {
                    Intent i = new Intent(MenuActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(MenuActivity.this, ChartActivity.class);
                    ChartActivity.reportEntityList = reportList;
                    MenuActivity.this.startActivity(i);
                }


            }
        }));
    }
}
