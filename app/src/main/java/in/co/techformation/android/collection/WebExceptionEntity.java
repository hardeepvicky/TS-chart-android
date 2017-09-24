package in.co.techformation.android.collection;

import in.co.techformation.android.Constant;


/**
 * Created by Tech on 31-Oct-15.
 */
public class WebExceptionEntity extends Exception
{
    public Constant.WebService.Codes errorCode;
    private String msg;

    public WebExceptionEntity(Constant.WebService.Codes errorCode, String msg)
    {
        this.errorCode = errorCode;
        this.msg = msg;

        if (this.msg.length() == 0)
        {
            switch (this.errorCode)
            {
                case SERVICE_NOT_FOUND:
                    this.msg = "Web Server is not responding or available";
                break;
            }
        }
    }

    public String getMessage()
    {
        return msg;
    }
}
