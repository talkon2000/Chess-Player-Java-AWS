package com.nashss.se.chessplayerservice.activity.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = GetAllGamesRequest.Builder.class)
public class GetAllGamesRequest {
    private final String username;

    private GetAllGamesRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String username;

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public GetAllGamesRequest build() {
            return new GetAllGamesRequest(username);
        }

    }
}
