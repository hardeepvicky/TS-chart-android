package in.co.techformation.tschart.http;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.LinkedHashMap;

import in.co.techformation.android.Constant;
import in.co.techformation.android.AsyncTaskHandler;
import in.co.techformation.android.collection.LinkedHashMapHelper;
import in.co.techformation.android.collection.WebExceptionEntity;
import in.co.techformation.android.http.WebRequest;
import in.co.techformation.tschart.collection.LoginEntity;

/**
 * Created by Hardeep on 15-Sep-17.
 */

public class WebApi
{
    Context context;
    CallBack mCaller;
    LinkedHashMap responseData;
    AsyncTaskHandler wsTask;

    private static String TAG = "webservice";
    private String baseUrl = "http://192.168.1.100/Ts-chart-admin/web-api";

    /**
     * callbacks
     */
    public interface CallBack
    {
        void WebApiSuccess(LinkedHashMap data);
        void WebApiFailure(String msg);
    }

    public WebApi(Context context)
    {
        this.context = context;
    }

    public void login(LinkedHashMap data, CallBack callBack)
    {
        this.mCaller = callBack;

        data.put("service_name", "LOGIN");

        _sendWebRequest(data, "Sign In", "This may take few seconds...");
    }

    public void get_company_details(LinkedHashMap data, CallBack callBack)
    {
        this.mCaller = callBack;

        data.put("service_name", "GET_COMPANY_DETAILS");

        _sendWebRequest(data, "Verifying Code", "This may take few seconds...");
    }

    public void get_menu_reports(CallBack callBack)
    {
        this.mCaller = callBack;

        LinkedHashMap data = new LinkedHashMap();
        data.put("service_name", "GET_MENU_REPORTS");
        data.put("company_id", LoginEntity.getInstance(context).companyId);

        _sendWebRequest(data, "", "");
    }

    /**
     * Sends web request as per the provided request data and msg
     *
     * @param requestData
     * @param desc
     */
    private void _sendWebRequest(final LinkedHashMap requestData, String title, String desc)
    {
        wsTask = new AsyncTaskHandler(context, title,  desc, new AsyncTaskHandler.CallBack()
        {
            @Override
            public boolean doTask()
            {
                try
                {
                    responseData = _webResponse(_sendJSONRequest(requestData));
                }
                catch (WebExceptionEntity ex)
                {
                    Log.d(TAG, "Error msg : " + ex.getMessage());
                    wsTask.setMsg(ex.getMessage());
                    return false;
                }
                catch (Exception ex)
                {
                    Log.d(TAG, "Error msg : " + ex.getMessage());
                    wsTask.setMsg("Internal Error");
                    return false;
                }

                return true;
            }

            @Override
            public void finish(boolean status)
            {
                Log.d(TAG, "Time Taken : " + wsTask.timeTaken + " seconds");

                if (status)
                {
                    mCaller.WebApiSuccess(responseData);
                }
                else
                {
                    mCaller.WebApiFailure(wsTask.msg);
                }
            }
        });

        wsTask.start();
    }

    /**
     * Sends web request as per the provided request without callback
     * @paramrequestData
     */
    private String _sendJSONRequest(LinkedHashMap requestData) throws Exception
    {
        String request = LinkedHashMapHelper.toJSONObject(requestData).toString();

        Log.d(TAG, " request string : " + request);

        try
        {
            return WebRequest.sendJSONRequest(this.baseUrl, request);
        }
        catch(Exception ex)
        {
            Log.e(TAG, ex.getMessage());
            throw new WebExceptionEntity(Constant.WebService.Codes.SERVICE_NOT_FOUND, "");
        }
    }

    private LinkedHashMap _webResponse(String response) throws Exception
    {
        Log.d(TAG, " response string : " + response);

        LinkedHashMap data = LinkedHashMapHelper.fromJSONObject(new JSONObject(response));

        Log.d(TAG, " response data : " + data.toString());

        boolean result = String.valueOf(data.get("status")).equals("1");
        String service_name =  (String) data.get("service_name");

        if (result == true)
        {
            switch (service_name)
            {
                case "LOGIN":
                case "GET_COMPANY_DETAILS":
                case "GET_MENU_REPORTS":
                    return (LinkedHashMap) data.get("data");
            }
        }
        else
        {
            String msg = (String) data.get("msg");
            throw new WebExceptionEntity(Constant.WebService.Codes.SERVICE_RESULT_FALSE, msg);
        }

        return null;
    }
}
