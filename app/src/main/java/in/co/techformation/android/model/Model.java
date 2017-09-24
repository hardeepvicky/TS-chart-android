package in.co.techformation.android.model;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import in.co.techformation.android.collection.*;
import in.co.techformation.android.database.Sqlite;
import in.co.techformation.android.Constant;
import in.co.techformation.android.Util;
import in.co.techformation.android.file.FileHelper;

/**
 * Created by Tech on 31-Jul-15.
 */

public class Model
{
    private static final String TAG = "model";
    private static final String DATABASE_NAME = "database.sqlite";

    protected static final String FIELD_PRIMARY_KEY = "id";
    protected static final String FIELD_CREATED = "created";
    protected static final String FIELD_MODIFIED = "updated";

    protected static Sqlite DB;

    public String Table_Name;
    public LinkedHashMap data;

    Context mContext;

    public HashMap<String, String> Fields;
    public LinkedHashMap dateFields;
    public LinkedHashMap fileFields;
    public ArrayList<String> booleanFields;

    public String ID;

    protected boolean eventBeforeInsert, eventBeforeUpdate, eventAfterInsert, eventAfterUpdate;
    protected boolean eventAfterGet, eventAfterDelete, eventBeforeDelete;

    public Model(Context context, String table_name)
    {
        mContext = context;

        if (DB == null)
        {
            File dbFile = mContext.getDatabasePath(DATABASE_NAME);

            if (!dbFile.exists() || Constant.DEBUG)
            {
                try
                {
                    _copyDatabase(dbFile);
                }
                catch (IOException e)
                {
                    throw new RuntimeException("DL Error creating source database " + e.toString(), e);
                }
            }

            DB = new Sqlite(dbFile.getPath());
        }

        Table_Name = table_name;
        Fields = new HashMap<>();
        dateFields = new LinkedHashMap();
        fileFields = new LinkedHashMap();
        booleanFields = new ArrayList<>();
        ID = "";

        eventBeforeInsert = eventBeforeUpdate = eventAfterGet = eventAfterInsert = eventAfterUpdate = eventAfterDelete = eventBeforeDelete = true;

        Log.d(TAG, "initialized " + Table_Name);
    }

    public Sqlite getDB()
    {
        return DB;
    }


    /**
     * function called before update the record
     * @return
     */
    protected boolean beforeUpdate() throws Exception
    {
        if (FIELD_MODIFIED != null && !data.containsKey(FIELD_MODIFIED))
        {
            data.put(FIELD_MODIFIED, Util.getCurrentDate(Constant.Datetime.SQL_FORMAT));
        }

        return beforeSave();
    }

    /**
     * function called before Insert the record
     * @return
     */
    protected boolean beforeInsert() throws Exception
    {
        if (FIELD_CREATED != null && !data.containsKey(FIELD_CREATED))
        {
            data.put(FIELD_CREATED, Util.getCurrentDate(Constant.Datetime.SQL_FORMAT));
        }

        return beforeSave();
    }

    protected boolean beforeSave() throws Exception
    {
        convertDateTime(data, true);
        saveFiles(data);
        convertBooleanFields(booleanFields);

        return true;
    }

    protected boolean beforeDelete(int id)
    {
        return true;
    }

    /**
     * function called after insert
     * @param id
     */
    protected void afterInsert(int id)
    {
        ID = String.valueOf(id);
    }

    /**
     * function called after update
     * @param id
     */
    protected void afterUpdate(int id)
    {
        ID = String.valueOf(id);
    }

    protected void afterDelete(int id)
    {
        ID = String.valueOf(id);
    }

    /**
     * function called after getting records
     * @param records
     * @return
     */
    protected void afterGet(LinkedHashMap records) throws Exception
    {
        Set set = records.entrySet();
        Iterator itr = set.iterator();

        while(itr.hasNext())
        {
            Map.Entry row = (Map.Entry)itr.next();
            LinkedHashMap record = (LinkedHashMap) row.getValue();

            //for date time conversion
            convertDateTime(record, false);

            //for getting full path with filename
            getFiles(record);
        }
    }

