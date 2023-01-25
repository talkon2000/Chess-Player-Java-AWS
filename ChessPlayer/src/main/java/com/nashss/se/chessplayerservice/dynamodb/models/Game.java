package com.nashss.se.chessplayerservice.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Games")
public class Game {

    private String gameId;
    private String moves;
    private String whitePlayerId;
    private String blackPlayerId;
    private Integer botDifficulty;
    private String active;

    @DynamoDBHashKey(attributeName = "gameId")
    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    @DynamoDBRangeKey(attributeName = "active")
    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    @DynamoDBAttribute(attributeName = "moves")
    public String getMoves() {
        return moves;
    }

    public void setMoves(String moves) {
        this.moves = moves;
    }

    @DynamoDBAttribute(attributeName = "whitePlayerId")
    public String getWhitePlayerId() {
        return whitePlayerId;
    }

    public void setWhitePlayerId(String whitePlayerId) {
        this.whitePlayerId = whitePlayerId;
    }

    @DynamoDBAttribute(attributeName = "blackPlayerId")
    public String getBlackPlayerId() {
        return blackPlayerId;
    }

    public void setBlackPlayerId(String blackPlayerId) {
        this.blackPlayerId = blackPlayerId;
    }

    @DynamoDBAttribute(attributeName = "botDifficulty")
    public Integer getBotDifficulty() {
        return botDifficulty;
    }

    public void setBotDifficulty(Integer botDifficulty) {
        this.botDifficulty = botDifficulty;
    }
}
