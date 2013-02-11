package com.james.erebus.networking;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map.Entry;

import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.JSONJava.JSONTokener;



public class TournamentRetriever extends Retriever{

	@Override
	public void updatePage()
	{/*
		DefaultHttpClient c = new DefaultHttpClient();
		HttpGet g = new HttpGet(baseUrl);
		try {
			HttpResponse r = c.execute(g);
			System.out.println(r.getStatusLine());
			HttpEntity e = r.getEntity();
			r.getEntity().consumeContent();
		
			//org.apache.http.util.EntityUtils.
		}catch (ClientProtocolException e) {
		 	// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally {
			//
		} */
		
	}
	
	public static void main(String[] args)
	{/*
		 URL url;
	        try {
	            url = new URL("http://192.168.0.11:3000/tournaments.json");
	            HttpURLConnection c = (HttpURLConnection)url.openConnection();
	            InputStream in = new BufferedInputStream(c.getInputStream());
	            JSONTokener jt = new JSONTokener(in);
	            JSONArray ja = new JSONArray(jt);
	            for(int i = 0; i < ja.length(); i++)
	            {
	            	System.out.println(ja.get(i));
	            }
	        } catch (MalformedURLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   
		try {
			JSONObject jo1 = new JSONObject("{\"id\":28,\"updated_at\":\"2013-01-10T00:00:00Z\",\"status\":\"status1\",\"location\":\"location1\",\"name\":\"name1\",\"created_at\":\"2013-01-10T00:00:00Z\",\"prizes\":\"prize1\",\"links\":\"somelinks1\",\"format\":\"format1\",\"sponsor\":\"sponsor1\",\"start_date\":\"07/01/2013\",\"entry_reqs\":\"entryreqs1\"}");
			System.out.println(jo1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    */
		
		TournamentRetriever t = new TournamentRetriever();
		//t.getTournaments();
		t.getBySubbed();
	}
	/**
	 * 
	 * @return JSONArray if query was valid, null otherwise
	 */
	public JSONArray getTournaments()
	{
		disableConnectionReuseIfNecessary();
		URL url;
		try {
			url = new URL("http://192.168.0.11:3000/tournaments.json");
			HttpURLConnection c = (HttpURLConnection)url.openConnection();
	        InputStream in = new BufferedInputStream(c.getInputStream());
	        JSONTokener jt = new JSONTokener(in);
	        JSONArray tournaments = new JSONArray(jt);
	        //System.out.println(tournaments.toString());
	        return tournaments;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
       
	}

	@Override
	public void getBySubbed() 
	{
		JSONArray tournaments = getTournaments();
		JSONArray subbedTournys = new JSONArray();
		for(int i = 0; i < tournaments.length(); i++)
		{
			try {
				JSONObject o = (JSONObject) tournaments.get(i);
				if(o.toString().contains("\"subscribed\":false"))
				{
					subbedTournys.put(o);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i = 0; i < subbedTournys.length(); i++)
		{
			try {
				System.out.println(subbedTournys.get(i).toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void getByUnsubbed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getByOngoing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getByPast() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getByFuture() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getEntryByName() {
		// TODO Auto-generated method stub
		
	}

}
