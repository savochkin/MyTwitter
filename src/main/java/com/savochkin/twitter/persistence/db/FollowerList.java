package com.savochkin.twitter.persistence.db;

import java.util.*;

public class FollowerList extends ArrayList {
    FollowerRepository followerRepository;
    int followeeId;

    public FollowerList(FollowerRepository followerRepository, int followeeId, List<Integer> followers) {
        super(followers);
        this.followerRepository = followerRepository;
        this.followeeId = followeeId;
    }

    @Override
    public boolean add(Object o) {
        Integer followerId = (Integer)o;
        super.add(followerId);
        Follower follower = new Follower(followerId, this.followeeId);
        followerRepository.save(follower);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        Integer followerId = (Integer) o;
        super.remove(followerId);
        Follower follower = new Follower(followerId, this.followeeId);
        followerRepository.delete(follower);
        return true;
    }
}