    /**
     * function to convert datetime format
     * @param record
     */
    protected void convertDateTime(LinkedHashMap record, boolean for_save) throws Exception
    {
        if (dateFields.size() > 0)
        {
            Set set = dateFields.entrySet();
            Iterator itr = set.iterator();

            while(itr.hasNext())
            {
                Map.Entry row = (Map.Entry)itr.next();

                if (row.getValue() != null && record.containsKey(row.getKey()) && record.get(row.getKey()) != null)
                {
                    String format = String.valueOf(row.getValue());
                    String val = ((String) record.get(row.getKey())).trim();

                    if (val.length() > 0) {
                        if (for_save) {
                            val = Util.convertDateFormat(val, format, Constant.Datetime.SQL_FORMAT);
                            Log.d(TAG, "datetime : " + row.getKey() + " to " + val + " : " + format + " to " + Constant.Datetime.SQL_FORMAT);
                        } else {
                            val = Util.convertDateFormat(val, Constant.Datetime.SQL_FORMAT, format);
                            Log.d(TAG, "datetime : " + row.getKey() + " to " + val + " : " + Constant.Datetime.SQL_FORMAT + " to " + format);
                        }

                        record.put(row.getKey(), val);
                    }
                }

            }
        }
    }


    protected void convertBooleanFields(ArrayList<String> fields)
    {
        for(String f : fields){
            if(data.containsKey(f))
            {
                if(data.get(f).toString().toLowerCase().equals("true"))
                    data.put(f,1);
                else
                    data.put(f,0);
            }
        }
    }

    /**
     * function save file
     * @param record
     * @throws Exception
     */
    protected void saveFiles(LinkedHashMap record) throws Exception
    {
        if (fileFields.size() > 0)
        {
            Set set = fileFields.entrySet();
            Iterator itr = set.iterator();

            while(itr.hasNext())
            {
                Map.Entry row = (Map.Entry)itr.next();

                if (row.getValue() != null && record.containsKey(row.getKey()))
                {
                    FileEntity fileObj = (FileEntity) record.get(row.getKey());

                    if (fileObj.filename.length() > 0 && fileObj.willSave)
                    {
                        FileRuleEntity fileRule = (FileRuleEntity) row.getValue();

                        File src = new File(fileObj.filepath, fileObj.filename);
                        File dst = null;
                        String filename = "";

                        if (fileRule.autoIncreament)
                        {
                            if (record.containsKey(FIELD_PRIMARY_KEY) && record.get(FIELD_PRIMARY_KEY) != null && record.get(FIELD_PRIMARY_KEY).toString().trim().length() > 0)
                            {
                                filename = record.get(FIELD_PRIMARY_KEY).toString().trim();
                            }
                            else
                            {
                                if (ID.length() > 0)
                                {
                                    filename = ID;
                                }
                                else
                                {
                                    filename = String.valueOf(getNextId());
                                }
                            }

                            filename += "." + FileHelper.getExtenstion(src.getName());

                            dst = new File(fileRule.path, filename);
                        }
                        else
                        {
                            dst = new File(fileRule.path, fileRule.filePrefix + "." + FileHelper.getExtenstion(fileObj.filename));

                            dst = FileHelper.getNextAutoincrementFilename(dst, 0);
                        }

                        //Log.d("File copy", src.getAbsolutePath() + " to " + dst.getAbsolutePath());
                        FileHelper.copy(src, dst);

                        record.put(row.getKey(), dst.getName());
                    }
                    else
                    {
                        record.remove(row.getKey());
                    }
                }

            }
        }
    }

    protected void getFiles(LinkedHashMap record) throws Exception
    {
        if (fileFields.size() > 0)
        {
            Set set = fileFields.entrySet();
            Iterator itr = set.iterator();

            while(itr.hasNext())
            {
                Map.Entry row = (Map.Entry)itr.next();

                if (row.getValue() != null && record.containsKey(row.getKey()) && record.get(row.getKey()) != null)
                {
                    FileRuleEntity fileRule = (FileRuleEntity) row.getValue();

                    String filename = ((String) record.get(row.getKey())).trim();

                    if (filename.length() > 0)
                    {
                        record.put(row.getKey(), new FileEntity(fileRule.path, filename, false));
                    }
                }

            }
        }
    }

