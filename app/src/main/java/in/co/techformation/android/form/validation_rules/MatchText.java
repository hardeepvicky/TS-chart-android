package in.co.techformation.android.form.validation_rules;

/**
 * Created by Tech on 04-Aug-15.
 */
import android.widget.EditText;


public class MatchText extends BaseValidation
{
    EditText txtOther;

    public MatchText(EditText txtOther, String error_msg)
    {
        super(error_msg);
        this.txtOther = txtOther;
    }

    @Override
    public boolean isValid(String text)
    {
        return txtOther.getText().toString().equals(text);
    }
}
