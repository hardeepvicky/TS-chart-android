package in.co.sevenrocks.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import in.co.sevenrocks.android.collection.*;


/**
 * Util - Common
 *
 * Package: com.cscegovindia.tablet.utils
 * Project: CSC
 * Created by: Gagandeep Gambhir
 * Created on: 24/01/15, 4:35 PM
 */
public class Util
{
    Context mContext;

    public Util(Context context)
    {
        mContext = context;
    }

    static Calendar mCalendar = Calendar.getInstance();
    //static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(Constants.DATE_DD_MMM_YYYY_HH_MIN_SS);

    public static void showErrorToast(Context context, String msg) {
        //Log.d("Log Error:", msg);
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static String getCurrentDate(String format)
    {
        SimpleDateFormat sdfDate = new SimpleDateFormat(format);
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public static String convertDateFormat(String str, String in_format, String out_format)
    {
        SimpleDateFormat inFormat = new SimpleDateFormat(in_format);
        SimpleDateFormat outFormat = new SimpleDateFormat(out_format);
        try
        {
            Date convertedDate = inFormat.parse(str);
            str = outFormat.format(convertedDate);

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return str;
    }


    public static Date convertStringToDate(String str, String in_format)
    {
        SimpleDateFormat inFormat = new SimpleDateFormat(in_format);
        Date convertedDate = null;
        try
        {
            convertedDate = inFormat.parse(str);


        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return convertedDate;
    }

    public static long getMillisecond(String date, String format) {

        return convertStringToDate(date, format).getTime();
    }

   public static String convertDateFormat(Date date, String format)
    {
        SimpleDateFormat outFormat = new SimpleDateFormat(format);
        return outFormat.format(date);
    }

    public static ArrayList<Entity> getSpinnerList(String[] list)
    {
        ArrayList<Entity> arrList = new ArrayList<Entity>();

        arrList.add(new Entity("", "Please Select"));

        for (int i = 0; i < list.length; i++)
        {
            arrList.add( new Entity(i, list[i]));
        }

        return arrList;
    }

    public static ArrayList<Entity> getSpinnerEmptyList()
    {
        ArrayList<Entity> arrList = new ArrayList<Entity>();
        arrList.add(new Entity("", "Please Select"));
        return arrList;
    }

    public static String getFromAadapterList(ArrayList<Entity>  list, String key, String value)
    {
        for (int i = 0; i < list.size(); i++)
        {
            if (key != "" && list.get(i).getId().equals(key))
            {
                return list.get(i).getName();
            }
            else if (value != "" && list.get(i).getName().equals(value))
            {
                return list.get(i).getId();
            }
        }

        return "";
    }

    public static String getULTag(ArrayList<String> list)
    {
        String str = "";
        int i = 1;
        for (String s : list)
        {
            str += "  " + s;
            if (i < list.size())
            {
                str += "\n";
            }
            i++;
        }

        return str;
    }

    public static ArrayList<String> getListFromHaspMap(HashMap mp, String find)
    {
        ArrayList<String> list = new ArrayList<>();

        Iterator it = mp.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();
            if (find.equals("key"))
            {
                list.add(pair.getKey().toString());
            }
            else
            {
                list.add(pair.getValue().toString());
            }
        }

        return list;
    }

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
}
