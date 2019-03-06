package in.co.sevenrocks.android.form;

/**
 * Created by Tech on 04-Aug-15.
 */

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import in.co.sevenrocks.android.collection.Entity;
import in.co.sevenrocks.android.collection.FileEntity;
import in.co.sevenrocks.android.form.validation_rules.Validation;


public class Field
{
    public static enum FieldType { TextView, EditText, Spinner, ImageView, CheckBox }

    private List<Validation> mValidations = new LinkedList<Validation>();

    public TextView mTextView;
    public EditText mEditText;
    public Spinner mSpinner;
    public ImageView mImageView;
    public CheckBox mCheckBox;
    public FieldType fieldType;
    public String fieldName;

    private Field(TextView textView, String fieldName) {
        this.mTextView = textView;
        this.fieldType = FieldType.TextView;
        this.fieldName = fieldName;
    }

    private Field(EditText editText, String fieldName) {
        this.mEditText = editText;
        this.fieldType = FieldType.EditText;
        this.fieldName = fieldName;
    }

    public static Field using(EditText textView) {
        return new Field(textView, "");
    }

    private Field(Spinner spinner, String fieldName) {
        this.mSpinner = spinner;
        this.fieldType = FieldType.Spinner;
        this.fieldName = fieldName;
    }

    private Field(ImageView imageView, String fieldName) {
        this.mImageView = imageView;
        this.fieldType = FieldType.ImageView;
        this.fieldName = fieldName;
    }

    private Field(CheckBox checkBox, String fieldName) {
        this.mCheckBox = checkBox;
        this.fieldType = FieldType.CheckBox;
        this.fieldName = fieldName;
    }

    public static Field using(Spinner spinner) {
        return new Field(spinner, "");
    }

    public static Field using(ImageView imageView) {
        return new Field(imageView, "");
    }

    public static Field using(TextView textView, String fieldName) {
        return new Field(textView, fieldName);
    }

    public static Field using(EditText textView, String fieldName) {
        return new Field(textView, fieldName);
    }

    public static Field using(Spinner spinner, String fieldName) {
        return new Field(spinner, fieldName);
    }

    public static Field using(ImageView imageView, String fieldName) {
        return new Field(imageView, fieldName);
    }

    public static Field using(CheckBox checkBox, String fieldName) {
        return new Field(checkBox, fieldName);
    }

    public Field validate(Validation what) {
        mValidations.add(what);
        return this;
    }

    public boolean isValid() throws FieldValidationException
    {
        if (mValidations.size() > 0) {
            for (Validation validation : mValidations) {
                switch (this.fieldType) {
                    case EditText:
                        if (mEditText.isShown() && !validation.isValid(mEditText.getText().toString()))
                        {
                            throw new FieldValidationException(validation.getErrorMessage(), this);
                        }
                        break;

                    case Spinner:
                        if (mSpinner.isShown())
                        {
                            Entity obj = (Entity) mSpinner.getSelectedItem();

                            if (!validation.isValid(obj.getId())) {
                                throw new FieldValidationException(validation.getErrorMessage(), this);
                            }
                        }
                        break;

                    case ImageView:
                        if (mImageView.isShown())
                        {
                            if (mImageView.getDrawable() == null)
                            {
                                throw new FieldValidationException(validation.getErrorMessage(), this);
                            }

                            FileEntity obj = (FileEntity) mImageView.getTag();

                            if (obj == null)
                            {
                                throw new FieldValidationException(validation.getErrorMessage(), this);
                            }

                            if (!validation.isValid(obj.filename))
                            {
                                throw new FieldValidationException(validation.getErrorMessage(), this);
                            }
                        }
                        break;

                    case CheckBox:
                        //TODO
                        break;
                }
            }
        }
        return true;
    }

}