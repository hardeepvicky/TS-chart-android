package in.co.techformation.tschart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import in.co.techformation.tschart.collection.LoginEntity;
import in.co.techformation.tschart.collection.MenuEntity;
import in.co.techformation.tschart.collection.ReportEntity;
import in.co.techformation.tschart.http.WebApi;

/**
 * Created by TechFormation on 20-Jan-17.
 */
public class SplashScreenActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    TextView lblError;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        lblError = (TextView) findViewById(R.id.lblError);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        progressBar.setVisibility(View.INVISIBLE);
        lblError.setVisibility(View.INVISIBLE);

        LoginEntity loginEntity = LoginEntity.getInstance(getApplicationContext());
        loginEntity.remeber();

        if (loginEntity.companyId.isEmpty())
        {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(SplashScreenActivity.this, CompanyLoginActivity.class);
                    startActivity(i);

                }
            }, SPLASH_TIME_OUT);
        }
        else
        {
            _fetchWebApiData();
        }
    }

    private void _fetchWebApiData()
    {
        progressBar.setVisibility(View.VISIBLE);
        lblError.setVisibility(View.INVISIBLE);

        WebApi webApi = new WebApi(this);

        webApi.get_menu_reports(new WebApi.CallBack() {
            @Override
            public void WebApiSuccess(LinkedHashMap data) {
                progressBar.setVisibility(View.INVISIBLE);

                MenuEntity.root.clear();
                _set_menus(data, MenuEntity.root);

                Intent i = new Intent(SplashScreenActivity.this, MenuActivity.class);
                MenuActivity.root = MenuEntity.root;
                startActivity(i);
            }

            @Override
            public void WebApiFailure(String msg) {
                progressBar.setVisibility(View.INVISIBLE);
                lblError.setText(msg);
                lblError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void _set_menus(LinkedHashMap data, MenuEntity parent)
    {
        Set set = data.entrySet();
        Iterator itr = set.iterator();

        while(itr.hasNext())
        {
            Map.Entry row = (Map.Entry) itr.next();

            LinkedHashMap record =  (LinkedHashMap) row.getValue();

            LinkedHashMap menu = (LinkedHashMap) record.get("ChartMenu");

            String id = String.valueOf(menu.get("id"));
            String parent_id = String.valueOf(menu.get("parent_id"));
            String name = String.valueOf(menu.get("name"));
            String type = String.valueOf(menu.get("type"));

            MenuEntity entity = new MenuEntity(id, name, parent_id, type);

            parent.addChild(entity);

            if (record.containsKey("children"))
            {
                LinkedHashMap children = (LinkedHashMap) record.get("children");
                _set_menus(children, entity);
            }
            else if (record.containsKey("ChartReport"))
            {
                LinkedHashMap chartReportData = (LinkedHashMap) record.get("ChartReport");
                Set set2 = chartReportData.entrySet();
                Iterator itr2 = set2.iterator();

                while(itr2.hasNext()) {
                    Map.Entry row2 = (Map.Entry) itr2.next();

                    LinkedHashMap report = (LinkedHashMap) row2.getValue();
                    String name2 = String.valueOf(report.get("name"));
                    String url = String.valueOf(report.get("url"));

                    entity.addReport(new ReportEntity(name2, url));
                }
            }
        }
    }
}
