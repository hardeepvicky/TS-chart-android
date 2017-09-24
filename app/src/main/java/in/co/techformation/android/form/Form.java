package in.co.techformation.android.form;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import in.co.techformation.android.collection.Entity;
import in.co.techformation.android.collection.FileEntity;

/**
 * Created by Tech on 04-Aug-15.
 */

public class Form {

    private static final String TAG = "android-form";
    private List<Field> mFields = new ArrayList<Field>();
    private Activity mActivity;
    private validationPopulate showType;
    public HashMap<String, String> errorMsgs;

    public static enum validationPopulate
    {
        SHOW_TOAST, SHOW_TEXTBOX_ERROR, SHOW_LABEL_ERROR, SHOW_NONE
    }

    public Form(Activity activity, validationPopulate show_type)
    {
        this.mActivity = activity;
        showType = show_type;
    }

    public void addField(Field field) {
        mFields.add(field);
    }

    public boolean isValid()
    {
        boolean result = true;

        errorMsgs = new HashMap<>();

        if (showType == validationPopulate.SHOW_TEXTBOX_ERROR)
        {
            for (Field field : mFields)
            {
                if(field.fieldType == Field.FieldType.EditText)
                {
                    field.mEditText.setError(null);
                }
                else if(field.fieldType == Field.FieldType.Spinner)
                {
                    field.mSpinner.requestFocus();
                    TextView textView = (TextView)field.mSpinner.getSelectedView();
                    textView.setError(null);
                }
            }
        }

        for (Field field : mFields)
        {
            try
            {
                field.isValid();
            }
            catch (FieldValidationException e)
            {
                result = false;

                if (e.field.fieldType == Field.FieldType.EditText) {
                    if (showType == validationPopulate.SHOW_TOAST) {
                        showErrorMessage(e.getMessage());
                        break;
                    } else if (showType == validationPopulate.SHOW_TEXTBOX_ERROR) {
                        e.field.mEditText.requestFocus();
                        e.field.mEditText.selectAll();

                        FormUtils.showKeyboard(mActivity, e.field.mEditText);
                        e.field.mEditText.setError(e.getMessage());
                    }
                    else if (showType == validationPopulate.SHOW_LABEL_ERROR)
                    {

                    }
                    else
                    {
                        errorMsgs.put(e.field.fieldName, e.getMessage());
                    }
                }
                else if (e.field.fieldType == Field.FieldType.Spinner)
                {
                    if (showType == validationPopulate.SHOW_TOAST)
                    {
                        showErrorMessage(e.getMessage());
                        break;
                    }
                    else if (showType == validationPopulate.SHOW_TEXTBOX_ERROR)
                    {
                        e.field.mSpinner.requestFocus();
                        TextView textView = (TextView) e.field.mSpinner.getSelectedView();

                        FormUtils.showKeyboard(mActivity, e.field.mSpinner);
                        textView.setError(e.getMessage());
                    }
                    else if (showType == validationPopulate.SHOW_LABEL_ERROR)
                    {

                    }
                    else
                    {
                        errorMsgs.put(e.field.fieldName, e.getMessage());
                    }
                } else if (e.field.fieldType == Field.FieldType.ImageView) {
                    if (showType == validationPopulate.SHOW_TOAST) {
                        showErrorMessage(e.getMessage());
                        break;
                    }
                    else
                    {
                        errorMsgs.put (e.field.fieldName, e.getMessage());
                    }
                }
            }
        }

        return result;
    }

    public LinkedHashMap getData()
    {
        LinkedHashMap record = new LinkedHashMap();

        String value;
        for (Field field : mFields)
        {
            if (field.fieldName != "" && field.fieldName != null)
            {
                if (field.fieldType == Field.FieldType.TextView || field.fieldType == Field.FieldType.EditText || field.fieldType == Field.FieldType.Spinner)
                {
                    value = "";
                    if (field.fieldType == Field.FieldType.TextView)
                    {
                        value = field.mTextView.getText().toString();
                    }
                    else if (field.fieldType == Field.FieldType.EditText)
                    {
                        value = field.mEditText.getText().toString();
                    }
                    else if (field.fieldType == Field.FieldType.Spinner)
                    {
                        Entity obj = (Entity) field.mSpinner.getSelectedItem();
                        value = obj.getId();
                    }

                    record.put(field.fieldName, value);
                }
                else if (field.fieldType == Field.FieldType.ImageView)
                {
                    if (field.mImageView.getDrawable() != null)
                    {
                        FileEntity obj = (FileEntity) field.mImageView.getTag();
                        record.put(field.fieldName, obj);
                    }
                }
                else if (field.fieldType == Field.FieldType.CheckBox)
                {
                    record.put(field.fieldName, field.mCheckBox.isChecked());
                }
            }
        }

        Log.d(TAG, record.toString());

        return record;
    }

    public void fillData(LinkedHashMap record)
    {
        String value;
        for (Field field : mFields)
        {
            if (field.fieldName != "" || field.fieldName != null)
            {
                if (field.fieldType == Field.FieldType.TextView || field.fieldType == Field.FieldType.EditText || field.fieldType == Field.FieldType.Spinner)
                {
                    value = "";
                    try {

                        value = record.get(field.fieldName).toString();
                    }
                    catch(Exception e)
                    {
                        Log.d("Error form", field.fieldName + " not found in record");
                    }

                    value = value.trim();


                    if (field.fieldType == Field.FieldType.TextView)
                    {
                        field.mTextView.setText(value);
                    }
                    else if (field.fieldType == Field.FieldType.EditText)
                    {
                        field.mEditText.setText(value);
                    }
                    else if (field.fieldType == Field.FieldType.Spinner)
                    {
                        field.mSpinner.setSelection(Integer.parseInt(value));
                    }
                }
                else if (field.fieldType == Field.FieldType.ImageView)
                {
                    if (field.fieldName != null && record.containsKey(field.fieldName) && record.get(field.fieldName) != null)
                    {
                        FileEntity obj = (FileEntity) record.get(field.fieldName);

                        Bitmap thumbnail = BitmapFactory.decodeFile((new File(obj.filepath, obj.filename)).getPath());

                        field.mImageView.setImageBitmap(thumbnail);
                    }
                }
                else if (field.fieldType == Field.FieldType.CheckBox)
                {
                    //todo
                }
            }
        }
    }

    public void clear()
    {
        for (Field field : mFields)
        {
            if (field.fieldName != "" && field.fieldName != null)
            {
                if (field.fieldType == Field.FieldType.TextView)
                {
                    field.mTextView.setText("");
                }
                else if (field.fieldType == Field.FieldType.EditText)
                {
                    field.mEditText.setText("");
                }
                else if (field.fieldType == Field.FieldType.Spinner)
                {
                    field.mSpinner.setSelection(0);
                }
                else if (field.fieldType == Field.FieldType.ImageView)
                {
                    field.mImageView.setImageDrawable(null);
                    field.mImageView.setTag(null);
                }
            }
        }
    }

    protected void showErrorMessage(String message) {
        // Crouton.makeText(mActivity, message, Style.ALERT).show();
        Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
    }
}
