package com.savochkin.twitter.data;

import com.savochkin.twitter.domain.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserHashMapRepository implements UserRepository {
    private Map<Integer, List<Integer>> followers = new HashMap();

    @Override
    public List<Integer> getFollowers(int userId) {
        return followers.computeIfAbsent(userId, k -> {
            List<Integer> followers = new ArrayList<>();
            followers.add(userId);
            return followers;
        });
    }
}
