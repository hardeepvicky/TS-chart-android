package in.co.sevenrocks.android.form.validation_rules;

/**
 * Created by Tech on 04-Aug-15.
 */
public interface Validation
{
    String getErrorMessage();

    boolean isValid(String text);
}