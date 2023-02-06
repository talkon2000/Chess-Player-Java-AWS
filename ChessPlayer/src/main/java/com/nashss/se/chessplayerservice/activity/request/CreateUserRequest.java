package com.nashss.se.chessplayerservice.activity.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = CreateUserRequest.Builder.class)
public class CreateUserRequest {
    private final String userId;
    private final String email;
    private final String username;

    private CreateUserRequest(String userId, String email, String username) {
        this.userId = userId;
        this.email = email;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String userId;
        private String email;
        private String username;

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public CreateUserRequest build() {
            return new CreateUserRequest(userId, email, username);
        }
    }
}
