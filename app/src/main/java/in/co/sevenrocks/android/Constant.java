package in.co.sevenrocks.android;

public class Constant
{
	public static boolean DEBUG = false;

    public static String FONT_AWESOME = "fonts/fontawesome-webfont.ttf";

    public static class Error
    {
        public static final String GENERAL = "Internal Error";
        public static final String DATABASE_GENERAL = "Database Error";
        public static final String DATABASE_SAVE_FAILED = "Failed to save data";
        public static final String NO_INTERNET_MSG = "No Internet Connection Available";
    }

    public static class Pref
    {
        public static final String key = "App";
    }

    public static class Date
    {
        public static String FORMAT = "dd-MMM-yyyy";
        public static String SQL_FORMAT = "yyyy-MMM-dd";
        public static String WEB_SERVICE_FORMAT = "yyyy-MM-dd";
    }

    public static class Datetime
    {
        public static String FORMAT = "dd-MM-yyyy hh:mm a";
        public static String SQL_FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static String WEB_SERVICE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    }

    public static class Time
    {
        public static String FORMAT = "hh:mm a";
        public static String SQL_FORMAT = "HH:mm:ss";
        public static String WEB_SERVICE_FORMAT = "HH:mm:ss";
    }

    public static class WebService
    {
        public static enum  Codes
        {
            ERROR, SUCCESS, INTERNET_FAILURE,
            SERVICE_NOT_FOUND, SERVICE_ERROR,
            SERVICE_RESULT_FALSE
        }
    }
}