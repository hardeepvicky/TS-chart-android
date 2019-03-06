package in.co.sevenrocks.android.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * created 06-03-2019
 * author Hardeep
 */
public class WebRequest
{
	public static String sendJSONRequest(String request_url, String params) throws Exception
	{
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(request_url);

		StringEntity se = new StringEntity(params);
		httpPost.setEntity(se);

		httpPost.setHeader("Content-type", "application/json");

		// 8. Execute POST request to the given URL
		HttpResponse httpResponse = httpclient.execute(httpPost);

		InputStream inputStream = httpResponse.getEntity().getContent();

		// 10. convert inputstream to string
		if (inputStream != null) {
			return convertStreamToString(inputStream);
		}

		return "";
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try
		{
			while ((line = reader.readLine()) != null)
			{
				sb.append((line + "\n"));
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
