package com.savochkin.twitter.persistence.db;

import com.savochkin.twitter.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDBRepository implements UserRepository {
    @Autowired
    FollowerRepository followerRepository;
    @Override
    public List<Integer> getFollowers(int userId) {
        return new FollowerList(
                this.followerRepository,
                userId,
                followerRepository.findFollowers(userId).stream()
                        .map(f -> f.followerId).toList());
    }

    @Override
    public void reset() {
        followerRepository.deleteAll();
    }
}
