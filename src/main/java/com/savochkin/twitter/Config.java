package com.savochkin.twitter;

import com.savochkin.twitter.persistence.db.UserDBRepository;
import com.savochkin.twitter.persistence.inmemory.TweetHashMapRepository;
import com.savochkin.twitter.domain.*;
import com.savochkin.twitter.persistence.inmemory.UserHashMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${userRepositoryType}")
    String userRepositoryType;

    @Autowired
    UserDBRepository userDBRepository;

    @Bean
    UserRepository userRepository() {
        if ("db".equals(userRepositoryType)) {
            return userDBRepository;
        } else {
            return new UserHashMapRepository();
        }
    }

    @Bean
    Twitter twitter() {
        return new Twitter(new TweetHashMapRepository(), new TweetHashMapRepository(), userRepository());
    }
}
