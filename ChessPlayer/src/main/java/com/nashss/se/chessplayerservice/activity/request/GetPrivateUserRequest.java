package com.nashss.se.chessplayerservice.activity.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = GetPrivateUserRequest.Builder.class)
public class GetPrivateUserRequest {
    private final String username;

    private GetPrivateUserRequest(String username) {
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

        public GetPrivateUserRequest build() {
            return new GetPrivateUserRequest(username);
        }
    }
}
