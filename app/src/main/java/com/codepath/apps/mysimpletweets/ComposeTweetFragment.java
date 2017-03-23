package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by lsyang on 3/22/17.
 */

public class ComposeTweetFragment extends DialogFragment{
    private EditText mTweet;
    private Button btnTweet;



    public interface ComposeTweetListener {
        void onFinishComposeDialog(String inputText);
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

        mTweet = (EditText) view.findViewById(R.id.et_tweet);
        btnTweet = (Button) view.findViewById(R.id.btn_tweet);
        String title = getArguments().getString("title", "Compose");
        getDialog().setTitle(title);

        //show keyboard
        mTweet.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnTweet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ComposeTweetListener listener = (ComposeTweetListener) getActivity();
                listener.onFinishComposeDialog(mTweet.getText().toString());
                dismiss();
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

    public void onTweet(View view) {

        dismiss();
    }
}