    /* --------------------------------------------------------------- */
    /* ----------------------- Insert -------------------------------- */

    public boolean save(LinkedHashMap record) throws Exception
    {
        if (ID.length() > 0)
        {
            return update(record, Integer.parseInt(ID));
        }
        else
        {
            return insert(record);
        }
    }

    public void create()
    {
        ID = "";
    }

    protected boolean insert(LinkedHashMap record) throws Exception
    {
        data = record;

        if (eventBeforeInsert) {
            if (!this.beforeInsert()) {
                return false;
            }
        }

        String q = "INSERT INTO " + Table_Name;
        String q_fields = "", q_values = "";

        Set set = data.entrySet();

        Iterator itr = set.iterator();

        while (itr.hasNext())
        {
            Map.Entry row = (Map.Entry) itr.next();

            q_fields += row.getKey().toString() + ",";
            q_values += "'" + sanitize(row.getValue().toString()) + "',";

        }

        if (q_fields.length() == 0) {
            return false;
        }

        q_fields = q_fields.substring(0, q_fields.length() - 1);
        q_values = q_values.substring(0, q_values.length() - 1);

        q += "(" + q_fields + ")VALUES(" + q_values + ")";

        DB.query(q);

        if (eventAfterInsert)
        {
            afterInsert(getLastInsertId());
        }

        return true;
    }

    /**
     *
     * @LinkedHashMap records
     * @return number of record inserted
     */
    public int insertMany(LinkedHashMap records)
    {
        int count = 0;

        eventAfterInsert = false;
        try
        {
            Set set = records.entrySet();

            Iterator itr = set.iterator();

            while (itr.hasNext())
            {
                Map.Entry row = (Map.Entry) itr.next();

                if (insert((LinkedHashMap) row.getValue()))
                {
                    count++;
                }
            }
        }
        catch(Exception ex)
        {
            Log.d(TAG, ex.toString());
            count = -1;
        }

        eventAfterInsert = true;

        return count;
    }

    /* --------------------------------------------------------------- */
    /* ----------------------- Update -------------------------------- */

    protected boolean update(LinkedHashMap record, String where)  throws Exception
    {
        data = record;

        if (eventBeforeUpdate) {
            if (!this.beforeUpdate()) {
                return false;
            }
        }

        String values = "";

        Set set = data.entrySet();
        Iterator itr = set.iterator();

        while(itr.hasNext())
        {
            Map.Entry row = (Map.Entry)itr.next();

            values += row.getKey().toString() + "=" + "'" + sanitize(row.getValue().toString()) + "',";
        }

        if (values.length() == 0)
        {
            return false;
        }

        values = values.substring(0, values.length() - 1);

        String q = "UPDATE " + Table_Name + " SET " + values + where;

        DB.query(q);

        return true;
    }

    /**
     * update the record on given id
     * @param record
     * @param  id
     * @return
     */
    public boolean update(LinkedHashMap record, int id) throws Exception
    {
        String where = " WHERE id=" + id;

        update(record, where);

        if(eventAfterUpdate)
        {
            afterUpdate(id);
        }

        return true;
    }


    /* --------------------------------------------------------------- */
    /* ----------------------- Select -------------------------------- */
    protected String getRecordFields(String fields, HashMap<String, String> Fields)
    {
        if ((fields.length() == 0 && Fields.size() > 0) || fields.equals("*"))
        {
            fields = "*";

            if (Fields.size() > 0)
            {
                String str = "";

                for (Map.Entry obj : Fields.entrySet())
                {
                    if (obj.getKey() != null)
                    {
                        str += "(" + obj.getValue() + ") AS " + obj.getKey() + ",";
                    }
                    else
                    {
                        str += obj.getValue() + ",";
                    }
                }

                fields = "*," + str.substring(0, str.length() - 1);
            }

        }
        else if (fields.length() == 0)
        {
            fields = "*";
        }
        else
        {
            String[] f = fields.split(",");

            fields = "";
            for (String s : f)
            {
                s = s.trim();
                for (Map.Entry obj : Fields.entrySet())
                {
                    if (obj.getKey().toString().equals(s))
                    {
                        s = "(" + obj.getValue() + ") AS " + obj.getKey();
                    }
                }

                fields += s + ",";
            }

            fields = fields.substring(0, fields.length() - 1);
        }

        return fields;
    }


