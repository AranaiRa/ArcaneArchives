package com.aranaira.arcanearchives.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import io.netty.handler.codec.DecoderResult;

public class PasteBinHelper 
{
	private static final String api_dev_key = "b3dd7c6a1d559ea3b6c53e6e143955bc";
	
	public static String PostToPasteBin(String s)
	{
		try {
			/*
			URL url = new URL("https://pastebin.com/api/api_post.php");
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestProperty("api_option", "paste");
			conn.setRequestProperty("api_dev_key", api_dev_key);
			conn.setRequestProperty("api_paste_code", s);
			conn.setRequestMethod("POST");
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				stringBuilder.append(line + "\n");
			}
			return stringBuilder.toString();
			*/
			HttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost("https://pastebin.com/api/api_post.php");
	
			// Request parameters and other properties.
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("api_option", "paste"));
			params.add(new BasicNameValuePair("api_dev_key", api_dev_key));
			params.add(new BasicNameValuePair("api_paste_code", s));
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
	
			//Execute and get the response.
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
	
			if (entity != null) {
			    InputStream instream = entity.getContent();
			    try {
			    	BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
					StringBuilder stringBuilder = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null)
					{
						stringBuilder.append(line);
					}
					if (line == null)
						return "An issue had occured";
			    	return line;
			    } finally {
			        instream.close();
			    }
			}
		} catch (Exception e)
		{
			return e.toString();
		}
		
		
		
		return "";
	}
}
