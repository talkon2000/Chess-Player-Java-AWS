package com.nashss.se.chessplayerservice.activity.response;

import com.nashss.se.chessplayerservice.dynamodb.models.Game;

import java.util.Set;

public class GetAllGamesResponse {

    private final Set<Game> games;

    private GetAllGamesResponse(Set<Game> games) {
        this.games = (games == null) ? null : Set.copyOf(games);
    }

    public Set<Game> getGames() {
        return (games == null) ? null : Set.copyOf(games);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Set<Game> games;

        public Builder withGames(Set<Game> games) {
            this.games = games;
            return this;
        }

        public GetAllGamesResponse build() {
            return new GetAllGamesResponse(games);
        }
    }
}
