package com.savochkin.twitter.domain;

import java.util.List;

public interface UserRepository {
    List<Integer> getFollowers(int userId);
    void reset();
}
