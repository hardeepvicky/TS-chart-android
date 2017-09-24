package in.co.techformation.android.form.validation_rules;

/**
 * Created by Tech on 04-Aug-15.
 */

abstract class BaseValidation implements Validation
{
    protected String errorMsg;

    protected BaseValidation(String error_msg)
    {
        errorMsg = error_msg;
    }

    public String getErrorMessage()
    {
        return errorMsg;
    }
}