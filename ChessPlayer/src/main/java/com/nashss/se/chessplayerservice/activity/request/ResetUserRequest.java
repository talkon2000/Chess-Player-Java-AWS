package com.nashss.se.chessplayerservice.activity.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = ResetUserRequest.Builder.class)
public class ResetUserRequest {

    private final String username;

    private ResetUserRequest(String username) {
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

        public ResetUserRequest build() {
            return new ResetUserRequest(username);
        }
    }
}
