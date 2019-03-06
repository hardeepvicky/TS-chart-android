package in.co.sevenrocks.android.form.validation_rules;

/**
 * Created by Tech on 04-Aug-15.
 */

public class MatchPattern extends BaseValidation
{
    private String pattern;

    public MatchPattern(String pattern, String error_msg)
    {
        super(error_msg);
        this.pattern = pattern;
    }

    @Override
    public boolean isValid(String text) {
        return text.isEmpty() || text.matches(pattern);
    }
}
