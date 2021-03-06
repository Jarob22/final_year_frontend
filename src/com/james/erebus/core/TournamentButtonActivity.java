package com.james.erebus.core;

import java.io.IOException;
import java.net.UnknownHostException;

import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.misc.AppConsts;
import com.james.erebus.misc.MiscJsonHelpers;
import com.james.erebus.networking.SubscriptionRetriever;
import com.james.erebus.networking.TournamentSubscriptionManager;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

/**
 * The java file for the TournamentButton activity, which is the screen that shows when you click on one of the tournaments
 * on the {@link com.james.erebus.core.Tournament} activity screen
 * @author james
 *
 */

public class TournamentButtonActivity extends Activity {

	
	Tournament tournament;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.james.erebus.R.layout.activity_tournament_button);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		this.setTitle("");
		Bundle b = this.getIntent().getExtras();
		if(b != null)
		{
			JSONObject o = (JSONObject) b.get("com.james.erebus.TournamentButtonActivity.dataValues");
			displayData(o);
		}
	}

	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Displays all of the available {@link com.james.erebus.core.Tournament} data
	 * @param data The data to be displayed in JSON format
	 */
	private void displayData(JSONObject data)
	{
		TextView tvTitle = (TextView)findViewById(com.james.erebus.R.id.tournamentButtonTitleBox);
		tvTitle.setTextSize(50f);
		TextView tvDate = (TextView)findViewById(com.james.erebus.R.id.tournamentButtonDateBox);
		tvDate.setTextSize(20f);
		TextView tvLinks = (TextView)findViewById(com.james.erebus.R.id.tournamentButtonLinksBox);
		tvLinks.setTextSize(20f);
		TextView tvLocation = (TextView)findViewById(com.james.erebus.R.id.tournamentButtonLocationBox);
		tvLocation.setTextSize(20f);
		TextView tvFormat = (TextView)findViewById(com.james.erebus.R.id.tournamentButtonFormatBox);
		tvFormat.setTextSize(20f);
		TextView tvStatus = (TextView)findViewById(com.james.erebus.R.id.tournamentButtonStatusBox);
		tvStatus.setTextSize(20f);
		TextView tvSponsor = (TextView)findViewById(com.james.erebus.R.id.tournamentButtonSponsorBox);
		tvSponsor.setTextSize(20f);
		TextView tvEntryReqs = (TextView)findViewById(com.james.erebus.R.id.tournamentButtonEntryReqsBox);
		tvEntryReqs.setTextSize(20f);
		TextView tvPrizes = (TextView)findViewById(com.james.erebus.R.id.tournamentButtonPrizesBox);
		tvPrizes.setTextSize(20f);


		tournament = MiscJsonHelpers.jsonToTournament(data);
		tvTitle.setText(tournament.getName());
		tvDate.setText("Tournament date: " + tournament.getStartDate());
		tvLinks.setText(tournament.getLinks());
		tvLocation.setText("Location: " + tournament.getLocation());
		tvFormat.setText("Format: " + tournament.getFormat());
		tvStatus.setText("Status: " + tournament.getStatus());
		tvSponsor.setText("Sponsor(s): " + tournament.getSponsor());
		tvEntryReqs.setText("Entry requirements: " + tournament.getEntryReqs());
		tvPrizes.setText("Prize(s): " + tournament.getPrizes());
		try {
			setSubButtonText();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the text in the subscribe/unsubscribe button
	 * @throws UnknownHostException 
	 */
	private void setSubButtonText() throws UnknownHostException
	{
		SubscriptionRetriever sr = new SubscriptionRetriever();
		JSONArray ja = sr.forceRetrieveFromServer(sr.getURI(), sr.getSubscriptionsFilename());
		Button subButton = (Button) findViewById(com.james.erebus.R.id.tournamentSubscribeButton);
		if(ja == null)
		{
			subButton.setText("Unknown");
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Subscription information was not able to be retrieved")
			.setTitle("Connection error");
			AlertDialog dialog = builder.create();
			dialog.show();
			subButton.setEnabled(false);
			subButton.setClickable(false);
			return;
		}
		subButton.setEnabled(true);
		subButton.setClickable(true);
		for(int i = 0; i < ja.length(); i++)
		{
			try {
				JSONObject obj = ja.getJSONObject(i);
				Log.v("objsubbuttontext", obj.toString());
				if(obj.get("model_type").equals("TournamentEntry"))
				{
					if(tournament.getId() == Integer.parseInt(obj.get("model_id").toString()))
					{
						subButton.setText("Subscribed");
						return;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		subButton.setText("Unsubscribed");
	}
	
	
	/**
	 * Subscribes or unsubscribes from a tournament depending on whether the tournament is subscribed to or not
	 * @param v The current {@link android.view.View}
	 * @throws IOException
	 * @throws JSONException
	 */
	public void tournamentSubUnsub(View v) throws IOException, JSONException
	{
		Button subButton = (Button) findViewById(com.james.erebus.R.id.tournamentSubscribeButton);
		TournamentSubscriptionManager tsm = new TournamentSubscriptionManager();
		subButton.setClickable(false);
		subButton.setEnabled(false);
		if(!tsm.isTournamentSubbed(this, tournament))
		{

			tsm.subToTournament(tournament, this, subButton);
		}
		else
		{
			tsm.unsubFromTournament(this, tournament, subButton);
		}
	}
	
	@Override
	public void onResume()
	{
		AppConsts.currentActivity = this;
		super.onResume();
	}

}