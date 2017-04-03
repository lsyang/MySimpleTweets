package com.codepath.apps.mysimpletweets.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.fragments.ComposeTweetFragment;
import com.codepath.apps.mysimpletweets.fragments.ComposeTweetFragment.ComposeTweetListener;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity
        implements TweetsArrayAdapter.OnItemSelectedListener, ComposeTweetListener {

    HomeTimelineFragment _homeTimelineFragment;
    MentionsTimelineFragment _mentionsTimelineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        _homeTimelineFragment = new HomeTimelineFragment();
        _mentionsTimelineFragment = new MentionsTimelineFragment();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the viewpager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        // Set the viewpager adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        // Find the pager sliding tabs
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the pager tabs to the viewpager
        tabStrip.setViewPager(vpPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }



    public void onComposeAction(MenuItem item) {
        //bring up the compose modal
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetFragment composeTweetFragment = ComposeTweetFragment.newInstance("Compose");
        composeTweetFragment.show(fm, "fragment_compose_tweet");
    }

    public void onProfileView(MenuItem imProfile) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFinishComposeDialog(Tweet tweet) {
        // TODO: refactor this
//        FragmentManager fm = getSupportFragmentManager();
//        HomeTimelineFragment timelineFragment = new HomeTimelineFragment();
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        tweets.add(0, tweet);
        _homeTimelineFragment.addAll(tweets, true);

//        tweets.add(0, tweet);
//        aTweets.notifyDataSetChanged();
//        lvTweets.setSelectionAfterHeaderView();
    }

     @Override
    public void onProfileImageClicked(String screeName) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("screen_name", screeName);
            startActivity(intent);
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return _homeTimelineFragment;
            } else if (position == 1) {
                return _mentionsTimelineFragment;
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
