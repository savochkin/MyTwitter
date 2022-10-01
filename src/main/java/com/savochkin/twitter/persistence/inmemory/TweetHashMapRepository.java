package com.savochkin.twitter.persistence.inmemory;

import com.savochkin.twitter.domain.Feed;
import com.savochkin.twitter.domain.Tweet;
import com.savochkin.twitter.domain.FeedRepository;
import com.savochkin.twitter.domain.Twitter;

import java.util.HashMap;
import java.util.Map;

public class TweetHashMapRepository implements FeedRepository {
    private Map<Integer, Feed<Tweet>> tweets = new HashMap();

    @Override
    public Feed<Tweet> getFeed(int userId) {
        return tweets.computeIfAbsent(userId, k -> new Feed<>(Twitter.MAX_TWEETS, (t1, t2) -> t2.time() - t1.time()));
    }

    @Override
    public void reset() {
        tweets = new HashMap();
    }
}
