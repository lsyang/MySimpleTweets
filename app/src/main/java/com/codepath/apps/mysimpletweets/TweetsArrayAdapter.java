package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by lsyang on 3/21/17.
 */
// Taking the Tweets Objects and turning them into views displayed in the list
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private long maxId = Long.MAX_VALUE;
    private long sinceId = 1;
    private OnItemSelectedListener profileListener;

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
        if (context instanceof OnItemSelectedListener) {
            profileListener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement TweetsArrayAdapter.OnItemSelectedListener");
        }
    }

    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {
        // This can be any number of events to be sent to the activity
        public void onProfileImageClicked(String screeName);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the tweet
        // find or inflate the template
        // populate data into subview

        Tweet tweet = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);

        }
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
        TextView tvTimeAgo = (TextView) convertView.findViewById(R.id.tvTimeAgo);

        tvUserName.setText(tweet.getUser().getName());
        tvBody.setText(tweet.getBody());
        tvScreenName.setText("@" + tweet.getUser().getScreenName());
        tvTimeAgo.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
        if (tweet.getUid() < maxId) {
            maxId = tweet.getUid();
        }

        if (tweet.getUid() > sinceId) {
            sinceId = tweet.getUid();
        }

        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfielImageUrl()).into(ivProfileImage);
        ivProfileImage.setTag(R.id.ivProfileImage, tweet.getUser().getScreenName());


        ivProfileImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + v);
                profileListener.onProfileImageClicked((String) v.getTag(R.id.ivProfileImage));
            }
        });
        return convertView;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public long getMaxID() {
        return maxId;
    }

    public long getSinceId() {
        return sinceId;
    }

}
