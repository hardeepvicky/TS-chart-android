package in.co.sevenrocks.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.LinkedHashMap;

/**
 * Created by Tech on 27-Aug-15.
 */
public class AsyncTaskHandler extends AsyncTask<String, Integer, String>
{
    public String msg;
    public long timeTaken;

    private LinkedHashMap data;

    public interface CallBack
    {
        public boolean doTask();
        public void finish(boolean status);
    }

    Context mContext;
    ProgressDialog mProgressDialog;
    CallBack caller;

    String title, subTitle;

    public AsyncTaskHandler(Context context,String title, String subTitle, CallBack caller)
    {
        this.mContext = context;
        this.caller = caller;

        this.title = title;
        this.subTitle = subTitle;
    }

    public AsyncTaskHandler(Context context, CallBack caller)
    {
        this(context, "", "", caller);
    }


    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        if (!title.isEmpty() && !subTitle.isEmpty())
        {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setTitle(title);
            mProgressDialog.setMessage(subTitle);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    @Override
    protected String doInBackground(String... urls)
    {
        String response = "0";

        long start = System.currentTimeMillis();

        response = caller.doTask() ? "1" : "0";

        long end = System.currentTimeMillis();

        timeTaken = ((end - start) / 1000);

        return response;
    }

    @Override
    protected void onProgressUpdate(Integer... values)
    {
        // TODO Auto-generated method stub
        super.onProgressUpdate(values);

        if (mProgressDialog != null) {
            mProgressDialog.setTitle(title + " (" + values[0] + "%)");
        }
    }

    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }

        caller.finish(result.equals("1"));
    }

    public void showProgress(Integer value)
    {
        publishProgress(value);
    }

    public void start()
    {
        execute(new String[]{""});
    }

    public void setTitle(String msg)
    {
        title = msg;
        publishProgress(null);
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public void setData(LinkedHashMap data)
    {
        this.data = data;
    }
}
