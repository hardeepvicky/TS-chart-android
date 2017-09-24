package in.co.techformation.android.http;

import android.app.ProgressDialog;
import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.LinkedHashMap;

/**
 * created 15-09-2017
 * author Hardeep
 */
public class WebRequest
{
	public static String sendJSONRequest(String request_url, String params) throws Exception
	{
		URL url = new URL(request_url);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

		urlConn.setRequestMethod("POST");
		urlConn.setRequestProperty("Content-Type", "applicaiton/json; charset=utf-8");
		urlConn.setRequestProperty("Accept", "applicaiton/json");
		urlConn.setDoInput(true);
		urlConn.setDoOutput(true);
		urlConn.connect();

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConn.getOutputStream()));
		writer.write(params.toString());
		writer.flush();
		writer.close();

		InputStream is = new BufferedInputStream(urlConn.getInputStream());

		if(urlConn.getResponseCode() == HttpURLConnection.HTTP_OK)
		{
			is = urlConn.getInputStream();
		}
		else
		{
			is = urlConn.getErrorStream();
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
		StringBuilder sb = new StringBuilder();
		String line = null;

		while ((line = reader.readLine()) != null)
		{
			sb.append(line + "\n");
		}

		is.close();

		return sb.toString();
	}

	public String formPostQuery(LinkedHashMap data) throws UnsupportedEncodingException
	{
		StringBuilder result = new StringBuilder();

		return result.toString();
	}
}
