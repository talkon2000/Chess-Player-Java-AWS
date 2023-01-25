package com.nashss.se.chessplayerservice.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.chessplayerservice.dynamodb.models.Game;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameDao {

    private final DynamoDBMapper dynamoDBMapper;

    @Inject
    public GameDao(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public Game load(String gameId, String active) {
        return dynamoDBMapper.load(Game.class, gameId, active);
    }

    public void save(Game game) {
        dynamoDBMapper.save(game);
    }
}
