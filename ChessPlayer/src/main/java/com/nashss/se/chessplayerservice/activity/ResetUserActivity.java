package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.ResetUserRequest;
import com.nashss.se.chessplayerservice.activity.response.ResetUserResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;

public class ResetUserActivity {

    private final UserDao userDao;

    public ResetUserActivity(UserDao userDao) {
        this.userDao = userDao;
    }

    public ResetUserResponse handleRequest(ResetUserRequest request) {
        if (request.getUsername() == null) {
            throw new InvalidRequestException("Username cannot be null");
        }

        User user = userDao.load(request.getUsername());
        if (user == null) {
            throw new InvalidRequestException("That user does not exist");
        }
        userDao.deleteUser(user);
        return ResetUserResponse.builder()
                .withUser(user)
                .build();
    }
}
