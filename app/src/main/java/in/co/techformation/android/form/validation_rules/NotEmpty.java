package in.co.techformation.android.form.validation_rules;

/**
 * Created by Tech on 04-Aug-15.
 */

public class NotEmpty extends BaseValidation
{
    public NotEmpty(String error_msg)
    {
        super(error_msg);
    }

    @Override
    public boolean isValid(String text) {
        return text.trim().length() > 0;
    }
}
