package in.co.techformation.android.collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Tech on 09-Sep-15.
 */
public class LinkedHashMapHelper
{
    public static LinkedHashMap fromJSONObject(JSONObject json)  throws JSONException
    {
        LinkedHashMap data = new LinkedHashMap();

        try
        {
            Iterator<?> keys = json.keys();

            while( keys.hasNext() )
            {
                String key = (String)keys.next();

                Object obj = json.get(key);

                if ( obj instanceof JSONObject )
                {
                    data.put(key, fromJSONObject((JSONObject) obj));
                }
                else if ( obj instanceof JSONArray)
                {
                    data.put(key, fromJSONArray((JSONArray) obj));
                }
                else if (obj instanceof String)
                {
                    if (obj.toString().equals("{}") || obj.toString().equals("[]"))
                    {
                        data.put(key, new LinkedHashMap<>());
                    }
                    else
                    {
                        data.put(key, obj);
                    }
                }
                else
                {
                    data.put(key, obj);
                }
            }

        }
        catch (Exception ex)
        {
            throw new JSONException(ex.toString());
        }

        return data;
    }

    public static LinkedHashMap fromJSONArray(JSONArray json) throws JSONException
    {
        LinkedHashMap data = new LinkedHashMap();

        try
        {
            for (int i = 0; i < json.length(); i++)
            {
                Object obj = json.get(i);

                if ( obj instanceof JSONObject )
                {
                    data.put(i, fromJSONObject((JSONObject) obj));
                }
                else if ( obj instanceof JSONArray)
                {
                    data.put(i, fromJSONArray((JSONArray) obj));
                }
                else if (obj instanceof String)
                {
                    data.put(i, obj);
                }
                else
                {
                    data.put(i, obj);
                }
            }

        }
        catch (JSONException ex)
        {
            throw new JSONException(ex.toString());
        }

        return data;
    }

    public static JSONObject toJSONObject(LinkedHashMap data)  throws JSONException
    {
        JSONObject json = new JSONObject();

        try
        {
            Set set = data.entrySet();
            Iterator itr = set.iterator();

            while(itr.hasNext())
            {
                Map.Entry row = (Map.Entry)itr.next();

                Object obj = row.getValue();

                if ( obj instanceof LinkedHashMap )
                {
                    Set set2 = ((LinkedHashMap) obj).entrySet();
                    Iterator itr2 = set2.iterator();

                    boolean json_array = true;

                    try {
                        while (itr2.hasNext())
                        {
                            Map.Entry row2 = (Map.Entry) itr2.next();
                            Integer.parseInt(String.valueOf(row2.getKey()));
                        }
                    }
                    catch(Exception e)
                    {
                        json_array = false;
                    }

                    if (json_array)
                    {
                        json.put(String.valueOf(row.getKey()), (Object) toJSONArray((LinkedHashMap) obj));
                    }
                    else
                    {
                        json.put(String.valueOf(row.getKey()), (Object) toJSONObject((LinkedHashMap) obj));
                    }
                }
                else
                {
                    json.put(String.valueOf(row.getKey()), obj);
                }
            }

        }
        catch (JSONException ex)
        {
            throw new JSONException(ex.toString());
        }

        return json;
    }

    public static JSONArray toJSONArray(LinkedHashMap data)  throws JSONException
    {
        JSONArray json = new JSONArray();

        try
        {
            Set set = data.entrySet();
            Iterator itr = set.iterator();

            while(itr.hasNext())
            {
                Map.Entry row = (Map.Entry)itr.next();

                Object obj = row.getValue();

                if ( obj instanceof LinkedHashMap)
                {
                    json.put((Object) toJSONObject((LinkedHashMap) obj));
                }
                else
                {
                    json.put(String.valueOf(obj));
                }
            }

        }
        catch (JSONException ex)
        {
            throw new JSONException(ex.toString());
        }

        return json;
    }

    public static LinkedHashMap merge(LinkedHashMap arr1, LinkedHashMap arr2)
    {
        Set set = arr2.entrySet();
        Iterator itr = set.iterator();

        while (itr.hasNext())
        {
            Map.Entry row = (Map.Entry) itr.next();

            String val = row.getValue().toString();
            String key = row.getKey().toString();

            arr1.put(key, val);
        }

        return arr1;
    }
}
