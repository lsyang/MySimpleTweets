package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        lvTweets = (ListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);

        client = TwitterApplication.getRestClient(); // singleton client
        populateTimeline();
    }

    // send API request and fill the listview by creating the tweet objects from json
    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // deserialize json
                // create model
                //load into listview

                aTweets.addAll(Tweet.fromJsonArray(response));

                Log.d("Debug", "onSuccess: " + aTweets.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                    JSONObject errorResponse) {
                Log.d("Debug", "onFailure: " + errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                    Throwable throwable) {
                Log.d("DEBUG", "onFailure: " + responseString);
            }
        });
    }
}