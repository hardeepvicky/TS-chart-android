package in.co.techformation.tschart.config;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by Tech on 09-Oct-15.
 */
public class Constant extends in.co.techformation.android.Constant
{
    public static final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
    public static final String FONT_AWESOME = "fonts/fontawesome-webfont.ttf";

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static class Pref
    {
        public static final String key = "Attendance";
    }

    public static class WebService extends in.co.techformation.android.Constant.WebService
    {
        public static final String URL = "http://attendance.techformation.co.in/web-api";
        public static final String LOGIN = "login";
        public static final String ATTENDANCE_IN = "attendance_in";
        public static final String ATTENDANCE_OUT = "attendance_out";
        public static final String ATTENDANCE = "attendance";
        public static final String GCM_REG = "gcm_registration";
    }
}
