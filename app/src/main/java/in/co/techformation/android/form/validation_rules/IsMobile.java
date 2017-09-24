package in.co.techformation.android.form.validation_rules;

/**
 * Created by Tech on 04-Aug-15.
 */


public class IsMobile extends MatchPattern {

    private static final String PATTERN = "^[789]\\d{9}$";

    public IsMobile(String error_msg)
    {
        super(PATTERN, error_msg);
    }

    @Override
    public String getErrorMessage()
    {
        if (errorMsg != null)
        {
            return errorMsg;
        }

        return "Invalid Mobile Number";
    }
}
