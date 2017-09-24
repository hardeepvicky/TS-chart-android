package in.co.techformation.android.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.LinkedHashMap;


/**
 * Created by Tech on 30-Jul-15.
 */
public class Sqlite
{
    public SQLiteDatabase db;

    private static final String TAG = "DB-sqlite";

    public Sqlite(String path)
    {
        db = SQLiteDatabase.openOrCreateDatabase(path, null);

        if (db.isOpen())
        {
            Log.d("database", "database open");
        }
        else
        {
            Log.d("database", "database closed");
        }
    }

    public boolean query (String q)
    {
        Log.d(TAG, q);
        db.execSQL(q);
        return true;
    }

    public LinkedHashMap getRecords(String q)
    {
        Log.d(TAG, q);
        Cursor cursor = db.rawQuery(q, null);

        LinkedHashMap data  = new LinkedHashMap();
        LinkedHashMap row;

        if (cursor.moveToFirst())
        {
            int c = 0;
            do {
                row = new LinkedHashMap();

                for(int i = 0; i < cursor.getColumnCount(); i++)
                {
                    row.put(cursor.getColumnName(i), cursor.getString(i));
                }
                //Log.d("database table", row.toString());
                data.put(c, row);
                c++;
            } while (cursor.moveToNext());
        }
        return data;
    }

    public void transactionBegin()
    {
        query("BEGIN;");
    }

    public void transactionCommit()
    {
        query("COMMIT;");
    }

    public void transactionRollBack()
    {
        query("ROLLBACK;");
    }
}
