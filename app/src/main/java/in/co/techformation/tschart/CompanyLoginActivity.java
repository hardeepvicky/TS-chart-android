package in.co.techformation.tschart;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.LinkedHashMap;

import in.co.techformation.tschart.collection.LoginEntity;
import in.co.techformation.tschart.http.WebApi;

/**
 * Created by TechFormation on 20-Jan-17.
 */
public class CompanyLoginActivity extends AppCompatActivity
{
    TextView lblError;
    EditText txtCompanyCode;
    Button btnOk, btnRegisterCompany;
    LoginEntity loginEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);

        _init();

        loginEntity = LoginEntity.getInstance(getApplicationContext());
        loginEntity.remeber();

        txtCompanyCode.setText(loginEntity.companyCode);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _verify_company_code();
            }
        });

        btnRegisterCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ts-chart-admin.vuwork.com/users/signup"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");

                try
                {
                    getApplicationContext().startActivity(intent);
                }
                catch (ActivityNotFoundException ex)
                {
                    intent.setPackage(null);
                    getApplicationContext().startActivity(intent);
                }
            }
        });
    }

    private void _init()
    {
        lblError = (TextView) findViewById(R.id.lblError);
        lblError.setVisibility(View.GONE);

        txtCompanyCode = (EditText) findViewById(R.id.txtCompanyCode);

        btnOk = (Button) findViewById(R.id.btnOk);
        btnRegisterCompany = (Button) findViewById(R.id.btnRegisterCompany);
    }

    private void _verify_company_code()
    {
        lblError.setVisibility(View.GONE);
        String company_code = txtCompanyCode.getText().toString();

        if (company_code.isEmpty())
        {
            lblError.setText("Please Enter Company code");
            lblError.setVisibility(View.VISIBLE);

            return;
        }

        LinkedHashMap data = new LinkedHashMap();

        data.put("code", company_code);

        WebApi api = new WebApi(this);

        api.get_company_details(data, true, new WebApi.CallBack() {
            @Override
            public void WebApiSuccess(LinkedHashMap data) {

                LoginEntity loginEntity = LoginEntity.getInstance(getApplicationContext());

                loginEntity.companyCode = txtCompanyCode.getText().toString();
                loginEntity.companyId = data.get("id").toString();

                loginEntity.store();

                CompanyLoginActivity.this.finish();
            }

            @Override
            public void WebApiFailure(String msg) {
                lblError.setText(msg);
                lblError.setVisibility(View.VISIBLE);
            }
        });
    }
}
