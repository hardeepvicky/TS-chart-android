package in.co.techformation.android.form.validation_rules;

/**
 * Created by Tech on 04-Aug-15.
 */


public class IsMinChar extends BaseValidation {

    private int len;

    public IsMinChar(int len, String error_msg)
    {
        super(error_msg);
        this.len = len;
    }

    @Override
    public String getErrorMessage()
    {
        if (errorMsg != null)
        {
            return errorMsg;
        }

        return "Minimum characters " + len + " required";
    }

    @Override
    public boolean isValid(String text)
    {
        return text.length() >= len;
    }
}
