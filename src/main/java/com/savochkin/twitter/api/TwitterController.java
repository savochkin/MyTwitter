package com.savochkin.twitter.api;

import com.savochkin.twitter.domain.Twitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TwitterController {

    @Autowired
    private Twitter twitter;

    @GetMapping("/user/{userId}/tweet/{tweetId}")
    public ResponseEntity<String> postTweet(@PathVariable int userId, @PathVariable int tweetId) {
        System.out.println("userId = "+userId + " tweetId="+tweetId);
        twitter.postTweet(userId, tweetId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}/feed")
    public ResponseEntity<String> getNewsFeed(@PathVariable int userId) {
        List<Integer> tweets = twitter.getNewsFeed(userId);
        return ResponseEntity.ok().body(tweets.toString());
    }

    @GetMapping("/user/{followerId}/follow/{followeeId}")
    public ResponseEntity<String> follow(@PathVariable int followerId, @PathVariable int followeeId) {
        twitter.follow(followerId, followeeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{followerId}/unfollow/{followeeId}")
    public ResponseEntity<String> unfollow(@PathVariable int followerId, @PathVariable int followeeId) {
        twitter.unfollow(followerId, followeeId);
        return ResponseEntity.ok().build();
    }

}
