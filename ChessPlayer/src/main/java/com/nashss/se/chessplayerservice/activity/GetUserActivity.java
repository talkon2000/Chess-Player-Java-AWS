package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.GetUserRequest;
import com.nashss.se.chessplayerservice.activity.response.GetUserResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
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

        return GetUserResponse.builder()
                .withUser(dao.load(request.getUserId()))
                .build();
    }
}
