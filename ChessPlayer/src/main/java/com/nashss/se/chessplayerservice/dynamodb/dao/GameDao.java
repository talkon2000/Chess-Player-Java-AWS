package com.nashss.se.chessplayerservice.dynamodb.dao;

import com.nashss.se.chessplayerservice.dynamodb.models.Game;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Accesses data for an album using {@link Game} to represent the model in DynamoDB.
 */
@Singleton
public class GameDao {

    private final DynamoDBMapper dynamoDBMapper;

    /**
     * Instantiates a GameDao object.
     *
     * @param dynamoDBMapper the {@link DynamoDBMapper} used to interact with the Games table
     */
    @Inject
    public GameDao(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    /**
     * Retrieves a Game by gameId.
     *
     * If not found, returns null.
     *
     * @param gameId The gameId to look up
     * @return The corresponding Game if found, or null if not
     */
    public Game load(String gameId) {
        return dynamoDBMapper.load(Game.class, gameId);
    }

    /**
     * Saves a Game to the database.
     *
     * @param game the Game to save
     */
    public void save(Game game) {
        dynamoDBMapper.save(game);
    }
}
