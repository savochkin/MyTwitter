package com.savochkin.twitter.domain;

public interface FeedRepository {
    Feed<Tweet> getFeed(int userId);
    void reset();
}
