package in.co.sevenrocks.android.form.validation_rules;

/**
 * Created by Tech on 04-Aug-15.
 */

public class IsEmail extends MatchPattern
{
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    public IsEmail(String error_msg)
    {
        super(EMAIL_PATTERN, error_msg);
    }

    public String getErrorMessage()
    {
        if (errorMsg != null)
        {
            return errorMsg;
        }

        return "Invalid Email";
    }
}
