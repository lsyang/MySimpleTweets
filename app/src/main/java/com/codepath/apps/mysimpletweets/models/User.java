package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by lsyang on 3/21/17.
 */

public class User implements Serializable{
    private String name;
    private String screenName;
    private long uid;
    private String profielImageUrl;
    private String tagline;
    private int followersCount;
    private int followingCount;

    public String getTagline() {
        return tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public long getUid() {
        return uid;
    }

    public String getProfielImageUrl() {
        return profielImageUrl;
    }

    public static User fromJson(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.screenName = jsonObject.getString("screen_name");
            user.uid = jsonObject.getLong("id");
            user.profielImageUrl = jsonObject.getString("profile_image_url");
            user.tagline = jsonObject.getString("description");
            user.followersCount = jsonObject.getInt("followers_count");
            user.followingCount = jsonObject.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}
