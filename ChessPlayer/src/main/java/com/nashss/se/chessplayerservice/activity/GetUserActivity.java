package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.GetUserRequest;
import com.nashss.se.chessplayerservice.activity.response.GetUserResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;

import javax.inject.Inject;

public class GetUserActivity {

    private final UserDao dao;

    @Inject
    public GetUserActivity(UserDao dao) {
        this.dao = dao;
    }

    public GetUserResponse handleRequest(GetUserRequest request) {
        if (request.getUserId() == null) {
            throw new InvalidRequestException("User Id is null");
        }

        // If the requester is viewing their own profile
        if (request.getRequesterId() != null && request.getRequesterId().equals(request.getUserId())) {
            User user = dao.load(request.getUserId());
            if (user == null) {
                throw new InvalidRequestException("There is no user with that Id");
            }
            return GetUserResponse.builder()
                    .withUser(user)
                    .build();
        }

        // Only viewing public information
        User user = dao.loadUserLimited(request.getUserId());
        if (user == null) {
            throw new InvalidRequestException("There is no user with that Id");
        }
        return GetUserResponse.builder()
                .withUser(user)
                .build();
    }
}