    /**
     * main function for creating query;
     * @return query string
     */
    protected String getRecordQuery(String fields, String where, String group_by, String order, String limit)
    {
        return "SELECT " + fields + " FROM " + Table_Name + _addSpace(where) + _addSpace(group_by) + _addSpace(order) + _addSpace(limit);
    }

    public LinkedHashMap getRecords()
    {
        return getRecords(getRecordFields("", Fields), "", "", "", "");
    }
    public LinkedHashMap getRecords(String fields)
    {
        return getRecords(getRecordFields(fields, Fields), "", "", "", "");
    }

    public LinkedHashMap getRecords(ArrayList<String> fields)
    {
        return getRecords(getRecordFields(android.text.TextUtils.join(",", fields), Fields), "", "", "", "");
    }

    public LinkedHashMap getRecords(ArrayList<String> fields, String where, String order)
    {
        return getRecords(getRecordFields(android.text.TextUtils.join(",", fields), Fields), where, "", order, "");
    }

    public LinkedHashMap getRecords(ArrayList<String> fields, String where, String group_by, String order, String limit)
    {
        return getRecords(getRecordFields(android.text.TextUtils.join(",", fields), Fields), where, group_by, order, limit);
    }

    public LinkedHashMap getRecords(HashMap<String, String> fields)
    {
        return getRecords(getRecordFields("", fields), "", "", "", "");
    }

    public LinkedHashMap getRecords(HashMap<String, String> fields, String where, String order)
    {
        return getRecords(getRecordFields("", fields), where, "", order, "");
    }

    public LinkedHashMap getRecords(HashMap<String, String> fields, String where, String group_by, String order, String limit)
    {
        return getRecords(getRecordFields("", fields), where, group_by, order, limit);
    }

    public LinkedHashMap getRecords(String fields, String where, String order)
    {
        return getRecords(getRecordFields(fields, Fields), where, "", order, "");
    }

    public LinkedHashMap getRecords(String fields, String where, String group_by, String order, String limit)
    {
        String q = getRecordQuery(fields, where, group_by, order, limit);

        LinkedHashMap records = DB.getRecords(q);

        if (eventAfterGet)
        {
            try
            {
                afterGet(records);
            }
            catch(Exception ex)
            {
                Log.d(TAG, ex.toString());
                throw new RuntimeException("DL Error " + ex.toString(), ex);
            }
        }

        Log.d(TAG, records.toString());

        return records;
    }

    public LinkedHashMap queryRecords(String q)
    {
        return DB.getRecords(q);
    }

    public String getRecordField(String field, String where)
    {
        return getRecordField(field, where, "", "");
    }

    public String getRecordField(String field, String where, String order, String limit)
    {
        LinkedHashMap records = getRecords(getRecordFields(field, Fields), where, "", order, limit);

        if (records.size() > 0)
        {
            return ((LinkedHashMap) records.get(0)).get(field).toString();
        }

        return "";
    }


    /**
     * @return count of records
     */
    public int getCount()
    {
        return getCount("");
    }

    /**
     * get count
     * @String where
     * @return count of records
     */
    public int getCount(String where)
    {
        String q = "SELECT COUNT(*) record_count FROM " + Table_Name + _addSpace(where);

        LinkedHashMap records = DB.getRecords(q);

        LinkedHashMap record = (LinkedHashMap) records.get(0);

        Integer count = Integer.parseInt((String) record.get("record_count"));

        return count;
    }

    /**
     * get Last Inserted id
     * @return
     */
    public int getLastInsertId()
    {
        String q = "SELECT last_insert_rowid() as last_id ;";

        LinkedHashMap records = DB.getRecords(q);

        LinkedHashMap record = (LinkedHashMap) records.get(0);

        return Integer.parseInt((String) record.get("last_id"));
    }

