package com.nashss.se.chessplayerservice.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

import javax.annotation.Nonnull;

@DynamoDBTable(tableName = "Games")
public class Game {

    private String gameId;
    private String active;
    private String winner;
    private String notation;
    private String validMoves;
    private String moves;
    private String whitePlayerUsername;
    private String blackPlayerUsername;
    private Integer botDifficulty;
    private String isVisible;

    @DynamoDBHashKey(attributeName = "gameId")
    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    @DynamoDBAttribute(attributeName = "active")
    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    @DynamoDBAttribute(attributeName = "winner")
    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    @DynamoDBAttribute(attributeName = "notation")
    public String getNotation() {
        return notation;
    }

    public void setNotation(String notation) {
        this.notation = notation;
    }

    @DynamoDBAttribute(attributeName = "validMoves")
    public String getValidMoves() {
        return validMoves;
    }

    public void setValidMoves(@Nonnull String validMoves) {
        this.validMoves = validMoves;
    }

    @DynamoDBAttribute(attributeName = "moves")
    public String getMoves() {
        return moves;
    }

    public void setMoves(String moves) {
        this.moves = moves;
    }

    @DynamoDBAttribute(attributeName = "whitePlayerUsername")
    public String getWhitePlayerUsername() {
        return whitePlayerUsername;
    }

    public void setWhitePlayerUsername(String whitePlayerUsername) {
        this.whitePlayerUsername = whitePlayerUsername;
    }

    @DynamoDBAttribute(attributeName = "blackPlayerUsername")
    public String getBlackPlayerUsername() {
        return blackPlayerUsername;
    }

    public void setBlackPlayerUsername(String blackPlayerUsername) {
        this.blackPlayerUsername = blackPlayerUsername;
    }

    @DynamoDBAttribute(attributeName = "botDifficulty")
    public Integer getBotDifficulty() {
        return botDifficulty;
    }

    public void setBotDifficulty(Integer botDifficulty) {
        this.botDifficulty = botDifficulty;
    }

    @DynamoDBAttribute(attributeName = "isVisible")
    public String getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(String isVisible) {
        this.isVisible = isVisible;
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId='" + gameId + '\'' +
                ", active='" + active + '\'' +
                ", winner='" + winner + '\'' +
                ", notation='" + notation + '\'' +
                ", validMoves='" + validMoves + '\'' +
                ", moves='" + moves + '\'' +
                ", whitePlayerUsername='" + whitePlayerUsername + '\'' +
                ", blackPlayerUsername='" + blackPlayerUsername + '\'' +
                ", botDifficulty=" + botDifficulty +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game game = (Game) o;
        return gameId.equals(game.gameId) &&
                Objects.equals(active, game.active) &&
                Objects.equals(winner, game.winner) &&
                Objects.equals(notation, game.notation) &&
                Objects.equals(validMoves, game.validMoves) &&
                Objects.equals(moves, game.moves) &&
                Objects.equals(whitePlayerUsername, game.whitePlayerUsername) &&
                Objects.equals(blackPlayerUsername, game.blackPlayerUsername) &&
                Objects.equals(botDifficulty, game.botDifficulty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, active, winner, notation, validMoves,
                moves, whitePlayerUsername, blackPlayerUsername, botDifficulty);
    }
}
