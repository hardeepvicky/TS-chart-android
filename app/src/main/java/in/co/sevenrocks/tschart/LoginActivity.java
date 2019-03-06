package in.co.sevenrocks.tschart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedHashMap;

import in.co.sevenrocks.android.form.Field;
import in.co.sevenrocks.android.form.Form;
import in.co.sevenrocks.android.form.validation_rules.NotEmpty;
import in.co.sevenrocks.tschart.collection.LoginEntity;
import in.co.sevenrocks.tschart.http.WebApi;

/**
 * Created on 20-Jan-17.
 */
public class LoginActivity extends AppCompatActivity
{
    EditText txtUsername, txtPassword;
    TextView lblError;
    Button btnLogin;

    Form form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _init();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _attemptLogin();
            }
        });
    }

    private void _init()
    {
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        lblError = (TextView) findViewById(R.id.lblError);

        btnLogin = (Button) findViewById(R.id.btnLogin);

        form = new Form(this, Form.validationPopulate.SHOW_NONE);
        form.addField(Field.using(txtUsername, "username").validate(new NotEmpty("Please enter Username") ));
        form.addField(Field.using(txtPassword, "password").validate(new NotEmpty("Please enter Password") ));

        lblError.setText("");
    }

    private void _attemptLogin()
    {
        lblError.setText("");

        if (form.isValid())
        {
            WebApi api = new WebApi(this);

            final LoginEntity loginEntity = LoginEntity.getInstance(getApplicationContext());

            LinkedHashMap data = form.getData();
            data.put("company_id", loginEntity.companyId);
            api.login(data, new WebApi.CallBack() {
                @Override
                public void WebApiSuccess(LinkedHashMap data) {

                    loginEntity.username = txtUsername.getText().toString();
                    loginEntity.password = txtPassword.getText().toString();

                    loginEntity.store();

                    LoginActivity.this.finish();
                }

                @Override
                public void WebApiFailure(String msg) {
                    lblError.setText(msg);
                    lblError.setVisibility(View.VISIBLE);
                }
            });
        }
        else
        {
            lblError.setText(form.getErrorString());
            lblError.setVisibility(View.VISIBLE);
        }
    }
}
