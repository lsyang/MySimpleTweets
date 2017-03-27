package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lsyang on 3/22/17.
 */

public class ComposeTweetFragment extends DialogFragment{
    private TwitterClient client;
    private EditText mTweet;
    private Button btnTweet;
    private TextView tvCharCount;
    private Tweet tweet;
    private final int CHARACTER_LIMIT = 140;



    public interface ComposeTweetListener {
        void onFinishComposeDialog(Tweet tweet);
    }

    public ComposeTweetFragment() {

    }

    public static ComposeTweetFragment newInstance(String title) {
        ComposeTweetFragment frag = new ComposeTweetFragment();
        Bundle args = new Bundle();
        args.putString("Title", title);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_compose_tweet, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        client = TwitterApplication.getRestClient();

        mTweet = (EditText) view.findViewById(R.id.et_tweet);
        btnTweet = (Button) view.findViewById(R.id.btn_tweet);
        tvCharCount = (TextView) view.findViewById(R.id.tv_charCount);
        String title = getArguments().getString("title", "Compose");
        getDialog().setTitle(title);

        //show keyboard
        mTweet.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnTweet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = mTweet.getText().toString();
                postTweet(status);
            }
        });



        mTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCharCount.setText(Integer.toString(CHARACTER_LIMIT - count - start));
                if (count + start > CHARACTER_LIMIT) {
                    btnTweet.setEnabled(false);
                } else {
                    btnTweet.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    public void backToTimeline() {
        ComposeTweetListener listener = (ComposeTweetListener) getActivity();
        listener.onFinishComposeDialog(tweet);
        dismiss();
    }

    public void postTweet(String status) {
        client.postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                tweet = Tweet.fromJson(response);
                backToTimeline();
                Log.d("resp", "onSuccess: " + tweet);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                    JSONObject errorResponse) {
                Log.d("resp", "onFailure: " + errorResponse);
            }
        }, status);
    }
}
