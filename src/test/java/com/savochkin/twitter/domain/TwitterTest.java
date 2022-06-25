package com.savochkin.twitter.domain;

import com.savochkin.twitter.domain.Twitter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TwitterTest {

    @Autowired
    Twitter twitter;

    @AfterEach
    public void afterEach() {
        twitter.reset();
    }

    @Test
    public void testPostTweet() {
        twitter.postTweet(1, 5); // User 1 posts a new tweet (id = 5).
        List<Integer> feed1 = twitter.getNewsFeed(1);  // User 1's news feed should return a list with 1 tweet id -> [5]. return [5]
        assertEquals(1, feed1.size());
        assertEquals(5, feed1.get(0));
    }

    @Test
    public void testFollowUserAndCheckPostsAdded() {
        twitter.follow(1, 2);    // User 1 follows user 2.
        twitter.postTweet(2, 6); // User 2 posts a new tweet (id = 6).
        List<Integer> feed = twitter.getNewsFeed(1);  // User 1's news feed should return a list with 2 tweet ids -> [6, 5]. Tweet id 6 should precede tweet id 5 because it is posted after tweet id 5.
        assertEquals(1, feed.size());
        assertEquals(6, feed.get(0));
    }

    @Test
    public void testFollowUserAndCheckPostsAddedInRightOrder() {
        // timeline:
        // user 1:    5     7
        // user 2:       6

        twitter.postTweet(1, 5);
        twitter.postTweet(2, 6);
        twitter.postTweet(1, 7);
        twitter.follow(1, 2);    // User 1 follows user 2.
        List<Integer> feed = twitter.getNewsFeed(1);  // User 1's news feed should return a list with 2 tweet ids -> [6, 5]. Tweet id 6 should precede tweet id 5 because it is posted after tweet id 5.
        assertEquals(3, feed.size());
        assertEquals(7, feed.get(0));
        assertEquals(6, feed.get(1));
        assertEquals(5, feed.get(2));
    }

    @Test
    public void testUnfollowUserAndCheckPostsRemoved() {
        twitter.follow(1, 2);    // User 1 follows user 2.
        twitter.postTweet(2, 6); // User 2 posts a new tweet (id = 6).
        List<Integer> feed = twitter.getNewsFeed(1);  // User 1's news feed should return a list with 2 tweet ids -> [6, 5]. Tweet id 6 should precede tweet id 5 because it is posted after tweet id 5.
        assertEquals(1, feed.size());
        assertEquals(6, feed.get(0));

        twitter.unfollow(1, 2);
        feed = twitter.getNewsFeed(1);
        assertEquals(0, feed.size());
    }

    @Test
    public void testUnfollowUserAndCheckPostsInRightOrder() {
        // timeline:
        // user 1:    5     7
        // user 2:       6
        // user 1 follows 2 => 5 6 7
        // user 1 unfollows 2 => 5 7
        twitter.postTweet(1, 5);
        twitter.postTweet(2, 6);
        twitter.postTweet(1, 7);
        twitter.follow(1, 2);    // User 1 follows user 2.
        twitter.unfollow(1, 2);
        List<Integer> feed = twitter.getNewsFeed(1);
        assertEquals(2, feed.size());
        assertEquals(7, feed.get(0));
        assertEquals(5, feed.get(1));
    }

        @Test
    public void test01() {
        twitter.postTweet(1, 5); // User 1 posts a new tweet (id = 5).
        List<Integer> feed1 = twitter.getNewsFeed(1);  // User 1's news feed should return a list with 1 tweet id -> [5]. return [5]
        assertEquals(1, feed1.size());
        assertEquals(5, feed1.get(0));
        twitter.follow(1, 2);    // User 1 follows user 2.
        twitter.postTweet(2, 6); // User 2 posts a new tweet (id = 6).
        List<Integer> feed2 = twitter.getNewsFeed(1);  // User 1's news feed should return a list with 2 tweet ids -> [6, 5]. Tweet id 6 should precede tweet id 5 because it is posted after tweet id 5.
        assertEquals(2, feed2.size());
        assertEquals(6, feed2.get(0));
        assertEquals(5, feed2.get(1));
        twitter.unfollow(1, 2);  // User 1 unfollows user 2.
        twitter.getNewsFeed(1);  // User 1's news feed should return a list with 1 tweet id -> [5], since user 1 is no longer following user 2.
        assertEquals(1, feed1.size());
        assertEquals(5, feed1.get(0));
    }

    private void printMemoryInfo() {
        int MB = 1024*1024;

        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();

        //Print used memory
        System.out.println("Used Memory:"
                + (runtime.totalMemory() - runtime.freeMemory()) / MB);

        //Print free memory
        System.out.println("Free Memory:"
                + runtime.freeMemory() / MB);

        //Print total available memory
        System.out.println("Total Memory:" + runtime.totalMemory() / MB);

        //Print Maximum available memory
        System.out.println("Max Memory:" + runtime.maxMemory() / MB);
    }
    @Test
    public void testStress() {

        //printMemoryInfo();

        // we have 10 000 writers and 1M of readers
        // every reader follow 10 writers
        // every writer posts 1000 tweets
        // than every writer unfollows from 1 writer and follows another writer taken at random

        int W = 100; // number of writers
        int R = 1000; // number of readers
        int F = 10; // number of follows
        int T = 1000; // number of tweets per writer

        long start = System.currentTimeMillis();

        System.out.println("Setting up readers...");
        IntStream.range(0, R).forEach(r -> {
                            int firstWriter = r % W;
                            for (int f = 0; f < F; f++) {
                                int followeeId = (firstWriter + f) % W;
                                twitter.follow(r, followeeId);
                                //System.out.println("reader "+r+" is now following writer "+followeeId);
                            }
                        }
                );

        /*for(int r=0; r<R; r++) {
            int firstWriter = r % W;
            for(int f=0; f<F; f++) {
                int followeeId = (firstWriter + f) % W;
                twitter.follow(r, followeeId);
                //System.out.println("reader "+r+" is now following writer "+followeeId);
            }*/

        long end1 = System.currentTimeMillis();
        System.out.println("setting up readers took "+(end1-start) + " ms.");

        System.out.println("Readers now following writers");

        long start1 = System.currentTimeMillis();
        int tweetId = 0;
        for(int t=0; t<T; t+=10) {
            for(int w=0; w<W; w++) {
                twitter.postTweet(w, tweetId++);
            }
        }

        for(int r=0; r<R; r++) {
            List<Integer> tweets = twitter.getNewsFeed(r);
            assertEquals(10, tweets.size());
        }

        for(int t=0; t<T; t++) {
            for(int w=0; w<W; w++) {
                twitter.postTweet(w, tweetId++);
            }
        }

        for(int r=0; r<R; r++) {
            List<Integer> tweets = twitter.getNewsFeed(r);
            assertEquals(10, tweets.size());
        }

        long end2 = System.currentTimeMillis();
        System.out.println("posting tweets took "+(end2-start1) + " ms.");
        System.out.println("Writers have posted all tweets");

        long start3 = System.currentTimeMillis();

        for(int r=0; r<R; r++) {
            int unfollowId = r % W;
            twitter.unfollow(r, unfollowId);
            twitter.follow(r, unfollowId);
        }
        long end3 = System.currentTimeMillis();
        System.out.println("following/unfollowing  took "+(end3-start3) + " ms.");
        System.out.println("Each reader unfollowed 1 writer and followed another 1");

        long end = System.currentTimeMillis();
        System.out.println("stress test took "+(end-start) + " ms.");
        assertTrue(10000 >= (end-start));
    }
}
