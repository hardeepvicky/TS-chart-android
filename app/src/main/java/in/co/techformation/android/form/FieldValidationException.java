package in.co.techformation.android.form;

/**
 * Created by Tech on 04-Aug-15.
 */


public class FieldValidationException extends Exception
{
    public Field field;

    public FieldValidationException(String message, Field field)
    {
        super(message);
        this.field = field;
    }
}
