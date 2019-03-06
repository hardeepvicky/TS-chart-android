package in.co.sevenrocks.android.form.validation_rules;

/**
 * Created by Tech on 04-Aug-15.
 */


public class IsPincode extends MatchPattern {

    private static final String PATTERN = "[0-9]{6}";

    public IsPincode(String error_msg)
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

        return "Invalid Pincode";
    }
}
