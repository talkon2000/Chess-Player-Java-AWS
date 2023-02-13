package com.nashss.se.chessplayerservice.activity.request;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

public class GetPublicUserRequest {
    private final String username;

    private GetPublicUserRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    //CHECKSTYLE:OFF:Builder
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

        public GetPublicUserRequest build() {
            return new GetPublicUserRequest(username);
        }
    }
}
