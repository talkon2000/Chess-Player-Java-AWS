package com.nashss.se.chessplayerservice.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.chessplayerservice.dynamodb.models.User;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserDao {
    private final DynamoDBMapper dynamoDBMapper;

    @Inject
    public UserDao(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public User load(String userId) {
        return dynamoDBMapper.load(User.class, userId);
    }

    public User loadUserLimited(String userId) {
        User user = load(userId);
        // User not found in DB
        if (user == null) {
            return null;
        }
        User limitedCopyOfUser = new User();
        limitedCopyOfUser.setUserId(user.getUserId());
        limitedCopyOfUser.setActive(user.getActive());
        limitedCopyOfUser.setUsername(user.getUsername());
        limitedCopyOfUser.setRating(user.getRating());
        return limitedCopyOfUser;
    }

    public void createUser(User user) {
        dynamoDBMapper.save(user);
    }
}
