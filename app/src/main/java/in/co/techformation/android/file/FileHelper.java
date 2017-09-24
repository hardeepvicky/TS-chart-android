package in.co.techformation.android.file;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import in.co.techformation.android.collection.FileEntity;


/**
 * Created by Tech on 10-Sep-15.
 */
public class FileHelper
{
    static int IMAGE_CODE_CAMERA = 101, IMAGE_CODE_GALLERY = 102;

    Activity mActivity;

    public String error_msg, path;
    public File file;

    ArrayList<String> ext;

    public FileHelper(Activity activity, ArrayList<String> ext)
    {
        mActivity = activity;
        this.ext = ext;
    }

    public void browseImage()
    {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        builder.setTitle("Get Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))
                {
                    getFromCamera();
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    getFromGallery(IMAGE_CODE_GALLERY);
                }
                else if (options[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }

        });

        builder.show();
    }

    public void getFromCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File f = new File(path, "temp.jpg");

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

        mActivity.startActivityForResult(intent, IMAGE_CODE_CAMERA);
    }

    public void getFromGallery(int code)
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mActivity.startActivityForResult(intent, code);
    }

    public void getFromGalleryOnResult(Intent i) throws Exception
    {
        Uri selectedImage = i.getData();
        String[] filePath = {MediaStore.Images.Media.DATA};

        Cursor c =  mActivity.getContentResolver().query(selectedImage, filePath, null, null, null);

        c.moveToFirst();

        String filename = c.getString(c.getColumnIndex(filePath[0]));

        c.close();

        if (ext.contains(getExtenstion(filename)))
        {
            file = new File(filename);
        }
        else
        {
            throw new Exception("Please select " + android.text.TextUtils.join(" / ", ext) + " file only");
        }
    }

    public boolean onResult(int requestCode, Intent i)
    {
        file = null;

        if (requestCode == IMAGE_CODE_GALLERY)
        {
            Uri selectedImage = i.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};

            Cursor c =  mActivity.getContentResolver().query(selectedImage, filePath, null, null, null);

            c.moveToFirst();

            String filename = c.getString(c.getColumnIndex(filePath[0]));

            c.close();

            if (ext.contains(getExtenstion(filename)))
            {
                file = new File(filename);
            }
            else
            {
                error_msg = "Please select " + android.text.TextUtils.join(" / ", ext) + " file only";
            }
        }

        if (file == null)
        {
            return false;
        }

        Log.d("FileHelper file", file.getAbsolutePath());
        return true;
    }


    public static void setImage(ImageView imageView, FileEntity obj)
    {
        Bitmap bitmap = (BitmapFactory.decodeFile(obj.filepath + obj.filename));
        imageView.setImageBitmap(bitmap);
        imageView.setTag(obj);
    }

    public static void copy(File src, File dst) throws IOException
    {
        File dir = new File(dst.getParent());

        if (!dir.exists())
        {
            Log.d("fileHelper", "creating folders");
            dir.mkdirs();
        }

        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static void copy(String src, String dst, String file) throws IOException
    {
        File in_dir = new File(src);
        File out_dir = new File(dst);

        if (!out_dir.exists())
        {
            Log.d("fileHelper", "creating folders");
            out_dir.mkdirs();
        }

        InputStream in = new FileInputStream(in_dir.getPath() + file);
        OutputStream out = new FileOutputStream(out_dir.getPath() + file);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static void copyFromAssets(Context mContext, String filename, File dst) throws IOException
    {
        InputStream is = mContext.getAssets().open(filename);

        OutputStream os = new FileOutputStream(dst.toString(), false);

        byte[] buffer = new byte[1024];

        while (is.read(buffer) > 0) {
            os.write(buffer);
        }

        os.flush();
        os.close();
        is.close();
    }

    public static File getNextAutoincrementFilename(File file, int count)
    {
        if (file.exists())
        {
            String filename = file.getParent() + getFileName(file.getName()) + "_" + String.valueOf(count) + "." + getExtenstion(file.getName());
            File f = new File(filename);
            getNextAutoincrementFilename(f, count + 1);
        }

        return file;
    }

    public static String getExtenstion(String filename)
    {
        String extension = "";

        int i = filename.lastIndexOf('.');
        if (i > 0) {
            extension = filename.substring(i+1);
        }

        return extension;
    }

    public static String getFileName(String filename)
    {
        int i = filename.lastIndexOf('.');

        if (i > 0)
        {
            filename = filename.substring(0, i - 1);
        }

        return filename;
    }

    public static String encodeBase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
