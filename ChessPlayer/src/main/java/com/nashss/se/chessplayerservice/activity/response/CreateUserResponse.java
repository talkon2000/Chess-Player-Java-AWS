package com.nashss.se.chessplayerservice.activity.response;

import com.nashss.se.chessplayerservice.dynamodb.models.User;

public class CreateUserResponse {
    private final User user;

    private CreateUserResponse(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private User user;

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        public CreateUserResponse build() {
            return new CreateUserResponse(user);
        }
    }
}
