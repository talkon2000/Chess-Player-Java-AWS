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

    public User load(String username) {
        return dynamoDBMapper.load(User.class, username);
    }

    public User loadPublicUser(String username) {
        User user = load(username);
        // User not found in DB
        if (user == null) {
            return null;
        }
        User limitedCopyOfUser = new User();
        limitedCopyOfUser.setUsername(user.getUsername());
        limitedCopyOfUser.setActive(user.getActive());
        limitedCopyOfUser.setRating(user.getRating());
        limitedCopyOfUser.setGames(user.getGames());
        return limitedCopyOfUser;
    }

    public void saveUser(User user) {
        dynamoDBMapper.save(user);
    }
}
