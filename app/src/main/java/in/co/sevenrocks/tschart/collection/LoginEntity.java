package in.co.sevenrocks.tschart.collection;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Hardeep on 16-Sep-17.
 */

public class LoginEntity
{
    static String TAG = "LoginEntity";
    static LoginEntity instance;

    public String companyCode, companyId, username, password;
    private Context context;

    public LoginEntity(Context context)
    {
        this.context = context;
        this.username = this.password = this.companyId = this.companyCode = "";
    }

    public static LoginEntity getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new LoginEntity(context);
        }

        return instance;
    }

    public void store()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TAG, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("companyCode", companyCode);
        editor.putString("companyId", companyId);
        editor.commit();
    }

    public void remeber()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TAG, context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        password = sharedPreferences.getString("password", "");
        companyCode = sharedPreferences.getString("companyCode", "");
        companyId = sharedPreferences.getString("companyId", "");
    }
}
