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

import java.util.ArrayList;
import java.util.LinkedHashMap;

import in.co.techformation.android.form.Field;
import in.co.techformation.android.form.Form;
import in.co.techformation.android.form.validation_rules.NotEmpty;
import in.co.techformation.tschart.collection.LoginEntity;
import in.co.techformation.tschart.http.WebApi;

/**
 * Created by TechFormation on 20-Jan-17.
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

        }
        else
        {

        }
    }
}