    /**
     * get next auto increment id
     * @return
     */
    public int getNextId()
    {
        LinkedHashMap records = DB.getRecords("SELECT * FROM SQLITE_SEQUENCE where name='" + Table_Name + "'");

        LinkedHashMap record = (LinkedHashMap) records.get(0);

        return Integer.parseInt((String) record.get("seq")) + 1;
    }

    public ArrayList<Entity> getAdapterArrayList(String key_field, String value_field, String where, String order, String default_value)
    {
        String q = "SELECT " +  getRecordFields(key_field + "," + value_field, Fields) + " FROM " + Table_Name + _addSpace(where) +  " GROUP BY " + key_field + _addSpace(order);

        LinkedHashMap records = DB.getRecords(q);

        if (eventAfterGet)
        {
            try {
                afterGet(records);
            }
            catch(Exception ex)
            {
                Log.d(TAG, ex.toString());
                throw new RuntimeException("DL Error" + ex.toString(), ex);
            }
        }

        Log.d(TAG, " select : " + records.toString());

        LinkedHashMap record;

        ArrayList<Entity> list = new ArrayList<Entity>();

        Set set = records.entrySet();
        Iterator itr = set.iterator();

        if (default_value.length() > 0) {
            list.add(new Entity("", default_value));
        }

        while(itr.hasNext())
        {
            Map.Entry row = (Map.Entry)itr.next();

            record = (LinkedHashMap) row.getValue();

            list.add(new Entity(record.get(key_field), record.get(value_field)));
        }

        return list;
    }

    public ArrayList<String> getArrayList(String field, String where, String order)
    {
        String q = "SELECT " + getRecordFields(field, Fields) + " FROM " + Table_Name + _addSpace(where)  + _addSpace(order);

        LinkedHashMap records = DB.getRecords(q);

        if (eventAfterGet)
        {
            try {
                afterGet(records);
            }
            catch(Exception ex)
            {
                Log.d("DL", ex.toString());
                throw new RuntimeException("DL Error" + ex.toString(), ex);
            }
        }

        Log.d(TAG, " select : " + records.toString());

        LinkedHashMap record;

        ArrayList<String> list = new ArrayList<String>();

        Set set = records.entrySet();
        Iterator itr = set.iterator();

        while(itr.hasNext())
        {
            Map.Entry row = (Map.Entry)itr.next();

            record = (LinkedHashMap) row.getValue();

            list.add(record.get(field).toString());
        }

        return list;
    }


    /**------------------- delete -------------------------------------- */

    public boolean delete(int id)
    {
        if (beforeDelete(id))
        {
            boolean result = delete("WHERE " + FIELD_PRIMARY_KEY + "=" + String.valueOf(id));

            if (result)
            {
                afterDelete(id);
            }

            return result;
        }

        return false;
    }

    public boolean delete(String where)
    {
        DB.query("DELETE FROM " + Table_Name + _addSpace(where));

        return true;
    }

    /**---------------- other ----------------------------------------- */

    public boolean truncate()
    {
        try
        {
            DB.query("DELETE FROM " + Table_Name);
        }
        catch (Exception ex)
        {
            Log.d("DL", ex.toString());
            return false;
        }

        return true;
    }


    /** ---------------- private functions ------------------------------ */

    private void _copyDatabase(File dbFile) throws IOException
    {
        Log.d(TAG, "copy database");

        File dir = new File(dbFile.getParent());

        if (!dir.exists())
        {
            Log.d(TAG, "creating  database folder");
            dir.mkdirs();
        }

        if(!dbFile.exists())
        {
            Log.d(TAG, "creating  database File");
            dbFile.createNewFile();
        }

        InputStream is = mContext.getAssets().open(DATABASE_NAME);

        OutputStream os = new FileOutputStream(dbFile, false);

        byte[] buffer = new byte[1024];

        //Log.d("DL", "copying file");

        while (is.read(buffer) > 0) {
            os.write(buffer);
        }

        os.flush();
        os.close();
        is.close();
    }


    private String _addSpace(String val)
    {
        if (val != "")
        {
            val = " " + val + " ";
        }

        return val;
    }

    public String sanitize(String str)
    {
        return str.replaceAll("[']", "");
    }
}