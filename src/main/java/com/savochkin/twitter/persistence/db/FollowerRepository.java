package com.savochkin.twitter.persistence.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowerRepository extends CrudRepository<Follower, Long> {
    @Query(value = "SELECT f FROM Follower f WHERE f.followeeId = ?1")
    List<Follower> findFollowers(int followeeId);
}
