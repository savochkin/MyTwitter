package com.savochkin.twitter.persistence.db;

import javax.persistence.*;

@Entity
public class Follower {

    // FOLLOWER(follower_id, followee_id)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    int followerId;
    int followeeId;

    protected Follower() {}

    public Follower(int followerId, int followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
    }
}
