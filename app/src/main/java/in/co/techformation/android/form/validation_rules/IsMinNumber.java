package in.co.techformation.android.form.validation_rules;

/**
 * Created by Tech on 04-Aug-15.
 */


public class IsMinNumber extends BaseValidation {

    private int num;

    public IsMinNumber(int num, String error_msg)
    {
        super(error_msg);
        this.num = num;
    }

    @Override
    public String getErrorMessage()
    {
        if (errorMsg != null)
        {
            return errorMsg;
        }

        return "Enter less than " + num;
    }

    @Override
    public boolean isValid(String text)
    {
        if (Double.parseDouble(text.trim()) < num)
        {
            return true;
        }
        return false;
    }
}
