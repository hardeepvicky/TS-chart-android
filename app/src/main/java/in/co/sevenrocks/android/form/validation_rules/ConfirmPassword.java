package in.co.sevenrocks.android.form.validation_rules;

import android.content.Context;
import android.widget.EditText;

/**
 * Created by Anshita-Tech on 1/14/2016.
 */
public class ConfirmPassword implements Validation {
    EditText txtPassword;
    Context mContext;
    String msg;

    public ConfirmPassword(Context context,EditText txtpassword, String error_msg)
    {
        txtPassword = txtpassword;
        mContext = context;
        msg = error_msg;
    }

    public ConfirmPassword(Context context,EditText txtpassword)
    {
        txtPassword = txtpassword;
        mContext = context;
        msg = "";
    }

    @Override
    public String getErrorMessage()
    {
          return msg;
    }

    @Override
    public boolean isValid(String text)
    {
        return txtPassword.getText().toString().equals(text);
    }
}
