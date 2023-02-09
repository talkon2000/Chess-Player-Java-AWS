package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.GetPublicUserRequest;
import com.nashss.se.chessplayerservice.activity.response.GetUserResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;

import javax.inject.Inject;

public class GetPublicUserActivity {

    private final UserDao dao;

    @Inject
    public GetPublicUserActivity(UserDao dao) {
        this.dao = dao;
    }

    public GetUserResponse handleRequest(GetPublicUserRequest request) {
        if (request.getUsername() == null) {
            throw new InvalidRequestException("Username can not be null");
        }

        User user = dao.loadPublicUser(request.getUsername());
        if (user == null) {
            throw new InvalidRequestException("There is no user with that username");
        }
        return GetUserResponse.builder()
                .withUser(user)
                .build();
    }
}
