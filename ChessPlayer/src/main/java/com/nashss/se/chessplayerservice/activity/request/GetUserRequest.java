package com.nashss.se.chessplayerservice.activity.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = GetUserRequest.Builder.class)
public class GetUserRequest {
    private final String requesterId;
    private final String userId;

    private GetUserRequest(String requesterId, String userId) {
        this.requesterId = requesterId;
        this.userId = userId;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public String getUserId() {
        return userId;
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String requesterId;
        private String userId;

        public Builder withRequesterId(String requesterId) {
            this.requesterId = requesterId;
            return this;
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public GetUserRequest build() {
            return new GetUserRequest(requesterId, userId);
        }
    }
}
