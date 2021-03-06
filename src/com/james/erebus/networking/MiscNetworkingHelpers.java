package com.james.erebus.networking;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.misc.AppConsts;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * A helper class which provides the necessary methods to POST and DELETE information to and from the server
 * and to add information to internal files
 * @author james
 *
 */

public class MiscNetworkingHelpers {

	public static String regId;

	public static Handler handler = new Handler(Looper.getMainLooper());

	
	/**
	 * Adds a {@link com.james.erebus.JSONJava.JSONObject} to a file on the internal storage
	 * @param obj The object to be added
	 * @param filename The name of the file which will be added to
	 */
	public static void addEntryToInternalStorage(JSONObject obj, String filename)
	{
		try{
			FileInputStream fis = AppConsts.currentActivity.openFileInput(filename);
			JSONArray ja;
			int ch;
			StringBuffer strBuf = new StringBuffer("");
			while((ch = fis.read()) != -1)
			{
				strBuf.append((char)ch);
			}
			fis.close();
			ja = new JSONArray(strBuf.toString());
			
			boolean doesObjectExist = false;
			for(int i = 0; i < ja.length(); i++)
			{
				if(ja.getJSONObject(i).get("id").equals(obj.get("id")))
				{
					doesObjectExist = true;
					ja.put(i, obj);
				}
			}
			if(!doesObjectExist)
			{	
				ja.put(obj);
				
			}
			FileOutputStream fos = AppConsts.currentActivity.openFileOutput(filename, Context.MODE_PRIVATE);
			fos.write(ja.toString().getBytes());
			fos.close();
		}
		catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Posts information to the server
	 * @param regId The registration ID of this app
	 * @param uriExtension The URI extension to post to in String form
	 * @param info The information to be posted
	 * @return If the information was posted successfully
	 * @throws Exception
	 */
	public static boolean postInformationToServer(String regId, String uriExtension, ArrayList<BasicNameValuePair> info) throws Exception
	{
		HttpClient httpclient = new DefaultHttpClient();
		HttpParams httpParameters = httpclient.getParams();
		HttpConnectionParams.setTcpNoDelay(httpParameters, true); 
		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);

		HttpPost httppost = new HttpPost("http://teamfrag.net:3002/" + uriExtension);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(info.size());
		for(BasicNameValuePair p : info)
			nameValuePairs.add(p);
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		HttpResponse response = httpclient.execute(httppost);
		Log.v("response_code", Integer.toString(response.getStatusLine().getStatusCode()));
		if(response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300)
		{
			Log.v("postInformationToServer", "success!");
			return true;
		}
		else
		{
			Log.v("postInformationToServer", "failure :(");
			return false;
		}
	}

	/**
	 * Deletes information from the server
	 * @param regId The registration ID of this app
	 * @param uriExtension The URI extension to post to
	 * @return If the information was deleted successfully
	 * @throws Exception
	 */
	public static boolean deleteInformationFromServer(String regId, String uriExtension) throws Exception
	{
		HttpClient httpclient = new DefaultHttpClient();
		HttpParams httpParameters = httpclient.getParams();
		HttpConnectionParams.setTcpNoDelay(httpParameters, true); 
		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);

		HttpDelete httpdelete = new HttpDelete("http://teamfrag.net:3002/" + uriExtension);

		HttpResponse response = httpclient.execute(httpdelete);
		Log.v("response_code", Integer.toString(response.getStatusLine().getStatusCode()));
		if(response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300)
		{
			Log.v("deleteInformationFromServer", "success!");
			return true;
		}
		else
		{
			Log.v("deleteInformationFromServer", "failure :(");
			return false;
		}
	} 
}
