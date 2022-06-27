package com.savochkin.twitter.domain;

import java.util.*;

public class Twitter {

    private int timestamp = 0;
    public static final int MAX_TWEETS = 10;
    private final FeedRepository ownTweetsRepository;
    private final FeedRepository feedsRepository;
    private final UserRepository userRepository;
    public Twitter(FeedRepository ownTweetsRepository, FeedRepository feedsRepository, UserRepository userRepository) {
        this.ownTweetsRepository = ownTweetsRepository;
        this.feedsRepository = feedsRepository;
        this.userRepository = userRepository;
    }

    public void postTweet(int userId, int tweetId) { // O(F)
        Tweet tweet = new Tweet(++timestamp, tweetId);
        // add tweet to own feed
        ownTweetsRepository.getFeed(userId).addFirst(tweet);
        // user also follows himself
        feedsRepository.getFeed(userId).addFirst(tweet);
        // add tweet to each follower's feed
        userRepository.getFollowers(userId).forEach(followerId -> feedsRepository.getFeed(followerId).addFirst(tweet));
    }

    public List<Integer> getNewsFeed(int userId) {// O(1)
        return feedsRepository.getFeed(userId).getAll().stream().map(Tweet::id).toList();
    }

    public void follow(int followerId, int followeeId) {// O(1) since our feed is constrained
        userRepository.getFollowers(followeeId).add(followerId);
        feedsRepository.getFeed(followerId).addAll(ownTweetsRepository.getFeed(followeeId).getAll());
    }

    public void unfollow(int followerId, int followeeId) {// O(1) O(1) since our feed is constrained
        userRepository.getFollowers(followeeId).remove(Integer.valueOf(followerId));
        feedsRepository.getFeed(followerId).removeAll(ownTweetsRepository.getFeed(followeeId));
    }

    public void reset() {
        userRepository.reset();
        ownTweetsRepository.reset();
        feedsRepository.reset();
    }
}
