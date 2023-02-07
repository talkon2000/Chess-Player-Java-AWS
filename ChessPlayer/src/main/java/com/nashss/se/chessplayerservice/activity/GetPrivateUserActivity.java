package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.GetPrivateUserRequest;
import com.nashss.se.chessplayerservice.activity.response.GetUserResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;

import javax.inject.Inject;

public class GetPrivateUserActivity {

    private final UserDao dao;

    @Inject
    public GetPrivateUserActivity(UserDao dao) {
        this.dao = dao;
    }

    public GetUserResponse handleRequest(GetPrivateUserRequest request) {
        if (request.getUsername() == null) {
            throw new InvalidRequestException("Username can not be null");
        }

        User user = dao.load(request.getUsername());
        if (user == null) {
            throw new InvalidRequestException("There is no user with that username");
        }
        return GetUserResponse.builder()
                .withUser(user)
                .build();
    }
}
