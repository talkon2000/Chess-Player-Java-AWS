package com.nashss.se.chessplayerservice.dynamodb.dao;

import com.nashss.se.chessplayerservice.dynamodb.models.User;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Accesses data for an album using {@link User} to represent the model in DynamoDB.
 */
@Singleton
public class UserDao {
    private final DynamoDBMapper dynamoDBMapper;

    /**
     * Instantiates a UserDao object.
     *
     * @param dynamoDBMapper the {@link DynamoDBMapper} used to interact with the Users table
     */
    @Inject
    public UserDao(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    /**
     * Retrieves a User by username.
     *
     * If not found, returns null.
     *
     * @param username The username to look up
     * @return The corresponding User if found, or null if not
     */
    public User load(String username) {
        return dynamoDBMapper.load(User.class, username);
    }

    /**
     * Retrieves a User by username. Hides the email of the user
     *
     * If not found, returns null.
     *
     * @param username The username to look up
     * @return The corresponding User if found, or null if not
     */
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

    /**
     * Saves a User to the database.
     *
     * @param user the User to save
     */
    public void saveUser(User user) {
        dynamoDBMapper.save(user);
    }
}
