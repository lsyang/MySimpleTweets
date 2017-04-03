package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lsyang on 3/29/17.
 */

public class UserTimelineFragment extends TweetsListFragment{
    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient(); // singleton client
        populateTimeline(0, false);
    }

    public static UserTimelineFragment newInstance(String screen_name) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    // send API request and fill the listview by creating the tweet objects from json
    @Override
    public void populateTimeline(long maxId, final boolean isRefresh) {
        String screenName = getArguments().getString("screen_name");
        client.getUserTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // deserialize json
                // create model
                //load into listview
                ArrayList<Tweet> newTweets = Tweet.fromJsonArray(response);
                addAll(newTweets, isRefresh);
                // Log.d("Debug", "onSuccess: " + aTweets.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                    JSONObject errorResponse) {
                Log.d("Debug", "onFailure: " + errorResponse.toString());
                Toast.makeText(getContext(), errorResponse.optString("errors"), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                    Throwable throwable) {
                Log.d("DEBUG", "onFailure: " + responseString);
            }
        }, screenName);
    }
}
