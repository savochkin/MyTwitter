package com.savochkin.twitter.domain;

import org.springframework.stereotype.Component;

import java.util.*;

public class Twitter {

    private int timestamp = 0;
    public static final int MAX_TWEETS = 10;
    // We are storing own tweet of the user. This is the optimization - just so that
    private FeedRepository ownTweetsRepository; //  = new TweetHashMapRepository(); // todo
    private FeedRepository feedsRepository; // = new TweetHashMapRepository();
    private UserRepository userRepository; // = new UserHashMapRepository();
    public Twitter(FeedRepository ownTweetsRepository, FeedRepository feedsRepository, UserRepository userRepository) {
        this.ownTweetsRepository = ownTweetsRepository;
        this.feedsRepository = feedsRepository;
        this.userRepository = userRepository;
    }

    public void postTweet(int userId, int tweetId) { // O(F)
        Tweet tweet = new Tweet(++timestamp, tweetId);
        // add tweet to own feed
        ownTweetsRepository.getFeed(userId).addFirst(tweet);
        // add tweet to each follower's feed
        userRepository.getFollowers(userId)
                .forEach(followerId -> feedsRepository.getFeed(followerId).addFirst(tweet));
    }

    public List<Integer> getNewsFeed(int userId) {// O(1)
        return feedsRepository.getFeed(userId).getAll().stream()
                .map(tweet -> tweet.id())
                .toList();
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
